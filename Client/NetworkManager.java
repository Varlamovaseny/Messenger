import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class NetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;
    private boolean connected = false;
    
    private MessageListener messageListener;
    private UserListListener userListListener;
    
    public interface MessageListener {
        void onMessageReceived(String sender, String message);
    }
    
    public interface UserListListener {
        void onUserListUpdated(List<String> users);
    }
    
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
    
    public void setUserListListener(UserListListener listener) {
        this.userListListener = listener;
    }
    
    public boolean connect(String username, String serverIp) {
        try {
            this.username = username;
            
            System.out.println("Подключение к " + serverIp + ":12345...");
            
            socket = new Socket(serverIp, 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Отправляем логин
            Message loginMsg = new Message(MessageType.LOGIN, username, "SERVER", "");
            out.writeObject(loginMsg);
            out.flush();
            
            connected = true;
            
            // Запускаем поток для получения сообщений
            new Thread(this::receiveMessages).start();
            
            return true;
            
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "Не удалось подключиться к серверу!\n\n" +
                    "Причина: " + e.getMessage() + "\n\n" +
                    "Убедитесь что:\n" +
                    "1. Сервер запущен на " + serverIp + "\n" +
                    "2. Правильно указан IP адрес\n" +
                    "3. Компьютеры в одной сети\n" +
                    "4. Фаервол не блокирует порт 12345",
                    "Ошибка подключения",
                    JOptionPane.ERROR_MESSAGE);
            });
            return false;
        }
    }
    
    private void receiveMessages() {
        try {
            while (connected) {
                Message msg = (Message) in.readObject();
                processMessage(msg);
            }
        } catch (EOFException | SocketException e) {
            System.out.println("Соединение с сервером разорвано");
            connected = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void processMessage(Message msg) {
        switch (msg.getType()) {
            case TEXT:
            case PRIVATE_MESSAGE:
                if (messageListener != null) {
                    SwingUtilities.invokeLater(() -> {
                        messageListener.onMessageReceived(msg.getSender(), msg.getContent());
                    });
                }
                break;
                
            case USER_LIST:
                if (userListListener != null) {
                    List<String> users = parseUserList(msg.getContent());
                    SwingUtilities.invokeLater(() -> {
                        userListListener.onUserListUpdated(users);
                    });
                }
                break;
                
            case SEARCH_RESULT:
                // Обработка результатов поиска
                break;
        }
    }
    
    private List<String> parseUserList(String userListData) {
        List<String> users = new ArrayList<>();
        if (userListData == null || userListData.isEmpty()) {
            return users;
        }
        
        String[] entries = userListData.split(";");
        for (String entry : entries) {
            if (!entry.isEmpty()) {
                String[] parts = entry.split(",");
                if (parts.length > 0) {
                    String user = parts[0];
                    if (!user.equals(username)) {
                        users.add(user);
                    }
                }
            }
        }
        return users;
    }
    
    public void sendMessage(String receiver, String message) {
        if (!connected || message.trim().isEmpty()) return;
        
        try {
            Message msg = new Message(MessageType.TEXT, username, receiver, message);
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void searchUsers(String searchTerm) {
        if (!connected) return;
        
        try {
            Message msg = new Message(MessageType.SEARCH_REQUEST, username, "SERVER", searchTerm);
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void disconnect() {
        try {
            if (connected) {
                Message logoutMsg = new Message(MessageType.LOGOUT, username, "SERVER", "");
                out.writeObject(logoutMsg);
                out.flush();
            }
            if (socket != null) socket.close();
            connected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isConnected() { return connected; }
    public String getUsername() { return username; }
}