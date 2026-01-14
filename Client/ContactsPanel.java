import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ContactsPanel extends JPanel {
    private JButton searchButton;
    private JTextField searchField;
    private JList<String> contactsList;
    private DefaultListModel<String> contactsModel;
    private JLabel statusLabel;
    private JButton refreshButton;
    private ChatContainerPanel chatContainerPanel;
    private NetworkManager networkManager;
    private String username;
    private String serverIp;
    
    public ContactsPanel(ChatContainerPanel chatContainerPanel, String username, String serverIp) {
        this.chatContainerPanel = chatContainerPanel;
        this.username = username;
        this.serverIp = serverIp;
        this.contactsModel = new DefaultListModel<>();
        
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel(this);
        
        // Подключаемся к серверу
        connectToServer();
    }
    
    private void connectToServer() {
        networkManager = new NetworkManager();
        
        // Подключаемся
        boolean connected = networkManager.connect(username, serverIp);
        
        if (connected) {
            statusLabel.setText("✓ Подключено к серверу");
            
            // Устанавливаем слушатели
            networkManager.setMessageListener((sender, message) -> {
                // Показываем вкладку с отправителем
                SwingUtilities.invokeLater(() -> {
                    chatContainerPanel.showChatForContact(sender);
                    ChatPanel chatPanel = chatContainerPanel.getChatPanel(sender);
                    if (chatPanel != null) {
                        chatPanel.addMessage(message, false);
                    }
                });
            });
            
            networkManager.setUserListListener(users -> {
                updateContactsList(users);
            });
            
        } else {
            statusLabel.setText("✗ Не подключено");
            JOptionPane.showMessageDialog(this,
                "Не удалось подключиться к серверу. Запустите программу заново.",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initComponents() {
        searchField = new JTextField();
        Design.styleTextField(searchField);
        searchField.setToolTipText("Поиск пользователя...");
        
        searchButton = new JButton("Поиск");
        Design.styleButton(searchButton);
        
        refreshButton = new JButton("Обновить");
        Design.styleButton(refreshButton);
        
        contactsList = new JList<>(contactsModel);
        Design.styleList(contactsList);
        
        statusLabel = new JLabel("Подключение...");
        Design.styleLabel(statusLabel);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Контакты", SwingConstants.CENTER);
        Design.styleHeaderLabel(titleLabel);
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        Design.stylePanel(searchPanel);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        Design.stylePanel(buttonPanel);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        
        searchPanel.add(buttonPanel, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(contactsList);
        Design.styleScrollPane(scrollPane);
        
        JPanel contactsContainer = new JPanel(new BorderLayout());
        Design.stylePanel(contactsContainer);
        contactsContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.TURQUOISE, 2),
            "Пользователи онлайн",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_PRIMARY
        ));
        contactsContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(contactsContainer, BorderLayout.CENTER);
        
        JPanel statusPanel = new JPanel(new BorderLayout());
        Design.stylePanel(statusPanel);
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        
        JButton newChatButton = new JButton("+ Новый чат");
        Design.styleButton(newChatButton);
        newChatButton.addActionListener(e -> createNewChat());
        statusPanel.add(newChatButton, BorderLayout.EAST);
        
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        searchButton.addActionListener(e -> searchContact());
        refreshButton.addActionListener(e -> refreshContacts());
        
        contactsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && contactsList.getSelectedValue() != null) {
                String selectedContact = extractUsername(contactsList.getSelectedValue());
                chatContainerPanel.showChatForContact(selectedContact);
            }
        });
        
        contactsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedContact = contactsList.getSelectedValue();
                    if (selectedContact != null) {
                        chatContainerPanel.showChatForContact(extractUsername(selectedContact));
                        contactsList.clearSelection();
                    }
                }
            }
        });
    }
    
    private void updateContactsList(List<String> users) {
        contactsModel.clear();
        for (String user : users) {
            contactsModel.addElement("🟢 " + user);
        }
        statusLabel.setText("Онлайн: " + contactsModel.size() + " пользователей");
    }
    
    private void searchContact() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            if (networkManager != null && networkManager.isConnected()) {
                networkManager.searchUsers(searchText);
                statusLabel.setText("Поиск: " + searchText);
            }
            
            for (int i = 0; i < contactsModel.size(); i++) {
                String contact = contactsModel.get(i);
                if (contact.toLowerCase().contains(searchText.toLowerCase())) {
                    contactsList.setSelectedIndex(i);
                    contactsList.ensureIndexIsVisible(i);
                    break;
                }
            }
        }
    }
    
    private void refreshContacts() {
        contactsList.clearSelection();
        statusLabel.setText("Список обновлен");
    }
    
    private void createNewChat() {
        String contactName = JOptionPane.showInputDialog(
            this,
            "Введите имя пользователя:",
            "Новый чат",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (contactName != null && !contactName.trim().isEmpty()) {
            chatContainerPanel.showChatForContact(contactName.trim());
        }
    }
    
    private String extractUsername(String contactEntry) {
        if (contactEntry.startsWith("🟢 ")) {
            return contactEntry.substring(2);
        }
        return contactEntry;
    }
    
    public void disconnect() {
        if (networkManager != null) {
            networkManager.disconnect();
        }
    }
    
    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}