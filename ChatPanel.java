import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatPanel extends JPanel {
    private String contactName;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel contactLabel;
    
    public ChatPanel(String contactName) {
        this.contactName = contactName;
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel(this);
        loadInitialMessages();
    }
    
    private void initComponents() {
        // Метка с именем контакта
        contactLabel = new JLabel("Чат с: " + contactName);
        Design.styleHeaderLabel(contactLabel);
        
        // Область чата
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        Design.styleTextArea(chatArea);
        
        // Поле ввода сообщения
        messageField = new JTextField();
        Design.styleTextField(messageField);
        
        // Кнопка отправки
        sendButton = new JButton("Отправить");
        Design.styleButton(sendButton);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Заголовок
        JPanel headerPanel = new JPanel(new BorderLayout());
        Design.stylePanel(headerPanel);
        headerPanel.add(contactLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Область чата
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        Design.styleScrollPane(chatScrollPane);
        
        JPanel chatContainer = new JPanel(new BorderLayout());
        Design.stylePanel(chatContainer);
        // Используем бирюзовый цвет из нового дизайна
        chatContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.TURQUOISE, 2), // Бирюзовая рамка
            "История сообщений",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_PRIMARY // Используем основной цвет текста
        ));
        chatContainer.add(chatScrollPane, BorderLayout.CENTER);
        
        add(chatContainer, BorderLayout.CENTER);
        
        // Панель ввода сообщения
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        Design.stylePanel(inputPanel);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        sendButton.addActionListener(e -> sendMessage());
        
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }
    
    private void loadInitialMessages() {
        String welcomeMessage = String.format(
            "Добро пожаловать в чат с %s!\n%s\n",
            contactName,
            "--------------------------------------------------"
        );
        chatArea.append(welcomeMessage);
        
        // Добавляем несколько примеров сообщений для реалистичности
        if (contactName.equals("Алексей Петров")) {
            chatArea.append("\n[Алексей Петров] 10:30:\nПривет! Как дела?\n");
            chatArea.append("\n[Вы] 10:32:\nПривет! Всё отлично, спасибо!\n");
        } else if (contactName.equals("Мария Иванова")) {
            chatArea.append("\n[Мария Иванова] Вчера, 15:45:\nГотовы к встрече завтра?\n");
            chatArea.append("\n[Вы] Вчера, 16:20:\nДа, конечно! В 14:00?\n");
        }
    }
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            addMessage(message, true);
            
            simulateResponse(message);
            
            messageField.setText("");
            messageField.requestFocus();
        }
    }
    
    public void addMessage(String message, boolean isMyMessage) {
        String timestamp = java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm")
        );
        
        String prefix = isMyMessage ? "[Вы]" : "[" + contactName + "]";
        String formattedMessage = String.format("\n%s %s:\n%s\n", 
            prefix, timestamp, message);
        
        SwingUtilities.invokeLater(() -> {
            chatArea.append(formattedMessage);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
            
            if (!isMyMessage) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }
    
    private void simulateResponse(String myMessage) {
        new Thread(() -> {
            try {
                Thread.sleep(1000 + (int)(Math.random() * 2000));
                
                String response = generateResponse(myMessage);
                
                SwingUtilities.invokeLater(() -> {
                    addMessage(response, false);
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private String generateResponse(String myMessage) {
        // Простая логика генерации ответов
        String lowerMessage = myMessage.toLowerCase();
        
        if (lowerMessage.contains("привет") || lowerMessage.contains("здравствуй")) {
            return "Привет! Как дела?";
        } else if (lowerMessage.contains("как дела")) {
            return "Всё отлично, спасибо! А у тебя?";
        } else if (lowerMessage.contains("пока") || lowerMessage.contains("до свидания")) {
            return "До встречи! Было приятно пообщаться.";
        } else if (lowerMessage.contains("?")) {
            return "Интересный вопрос! Дай мне подумать...";
        } else if (lowerMessage.length() < 10) {
            return "Коротко, но ясно :)";
        } else {
            // Случайный ответ из набора
            String[] responses = {
                "Понятно, продолжайте.",
                "Интересно! Расскажите подробнее.",
                "Согласен с вами.",
                "Хм, нужно подумать над этим.",
                "Спасибо за информацию!",
                "Как интересно!",
                "Это действительно важно.",
                "Продолжайте в том же духе!",
                "Я вас понимаю.",
                "Отличная мысль!"
            };
            return responses[(int)(Math.random() * responses.length)];
        }
    }
    
    public String getContactName() {
        return contactName;
    }
}