package view.Menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        Button submit = new Button("submit");
        submit.setOnMouseClicked(event -> onSubmitClicked());

        refresh(data);

        //todo add backbutton, submitbutton, responseLabel, textArea to pane
    }


    public void addMessage(){
        String messageText = textArea.getText();

        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("add message " + messageText, token, menuName));

        refresh(data);

    }

    public void deleteMessage(){
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("delete message " + selectedMessage.Id, token, menuName));

        if (data.getMessageType().equals(MessageType.ERROR)){
            Printer.setFailureResponseToLabel(responseLabel, data.getMessage());
        } else {
            refresh(data);
        }
    }

    public void editMessage(){
        isEditing = false;
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient
                ("edit message " + selectedMessage.Id + " " + textArea.getText(), token, menuName));

        if (data.getMessageType().equals(MessageType.ERROR)){
            Printer.setFailureResponseToLabel(responseLabel, data.getMessage());
        } else {
            refresh(data);
        }
    }

    public void readyForEdit(){
        if (username != null && username.equals(selectedMessage.author)){
            Printer.setFailureResponseToLabel(responseLabel, "you cannot edit this message!");
        }

        isEditing = true;
        textArea.setText(selectedMessage.message);
    }

    public void refresh(DataForClientFromServer data){

        for (int i = 0; i < data.getMessages().size(); i++) {
            messages.add(new Message(data.getMessages().get(i).split("~")[0],
                    data.getMessages().get(i).split("~")[1], i));
        }

        ScrollPane scrollPane = new ScrollPane();

        VBox messageBox = new VBox();

        for (Message message : messages) {
            VBox box = message.getMessageForDisplay();
            box.setOnMouseClicked(event -> {
                selectedMessage = message;
                onMessageClicked();
            });

            messageBox.getChildren().add(box);
        }

        scrollPane.setContent(messageBox);

        // todo add scroll pane to pane
        //todo start scroll pane from bottom
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

        //todo add box to pane
    }


}
