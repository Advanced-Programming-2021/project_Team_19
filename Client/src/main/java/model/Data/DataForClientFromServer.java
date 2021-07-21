package model.Data;

import model.Enums.MessageType;

import java.util.ArrayList;

public class DataForClientFromServer {

    private String message;
    private MessageType messageType;
    private ArrayList<String> messages;

    public DataForClientFromServer(String data, MessageType messageType) {
        setMessage(data);
        setMessageType(messageType);
    }

    public DataForClientFromServer(ArrayList<String> messages, MessageType messageType){
        this.messages = messages;
        setMessageType(messageType);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }
}
