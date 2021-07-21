package model;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class Message {
    public String author;
    public String message;
    public int Id;

    public Message(String author, String message, int Id){
        this.author = author;
        this.message = message;
        this.Id = Id;
    }


    public VBox getMessageForDisplay(){
        VBox toReturn = new VBox();
        Label authorLabel = new Label(author);
        Label messageLabel = new Label(message);
        toReturn.getChildren().addAll(authorLabel, messageLabel);
        return toReturn;
    }
}
