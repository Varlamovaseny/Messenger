import javax.swing.*;
import java.awt.*;

public class MessengerFrame extends JFrame {
    private ContactsPanel contactsPanel;
    private ChatContainerPanel chatContainerPanel;
    private JSplitPane splitPane;
    private String username;
    private String serverIp;
    
    public MessengerFrame(String username, String serverIp) {
        this.username = username;
        this.serverIp = serverIp;
        
        setupFrame();
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void setupFrame() {
        setTitle("Мессенджер - " + username + " [" + serverIp + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        chatContainerPanel = new ChatContainerPanel();
        contactsPanel = new ContactsPanel(chatContainerPanel, serverIp, serverIp);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            contactsPanel,
            chatContainerPanel
        );
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerSize(8);
        splitPane.setBackground(Design.PRIMARY_PINK);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(splitPane, BorderLayout.CENTER);
        pack();
    }
    
    @Override
    public void dispose() {
        if (contactsPanel != null) {
            contactsPanel.disconnect();
        }
        super.dispose();
    }
}