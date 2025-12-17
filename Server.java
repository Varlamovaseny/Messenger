package messengerdlan;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private Map<String, ClientHandler> clients;
    private List<User> registeredUsers;
    
    public Server() {
        threadPool = Executors.newCachedThreadPool();
        clients = new ConcurrentHashMap<>();
        registeredUsers = new CopyOnWriteArrayList<>();
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен на порту " + PORT);
            System.out.println("IP адрес сервера: " + InetAddress.getLocalHost().getHostAddress());
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }
    
    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private String username;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                
                // Получаем логин пользователя
                Message loginMessage = (Message) in.readObject();
                this.username = loginMessage.getSender();
                
                // Регистрируем пользователя
                User user = new User(username, 
                    socket.getInetAddress().getHostAddress(),
                    socket.getPort());
                registeredUsers.add(user);
                clients.put(username, this);
                
                System.out.println(username + " подключился");
                
                broadcastUserList();
                
                while (true) {
                    Message message = (Message) in.readObject();
                    processMessage(message);
                }
                
            } catch (EOFException | SocketException e) {
                System.out.println(username + " отключился");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Ошибка: " + e.getMessage());
            } finally {
                disconnect();
            }
        }
        
        private void processMessage(Message message) throws IOException {
            switch (message.getType()) {
                case TEXT:
                case EMOJI:
                case IMAGE:
                case FILE:
                    sendMessageToReceiver(message);
                    break;
                    
                case PRIVATE_MESSAGE:
                    sendPrivateMessage(message);
                    break;
                    
                case SEARCH_REQUEST:
                    handleSearchRequest(message);
                    break;
                    
                case STATUS_UPDATE:
                    broadcastUserList();
                    break;
            }
        }
        
        private void sendMessageToReceiver(Message message) throws IOException {
            if ("ALL".equals(message.getReceiver())) {
                for (ClientHandler client : clients.values()) {
                    if (!client.username.equals(message.getSender())) {
                        client.sendMessage(message);
                    }
                }
            } else {
                ClientHandler receiver = clients.get(message.getReceiver());
                if (receiver != null) {
                    receiver.sendMessage(message);
                }
            }
        }
        
        private void sendPrivateMessage(Message message) throws IOException {
            ClientHandler receiver = clients.get(message.getReceiver());
            if (receiver != null) {
                receiver.sendMessage(message);
                sendMessage(message);
            }
        }
        
        private void handleSearchRequest(Message message) {
            String searchTerm = message.getContent().toLowerCase();
            List<User> searchResults = new ArrayList<>();
            
            for (User user : registeredUsers) {
                if (user.getUsername().toLowerCase().contains(searchTerm) &&
                    !user.getUsername().equals(username)) {
                    searchResults.add(user);
                }
            }
            
            Message resultMessage = new Message(
                MessageType.SEARCH_RESULT,
                "SERVER",
                username,
                "Найдено: " + searchResults.size() + " пользователей"
            );
            
            try {
                sendMessage(resultMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void sendMessage(Message message) throws IOException {
            out.writeObject(message);
            out.flush();
        }
        
        private void disconnect() {
            try {
                if (username != null) {
                    clients.remove(username);
                    registeredUsers.removeIf(user -> user.getUsername().equals(username));
                    broadcastUserList();
                }
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void broadcastUserList() {
            StringBuilder userList = new StringBuilder();
            for (User user : registeredUsers) {
                userList.append(user.getUsername())
                       .append(",")
                       .append(user.isOnline() ? "online" : "offline")
                       .append(";");
            }
            
            Message userListMessage = new Message(
                MessageType.USER_LIST,
                "SERVER",
                "ALL",
                userList.toString()
            );
            
            for (ClientHandler client : clients.values()) {
                try {
                    client.sendMessage(userListMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}