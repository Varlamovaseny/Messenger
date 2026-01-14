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
        loadWelcomeMessage();
    }
    
    private void initComponents() {
        contactLabel = new JLabel("Чат с: " + contactName);
        Design.styleHeaderLabel(contactLabel);
        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        Design.styleTextArea(chatArea);
        
        messageField = new JTextField();
        Design.styleTextField(messageField);
        
        sendButton = new JButton("Отправить");
        Design.styleButton(sendButton);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        Design.stylePanel(headerPanel);
        headerPanel.add(contactLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        Design.styleScrollPane(chatScrollPane);
        
        JPanel chatContainer = new JPanel(new BorderLayout());
        Design.stylePanel(chatContainer);
        chatContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.TURQUOISE, 2),
            "Сообщения",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_PRIMARY
        ));
        chatContainer.add(chatScrollPane, BorderLayout.CENTER);
        
        add(chatContainer, BorderLayout.CENTER);
        
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
    
    private void loadWelcomeMessage() {
        String welcome = "Чат с " + contactName + "\n" +
                        "------------------------\n";
        chatArea.append(welcome);
    }
    
    public void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            // Здесь сообщение будет отправляться через NetworkManager из ContactsPanel
            // Мы просто добавляем его в историю
            addMessage(message, true);
            messageField.setText("");
            messageField.requestFocus();
        }
    }
    
    public void addMessage(String message, boolean isMyMessage) {
        String timestamp = java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm")
        );
        
        String prefix = isMyMessage ? "[Вы]" : "[" + contactName + "]";
        String formatted = String.format("\n%s %s:\n%s\n", prefix, timestamp, message);
        
        SwingUtilities.invokeLater(() -> {
            chatArea.append(formatted);
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
            
            if (!isMyMessage) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }
    
    public String getContactName() {
        return contactName;
    }
}