package view.Menu;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import model.Message;
import view.Printer.Printer;

import java.util.ArrayList;

public class ChatMenu extends Menu{

    public TextArea textArea = new TextArea();
    public Label responseLabel = new Label();
    public Pane pane = new Pane();



    public ArrayList<Message> messages = new ArrayList<>();
    public Message selectedMessage;
    private String username;
    private boolean isEditing = false;



    public ChatMenu() {
        super("Chat Menu");
    }

    public void run(String username){

        this.username = username;

        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient("get all messages", token, menuName));

        initPane(data);
        stage.getScene().setRoot(pane);
}

    public void initPane(DataForClientFromServer data){
        responseLabel.setText("");

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        Button submit = new Button("submit");
        submit.setOnMouseClicked(event -> onSubmitClicked());

        submit.setLayoutX(690);
        submit.setLayoutY(440);

        textArea.setLayoutX(131);
        textArea.setLayoutY(300);
        textArea.prefHeight(68);
        textArea.prefWidth(538);

        responseLabel.setLayoutX(131);
        responseLabel.setLayoutY(550);
        responseLabel.prefHeight(17);
        responseLabel.prefWidth(538);

        pane.getChildren().addAll(backButton, submit, textArea, responseLabel);

        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("background1");


        refresh(data);

    }


    public void addMessage(){
        String messageText = textArea.getText();

        if (messageText.equals("")){
            Printer.setSuccessResponseToLabel(responseLabel, "please write something");
            return;
        }

        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("add message " + messageText.replace("/n", "*"), token, menuName));

        refresh(data);
    }

    public void deleteMessage(){
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("delete message " + selectedMessage.Id, token, menuName));

        if (data.getMessageType().equals(MessageType.ERROR)){
            Printer.setSuccessResponseToLabel(responseLabel, data.getMessage());
        } else {
            refresh(data);
        }
    }

    public void editMessage(){
        isEditing = false;
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("edit message " + selectedMessage.Id + " " + textArea.getText().replace("/n", "*"), token, menuName));

        if (data.getMessageType().equals(MessageType.ERROR)){
            Printer.setSuccessResponseToLabel(responseLabel, data.getMessage());
        } else {
            refresh(data);
        }
    }

    public void readyForEdit(){
        if (username != null && !username.equals(selectedMessage.author)){
            Printer.setSuccessResponseToLabel(responseLabel, "you cannot edit this message!");
            return;
        }

        isEditing = true;
        textArea.setText(selectedMessage.message);
    }

    public void refresh(DataForClientFromServer data){

        responseLabel.setText("");

        messages = new ArrayList<>();

        for (int i = 0; i < data.getMessages().size(); i++) {
            messages.add(new Message(data.getMessages().get(i).split("~")[0],
                    data.getMessages().get(i).split("~")[1].replace("*", "/n"), i));
        }

        ScrollPane scrollPane = new ScrollPane();

        VBox messageBox = new VBox(5);

        for (Message message : messages) {
            VBox box = message.getMessageForDisplay();
            box.setOnMouseClicked(event -> {
                selectedMessage = message;
                onMessageClicked();
            });
            box.minWidth(538);
            box.maxWidth(538);

            messageBox.getChildren().add(box);
        }

        messageBox.minWidth(538);
        messageBox.maxWidth(538);


        scrollPane.setContent(messageBox);

        scrollPane.setLayoutX(131);
        scrollPane.setLayoutY(38);
        scrollPane.minHeight(339);
        scrollPane.maxHeight(339);
        scrollPane.minWidth(538);
        scrollPane.maxWidth(538);

        pane.getChildren().removeIf(child -> child instanceof ScrollPane);

        pane.getChildren().add(scrollPane);

        textArea.clear();

    }

    private void onSubmitClicked(){
        if (isEditing){
            editMessage();
        } else {
            addMessage();
        }
    }

    public void onMessageClicked(){

        HBox buttonBox = new HBox(5);

        buttonBox.setLayoutX(300);

        Button delete = new Button("delete");
        Button edit = new Button("edit");

        buttonBox.getChildren().addAll(edit, delete);

        delete.setOnMouseClicked(event -> {
            pane.getChildren().remove(buttonBox);
            deleteMessage();
        });

        edit.setOnMouseClicked(event -> {
            pane.getChildren().remove(buttonBox);
            readyForEdit();
        });

        pane.getChildren().add(buttonBox);
    }


}
