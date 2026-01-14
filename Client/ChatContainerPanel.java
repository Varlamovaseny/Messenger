import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChatContainerPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private Map<String, ChatPanel> chatPanels;
    private JLabel emptyStateLabel;
    
    public ChatContainerPanel() {
        chatPanels = new HashMap<>();
        initComponents();
        setupLayout();
        Design.stylePanel(this);
    }
    
    private void initComponents() {
        // Панель с вкладками
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        styleTabbedPane();
        
        emptyStateLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h3 style='color: #3c3c3c;'>Выберите собеседника</h3>" + // Используем TEXT_ACCENT
            "<p style='color: #646464;'>" + // Используем TEXT_SECONDARY
            "Выберите контакт из списка слева, чтобы начать общение</p>" +
            "</div></html>",
            SwingConstants.CENTER
        );
        emptyStateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emptyStateLabel.setForeground(Design.TEXT_PRIMARY);
        
        emptyStateLabel.setIcon(new EmptyStateIcon());
        emptyStateLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        emptyStateLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(emptyStateLabel, BorderLayout.CENTER);
    }
    
    private void styleTabbedPane() {
        tabbedPane.setBackground(Design.PRIMARY_PINK); // Используем PRIMARY_PINK
        tabbedPane.setForeground(Design.TEXT_PRIMARY);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        tabbedPane.addChangeListener(e -> updateTabComponents());
    }
    
    public void showChatForContact(String contactName) {

        remove(emptyStateLabel);
        if (tabbedPane.getParent() != this) {
            add(tabbedPane, BorderLayout.CENTER);
        }
        
        if (!chatPanels.containsKey(contactName)) {

            ChatPanel chatPanel = new ChatPanel(contactName);
            chatPanels.put(contactName, chatPanel);
            
            tabbedPane.addTab(contactName, chatPanel);
            
            int tabIndex = tabbedPane.getTabCount() - 1;
            tabbedPane.setTabComponentAt(tabIndex, createTabComponent(contactName));
        }
        
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(contactName)) {
                tabbedPane.setSelectedIndex(i);
                break;
            }
        }
        
        revalidate();
        repaint();
    }
    
    private JPanel createTabComponent(String title) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Design.TEXT_PRIMARY);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(Design.TURQUOISE); // Используем TURQUOISE
        closeButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(Design.SHINY_TURQUOISE); // Блестящий бирюзовый
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setForeground(Design.TURQUOISE); // Обычный бирюзовый
            }
        });
        
        closeButton.addActionListener(e -> closeTab(title));
        
        tabPanel.add(titleLabel, BorderLayout.CENTER);
        tabPanel.add(closeButton, BorderLayout.EAST);
        
        return tabPanel;
    }
    
    private void closeTab(String contactName) {

        int tabIndex = -1;
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(contactName)) {
                tabIndex = i;
                break;
            }
        }
        
        if (tabIndex != -1) {
            tabbedPane.remove(tabIndex);
            chatPanels.remove(contactName);
            
            if (tabbedPane.getTabCount() == 0) {
                remove(tabbedPane);
                add(emptyStateLabel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
        }
    }
    
    private void updateTabComponents() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            String title = tabbedPane.getTitleAt(i);
            tabbedPane.setTabComponentAt(i, createTabComponent(title));
        }
    }
    
    public ChatPanel getChatPanel(String contactName) {
        return chatPanels.get(contactName);
    }
    
    private class EmptyStateIcon implements Icon {
        private final int SIZE = 64;
        
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.setColor(Design.TURQUOISE); // Используем TURQUOISE
            g2d.setStroke(new BasicStroke(2));
            
            g2d.drawRoundRect(x + 10, y + 5, SIZE - 20, SIZE - 25, 15, 15);
            
            int[] xPoints = {x + 20, x + 30, x + 25};
            int[] yPoints = {y + SIZE - 20, y + SIZE - 20, y + SIZE - 10};
            g2d.drawPolyline(xPoints, yPoints, 3);
            
            g2d.fillOval(x + 20, y + 15, 8, 8);
            g2d.fillOval(x + 35, y + 15, 8, 8);
            g2d.fillOval(x + 50, y + 15, 8, 8);
            
            g2d.dispose();
        }
        
        @Override
        public int getIconWidth() {
            return SIZE;
        }
        
        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }
}