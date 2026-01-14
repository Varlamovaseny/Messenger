import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JTextField serverIpField;
    private JButton connectButton;
    
    public LoginWindow() {
        setupWindow();
        initComponents();
        setupLayout();
        
        setVisible(true);
    }
    
    private void setupWindow() {
        setTitle("Вход в мессенджер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        serverIpField = new JTextField("localhost");
        serverIpField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        connectButton = new JButton("Подключиться");
        connectButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        connectButton.setBackground(new Color(64, 224, 208));
        connectButton.setForeground(Color.WHITE);
        connectButton.addActionListener(e -> connect());
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Заголовок
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Вход в мессенджер", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titleLabel, gbc);
        
        // Имя пользователя
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(new JLabel("Ваше имя:"), gbc);
        
        gbc.gridx = 1;
        add(usernameField, gbc);
        
        // IP сервера
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("IP сервера:"), gbc);
        
        gbc.gridx = 1;
        add(serverIpField, gbc);
        
        // Подсказка
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel hintLabel = new JLabel("<html><small>• localhost - если сервер на этом компьютере<br>• 192.168.1.XXX - если сервер на другом компьютере</small></html>");
        hintLabel.setForeground(Color.GRAY);
        add(hintLabel, gbc);
        
        // Кнопка подключения
        gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(connectButton, gbc);
        
        // Устанавливаем фокус
        usernameField.requestFocus();
    }
    
    private void connect() {
        String username = usernameField.getText().trim();
        String serverIp = serverIpField.getText().trim();
        
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите имя пользователя!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (serverIp.isEmpty()) {
            serverIp = "localhost";
        }
        
        // Закрываем окно входа
        dispose();
        
        // Запускаем главное окно мессенджера
        new MessengerFrame(username, serverIp);
    }
}