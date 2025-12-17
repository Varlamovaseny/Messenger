import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ContactsPanel extends JPanel {
    private JButton searchButton;
    private JTextField searchField;
    private JList<String> contactsList;
    private DefaultListModel<String> contactsModel;
    private JLabel statusLabel;
    private JButton refreshButton;
    private ChatContainerPanel chatContainerPanel;
    
    private static Map<String, JFrame> openChats = new HashMap<>();
    
    public ContactsPanel(ChatContainerPanel chatContainerPanel) {
        this.chatContainerPanel = chatContainerPanel;
        this.contactsModel = new DefaultListModel<>();
        
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel(this);
        loadSampleContacts();
    }
    
    private void initComponents() {
        searchField = new JTextField();
        Design.styleTextField(searchField);
        searchField.setToolTipText("Введите имя для поиска");
        
        searchButton = new JButton("Поиск");
        Design.styleButton(searchButton);
        
        refreshButton = new JButton("Обновить");
        Design.styleButton(refreshButton);
        
        contactsList = new JList<>(contactsModel);
        Design.styleList(contactsList);
        
        statusLabel = new JLabel("Доступные контакты");
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
            BorderFactory.createLineBorder(Design.TURQUOISE, 2), // Бирюзовая рамка
            "Список контактов",
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
                String selectedContact = contactsList.getSelectedValue();
                chatContainerPanel.showChatForContact(selectedContact);
            }
        });
        
        contactsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedContact = contactsList.getSelectedValue();
                    if (selectedContact != null) {
                        chatContainerPanel.showChatForContact(selectedContact);
                        contactsList.clearSelection();
                    }
                }
            }
        });
    }
    
    private void loadSampleContacts() {
        contactsModel.addElement("пусик");
        contactsModel.addElement("сюсик");
        contactsModel.addElement("блабла");
        statusLabel.setText("Контактов: " + contactsModel.size());
    }
    
    private void searchContact() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            for (int i = 0; i < contactsModel.size(); i++) {
                String contact = contactsModel.get(i);
                if (contact.toLowerCase().contains(searchText.toLowerCase())) {
                    contactsList.setSelectedIndex(i);
                    contactsList.ensureIndexIsVisible(i);
                    statusLabel.setText("Найден: " + contact);
                    break;
                }
            }
            
            if (contactsList.isSelectionEmpty()) {
                statusLabel.setText("Контакт не найден");
            }
        } else {
            statusLabel.setText("Контактов: " + contactsModel.size());
        }
    }
    
    private void refreshContacts() {
        contactsList.clearSelection();
        statusLabel.setText("Список обновлен");
    }
    
    private void createNewChat() {
        String newContactName = JOptionPane.showInputDialog(
            this,
            "Введите имя нового контакта:",
            "Новый чат",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (newContactName != null && !newContactName.trim().isEmpty()) {
            newContactName = newContactName.trim();
            
            boolean exists = false;
            for (int i = 0; i < contactsModel.size(); i++) {
                if (contactsModel.get(i).equals(newContactName)) {
                    exists = true;
                    break;
                }
            }
            
            if (!exists) {
                contactsModel.addElement(newContactName);
                statusLabel.setText("Добавлен: " + newContactName);
            }
            
            chatContainerPanel.showChatForContact(newContactName);
        }
    }
    
    public static void updateChatHistory(String contactName, String message, boolean isMyMessage) {

    }
}