import java.io.Serializable;

public enum MessageType implements Serializable {
    TEXT,               
    EMOJI,              
    IMAGE,              
    FILE,               
    PRIVATE_MESSAGE,    
    SEARCH_REQUEST,     
    SEARCH_RESULT,      
    USER_LIST,          
    STATUS_UPDATE,      
    LOGIN,              
    LOGOUT              