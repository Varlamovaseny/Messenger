import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private MessageType type;
    private String sender;
    private String receiver;
    private String content;
    private Date timestamp;
    private byte[] fileData;
    
    public Message(MessageType type, String sender, String receiver, String content) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
        this.fileData = null;
    }
    
    // Геттеры
    public MessageType getType() { return type; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getContent() { return content; }
    public Date getTimestamp() { return timestamp; }
    public byte[] getFileData() { return fileData; }
    
    // Сеттеры
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s -> %s: %s", 
            type, sender, receiver, content);
    }
}