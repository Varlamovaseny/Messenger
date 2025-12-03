import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatFrame extends JFrame {
    private String contactName;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel contactLabel;
    
    public ChatFrame(String contactName) {
        this.contactName = contactName;
        setupFrame();
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel((JPanel) getContentPane());
        
        setVisible(true);
    }
    
    private void setupFrame() {
        setTitle("Чат с " + contactName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 600));
        setMinimumSize(new Dimension(400, 400));
        
        // Позиционируем окно смещенным относительно главного
        Point mainLocation = MessengerFrame.getFrames()[0].getLocation();
        setLocation(mainLocation.x + 50, mainLocation.y + 50);
    }
    
    private void initComponents() {
        // Метка с именем контакта
        contactLabel = new JLabel(contactName, SwingConstants.CENTER);
        Design.styleHeaderLabel(contactLabel);
        
        // Область чата
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        Design.styleTextArea(chatArea);
        
        // Добавляем приветственное сообщение
        String welcomeMessage = String.format(
            "Чат с %s\n%s\n%s\n",
            contactName,
            "Начало диалога",
            "--------------------------------------------------"
        );
        chatArea.append(welcomeMessage);
        
        // Поле ввода сообщения
        messageField = new JTextField();
        Design.styleTextField(messageField);
        
        // Кнопка отправки
        sendButton = new JButton("Отправить");
        Design.styleButton(sendButton);
    }
    
    private void setupLayout() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout(0, 10));
        ((JPanel) container).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Заголовок
        JPanel headerPanel = new JPanel(new BorderLayout());
        Design.stylePanel(headerPanel);
        
        // Кнопка закрытия
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setForeground(Design.TEXT_ACCENT);
        closeButton.setBackground(Design.DARK_BORDEAUX);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        headerPanel.add(contactLabel, BorderLayout.CENTER);
        headerPanel.add(closeButton, BorderLayout.EAST);
        
        container.add(headerPanel, BorderLayout.NORTH);
        
        // Область чата
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        Design.styleScrollPane(chatScrollPane);
        
        JPanel chatContainer = new JPanel(new BorderLayout());
        Design.stylePanel(chatContainer);
        chatContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.DARK_BORDEAUX, 1),
            "История сообщений",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_SECONDARY
        ));
        chatContainer.add(chatScrollPane, BorderLayout.CENTER);
        
        container.add(chatContainer, BorderLayout.CENTER);
        
        // Панель ввода сообщения
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        Design.stylePanel(inputPanel);
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        container.add(inputPanel, BorderLayout.SOUTH);
        
        pack();
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
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String timestamp = java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm")
            );
            
            // Добавляем сообщение в историю
            addMessage(message, true);
            
            // Симулируем ответ через 1-3 секунды
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
            
            // Звуковое уведомление для входящих сообщений
            if (!isMyMessage) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }
    
    private void simulateResponse(String myMessage) {
        // Симуляция ответа собеседника через случайную задержку
        new Thread(() -> {
            try {
                // Случайная задержка 1-3 секунды
                Thread.sleep(1000 + (int)(Math.random() * 2000));
                
                // Генерация ответа на основе введенного сообщения
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
        } else {
            // Случайный ответ из набора
            String[] responses = {
                "Понятно, продолжайте.",
                "Плюс вайб",
                "Хм, нужно подумать над этим.",
                "Спасибо за информацию! Мне все равно",
                "Как интересно!",
                "Ты че бредишь",
                "Я вас понимаю."
            };
            return responses[(int)(Math.random() * responses.length)];
        }
    }
}