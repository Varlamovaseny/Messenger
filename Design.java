import java.awt.*;

public class Design {
    public static final Color PRIMARY_BACKGROUND = new Color(30, 0, 10);
    public static final Color SECONDARY_BACKGROUND = new Color(40, 5, 15);
    public static final Color ACCENT_COLOR = new Color(100, 20, 40);
    public static final Color LIGHT_BORDEAUX = new Color(140, 40, 70);
    public static final Color DARK_BORDEAUX = new Color(70, 10, 25);
    public static final Color TEXT_PRIMARY = new Color(230, 210, 210);
    public static final Color TEXT_SECONDARY = new Color(180, 160, 160);
    public static final Color TEXT_ACCENT = new Color(255, 220, 220);
    
    // Стили для кнопок
    public static void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(TEXT_ACCENT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_BORDEAUX, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Эффекты при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_BORDEAUX);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
    }
    
    // Стили для текстовых полей
    public static void styleTextField(JTextField field) {
        field.setBackground(SECONDARY_BACKGROUND);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BORDEAUX, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    // Стили для текстовых областей
    public static void styleTextArea(JTextArea area) {
        area.setBackground(SECONDARY_BACKGROUND);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
    }
    
    // Стили для панелей
    public static void stylePanel(JPanel panel) {
        panel.setBackground(PRIMARY_BACKGROUND);
    }
    
    // Стили для списков
    public static void styleList(JList<?> list) {
        list.setBackground(SECONDARY_BACKGROUND);
        list.setForeground(TEXT_PRIMARY);
        list.setSelectionBackground(LIGHT_BORDEAUX);
        list.setSelectionForeground(TEXT_ACCENT);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    // Стили для меток
    public static void styleLabel(JLabel label) {
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    // Стили для заголовков
    public static void styleHeaderLabel(JLabel label) {
        label.setForeground(TEXT_ACCENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }
    
    // Стили для полосы прокрутки
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SECONDARY_BACKGROUND);
        
        // Стилизация полосы прокрутки
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(SECONDARY_BACKGROUND);
        verticalScrollBar.setForeground(ACCENT_COLOR);
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(SECONDARY_BACKGROUND);
        horizontalScrollBar.setForeground(ACCENT_COLOR);
    }
}
