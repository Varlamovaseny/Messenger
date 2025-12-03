import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel contactLabel;
    
    public ChatPanel() {
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel(this);
    }
    
    private void initComponents() {
        // Метка текущего контакта
        contactLabel = new JLabel("Выберите собеседника");
        Design.styleHeaderLabel(contactLabel);
        
        // Область чата
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        Design.styleTextArea(chatArea);
        chatArea.append("Добро пожаловать в бордовый мессенджер!\n");
        chatArea.append("Выберите собеседника из списка контактов.\n");
        chatArea.append("----------------------------------------\n");
        
        // Поле ввода сообщения
        messageField = new JTextField();
        Design.styleTextField(messageField);
        messageField.setEnabled(false);
        
        // Кнопка отправки
        sendButton = new JButton("Отправить");
        Design.styleButton(sendButton);
        sendButton.setEnabled(false);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Заголовок с именем контакта
        JPanel headerPanel = new JPanel(new BorderLayout());
        Design.stylePanel(headerPanel);
        headerPanel.add(contactLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Область чата
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        Design.styleScrollPane(chatScrollPane);
        
        JPanel chatContainer = new JPanel(new BorderLayout());
        Design.stylePanel(chatContainer);
        chatContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.DARK_BORDEAUX, 1),
            "Сообщения",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_SECONDARY
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
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            String timestamp = java.time.LocalTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm")
            );
            chatArea.append("\n[Вы] " + timestamp + ":\n" + message + "\n");
            messageField.setText("");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        }
    }
    
    public void setContactName(String contactName) {
        contactLabel.setText("Чат с: " + contactName.split("\\(")[0].trim());
        messageField.setEnabled(true);
        sendButton.setEnabled(true);
        chatArea.append("\n--- Начат чат с " + contactName + " ---\n");
    }
}