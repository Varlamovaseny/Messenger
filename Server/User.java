import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String username;
    private String ipAddress;
    private int port;
    private boolean online;
    private Date lastSeen;
    
    public User(String username, String ipAddress, int port) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
        this.online = true;
        this.lastSeen = new Date();
    }
    
    public String getUsername() { return username; }
    public String getIpAddress() { return ipAddress; }
    public int getPort() { return port; }
    public boolean isOnline() { return online; }
    public Date getLastSeen() { return lastSeen; }
    
    public void setOnline(boolean online) { 
        this.online = online; 
        this.lastSeen = new Date();
    }
    
    public void updateLastSeen() {
        this.lastSeen = new Date();
    }
    
    @Override
    public String toString() {
        return username + " (" + (online ? "online" : "offline") + ")";
    }
}