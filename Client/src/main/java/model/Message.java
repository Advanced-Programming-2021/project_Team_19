package model;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


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
        VBox vBox = new VBox();
        Label label1 = new Label(author);
        label1.setFont(Font.font(15));
        label1.setTextFill(Color.BLUE);
        vBox.getChildren().add(label1);
        Label label2 = new Label(message);
        label2.setWrapText(true);
        vBox.getChildren().add(label2);
        vBox.setMinWidth(300);
        vBox.setMaxWidth(300);
        vBox.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(0),
                new Insets(0,0,0,0))));
        return vBox;
    }
}
