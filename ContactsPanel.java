import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ContactsPanel extends JPanel {
    private JButton searchButton;
    private JTextField searchField;
    private JList<String> contactsList;
    private DefaultListModel<String> contactsModel;
    private ChatPanel chatPanel;
    
    public ContactsPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
        initComponents();
        setupLayout();
        setupListeners();
        Design.stylePanel(this);
    }
    
    private void initComponents() {
        // Заголовок
        JLabel titleLabel = new JLabel("Контакты", SwingConstants.CENTER);
        Design.styleHeaderLabel(titleLabel);
        
        // Поле поиска
        searchField = new JTextField();
        Design.styleTextField(searchField);
        searchField.setToolTipText("Введите имя или IP для поиска");
        
        // Кнопка поиска
        searchButton = new JButton("Найти собеседника");
        Design.styleButton(searchButton);
        searchButton.setIcon(new ImageIcon("search_icon.png")); // Добавьте свою иконку
        
        // Список контактов
        contactsModel = new DefaultListModel<>();
        // Добавим тестовые контакты
        contactsModel.addElement("Алексей (192.168.1.101)");
        contactsModel.addElement("Мария (192.168.1.102)");
        contactsModel.addElement("Сергей (192.168.1.103)");
        contactsModel.addElement("Ольга (192.168.1.104)");
        
        contactsList = new JList<>(contactsModel);
        Design.styleList(contactsList);
        
        // Панель поиска
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        Design.stylePanel(searchPanel);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Заголовок
        add(new JLabel("Контакты", SwingConstants.CENTER), BorderLayout.NORTH);
        
        // Панель поиска
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        Design.stylePanel(searchPanel);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        add(searchPanel, BorderLayout.NORTH);
        
        // Список контактов с заголовком
        JScrollPane scrollPane = new JScrollPane(contactsList);
        Design.styleScrollPane(scrollPane);
        
        JPanel contactsContainer = new JPanel(new BorderLayout());
        Design.stylePanel(contactsContainer);
        contactsContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Design.DARK_BORDEAUX, 1),
            "Доступные контакты",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 12),
            Design.TEXT_SECONDARY
        ));
        contactsContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(contactsContainer, BorderLayout.CENTER);
        
        // Панель статуса
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Design.stylePanel(statusPanel);
        JLabel statusLabel = new JLabel("⚪ Поиск в локальной сети...");
        Design.styleLabel(statusLabel);
        statusLabel.setForeground(Design.TEXT_SECONDARY);
        statusPanel.add(statusLabel);
        
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupListeners() {
        searchButton.addActionListener(e -> searchContact());
        
        contactsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && contactsList.getSelectedValue() != null) {
                String selectedContact = contactsList.getSelectedValue();
                chatPanel.setContactName(selectedContact);
            }
        });
    }
    
    private void searchContact() {
        String searchText = searchField.getText().trim();
        if (!searchText.isEmpty()) {
            // Здесь будет логика поиска в локальной сети
            JOptionPane.showMessageDialog(this,
                "Поиск собеседника: " + searchText + "\n(Функция поиска будет реализована позже)",
                "Поиск",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}