import javax.swing.*;
import java.awt.*;

public class MessengerFrame extends JFrame {
    private ContactsPanel contactsPanel;
    private ChatContainerPanel chatContainerPanel;
    private JSplitPane splitPane;
    
    public MessengerFrame() {
        setupFrame();
        initComponents();
        layoutComponents();
        
        setVisible(true);
    }
    
    private void setupFrame() {
        setTitle("Мессенджер");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        chatContainerPanel = new ChatContainerPanel();
        contactsPanel = new ContactsPanel(chatContainerPanel);
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
        splitPane.setDividerSize(5);
        splitPane.setBackground(Design.PRIMARY_BACKGROUND);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        
        add(splitPane, BorderLayout.CENTER);
        pack();
    }
    
    // Метод для обновления чата
    public void updateChatForContact(String contactName) {
        chatContainerPanel.showChatForContact(contactName);
    }
}