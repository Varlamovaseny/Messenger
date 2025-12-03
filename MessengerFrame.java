import javax.swing.*;
import java.awt.*;

public class MessengerFrame extends JFrame {
    private ChatPanel chatPanel;
    private ContactsPanel contactsPanel;
    
    public MessengerFrame() {
        setupFrame();
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void setupFrame() {
        setTitle("Бордовый Мессенджер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        chatPanel = new ChatPanel();
        contactsPanel = new ContactsPanel(chatPanel);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            contactsPanel,
            chatPanel
        );
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);
        
        add(splitPane, BorderLayout.CENTER);
        pack();
    }
}