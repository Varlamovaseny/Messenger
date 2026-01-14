import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Design {
    public static final Color PRIMARY_PINK = new Color(255, 182, 193); // Светло-розовый
    public static final Color SECONDARY_PINK = new Color(255, 200, 210); // Более светлый розовый
    public static final Color DARK_PINK = new Color(230, 150, 160); // Темно-розовый
    public static final Color LIGHT_PINK = new Color(255, 220, 225); // Очень светлый розовый
    
    public static final Color TURQUOISE = new Color(64, 224, 208); // Бирюзовый
    public static final Color SHINY_TURQUOISE = new Color(0, 245, 212); // Блестящий бирюзовый
    public static final Color DARK_TURQUOISE = new Color(0, 206, 180); // Темный бирюзовый
    public static final Color LIGHT_TURQUOISE = new Color(175, 238, 238); // Светлый бирюзовый
    
    public static final Color TEXT_PRIMARY = new Color(60, 60, 60); // Темно-серый
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100); // Серый
    public static final Color TEXT_ACCENT = new Color(40, 40, 40); // Очень темный
    public static final Color TEXT_ON_TURQUOISE = Color.WHITE; // Белый текст на бирюзовом
    
    public static void styleButton(JButton button) {
        button.setBackground(TURQUOISE);
        button.setForeground(TEXT_ON_TURQUOISE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_TURQUOISE, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(SHINY_TURQUOISE);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(TURQUOISE);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    public static void styleTextField(JTextField field) {
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TURQUOISE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_TURQUOISE, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    public static void styleTextArea(JTextArea area) {
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TURQUOISE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    public static void stylePanel(JPanel panel) {
        panel.setBackground(PRIMARY_PINK);
    }
    
    public static void styleList(JList<?> list) {
        list.setBackground(LIGHT_PINK);
        list.setForeground(TEXT_PRIMARY);
        list.setSelectionBackground(TURQUOISE);
        list.setSelectionForeground(TEXT_ON_TURQUOISE);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        list.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    public static void styleLabel(JLabel label) {
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
    
    public static void styleHeaderLabel(JLabel label) {
        label.setForeground(TEXT_ACCENT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
    }
    
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(LIGHT_TURQUOISE, 2));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(LIGHT_PINK);
        verticalScrollBar.setForeground(TURQUOISE);
        
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setBackground(LIGHT_PINK);
        horizontalScrollBar.setForeground(TURQUOISE);
    }
}