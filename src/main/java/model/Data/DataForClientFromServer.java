package model.Data;

import controller.DataFromGameRun;
import model.Enums.MessageType;

import java.util.ArrayList;

public class DataForClientFromServer {

    private String message;
    private MessageType messageType;
    public ArrayList<DataFromGameRun> gameGraphicData = null;

    public DataForClientFromServer(ArrayList<DataFromGameRun> gameGraphicData){
        this.gameGraphicData = gameGraphicData;
    }

    public DataForClientFromServer(String data, MessageType messageType) {
        setMessage(data);
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
}
