package view.Menu;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.Printer.Printer;
import view.Utils;

import java.io.IOException;

import static view.Printer.Printer.setFailureResponseToLabel;

public class ProfileMenu extends Menu {

    private static ProfileMenu instance = null;
    private Pane pane = new Pane();
    private static TextField nicknameTextField = new TextField();
    private static TextField currentPasswordTextField = new TextField();
    private static TextField newPasswordTextField = new TextField();
    private static Label changeNicknameResponse = new Label();
    private static Label changePasswordResponse = new Label();

    private String nickname;

    private ProfileMenu() {
        super("Profile Menu");
    }

    public static ProfileMenu getInstance() {

        if (instance == null) {
            instance = new ProfileMenu();
        }
        return instance;

    }

    public void run(String username, String nickname) {
        pane = new Pane();
        setUsername(username);
        this.nickname = nickname;
        setButtons();
        setResponseLabels();
        stage.setTitle("Profile Menu");
        stage.getScene().setRoot(pane);

    }

    private void setResponseLabels() {
        changeNicknameResponse.setLayoutX(200);
        changeNicknameResponse.setLayoutY(300);
        changePasswordResponse.setLayoutX(200);
        changePasswordResponse.setLayoutY(300);

    }

    private void clearResponseLabels(){
        changeNicknameResponse.setText("");
        changePasswordResponse.setText("");
    }

    public void setButtons() {

        VBox buttonBox = setSeveralChoiceButtons("change nickname", "change password");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> {clearResponseLabels();
            setChangeNicknameMenu();
        });

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> {clearResponseLabels();
            setChangePasswordMenu();
        });

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        pane.getChildren().addAll(buttonBox, backButton);
        showUsernameAndNickName(pane);
    }

    public void setChangeNicknameMenu() {


        HBox changeNicknameBox = textFieldGridToEnterInfo("enter your new nickname here:");

        changeNicknameBox.setLayoutX(200);
        changeNicknameBox.setLayoutY(100);

        nicknameTextField = (TextField) ((VBox) changeNicknameBox.getChildren().get(1))
                .getChildren().get(0);

        Button submitNickname = new Button("submit");
        submitNickname.setOnMouseClicked(event -> changeNickName());
        readyCursorForButton(submitNickname);

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> run(username, nickname));

        ((VBox) changeNicknameBox.getChildren().get(1)).getChildren().add(submitNickname);

        pane = new Pane();
        pane.getChildren().addAll(changeNicknameBox, changeNicknameResponse, backButton);
        showUsernameAndNickName(pane);

        stage.getScene().setRoot(pane);

    }

    public void clearTextFields() {
        nicknameTextField.clear();
        newPasswordTextField.clear();
        currentPasswordTextField.clear();
    }

    public void changeNickName() {

        String nickname = nicknameTextField.getText();

        clearTextFields();

        if (nickname.equals("")) {
            setFailureResponseToLabel(changeNicknameResponse, "please enter a new nickname!");
            return;
        }

        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient(
                "profile change --nickname " + nickname, username, menuName));

        Printer.setAppropriateResponseToLabelFromData(data, changeNicknameResponse);

    }

    private void setChangePasswordMenu() {

        HBox changePasswordGrid = textFieldGridToEnterInfo("enter your current password here:",
                "enter your new password here:");

        currentPasswordTextField = (TextField) ((VBox) changePasswordGrid.getChildren().get(1))
                .getChildren().get(0);

        newPasswordTextField = (TextField) ((VBox) changePasswordGrid.getChildren().get(1))
                .getChildren().get(1);

        Button submit = new Button("submit");
        submit.setAlignment(Pos.CENTER_RIGHT);
        submit.setOnMouseClicked(event -> changePassword());
        readyCursorForButton(submit);

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> run(username, nickname));

        changePasswordGrid.setLayoutX(200);
        changePasswordGrid.setLayoutY(100);
        ((VBox) changePasswordGrid.getChildren().get(1)).getChildren().add(submit);

        pane = new Pane();
        pane.getChildren().addAll(changePasswordGrid, changePasswordResponse, backButton);
        showUsernameAndNickName(pane);

        stage.getScene().setRoot(pane);

    }

    private void setChangePictureMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/ChangePicture.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            stage.getScene().setRoot(anchorPane);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword() {

        String currentPassword = currentPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();

        if (currentPassword.equals("")) {
            setFailureResponseToLabel(changePasswordResponse, "enter your current password");
            return;
        }

        clearTextFields();

        if (newPassword.equals("")) {
            setFailureResponseToLabel(changePasswordResponse, "enter a new password");
            return;
        }

        if (!Utils.checkFormatValidity(changePasswordResponse,
                Utils.getHashMap(
                        "password", currentPassword, "newPassword", newPassword))) {
            return;
        }

        DataForClientFromServer data = sendDataToServer(
                new DataForServerFromClient("profile change --password" +
                        "--current " + currentPassword + " --new " + newPassword
                        , username, menuName));

        Printer.setAppropriateResponseToLabelFromData(data, changePasswordResponse);
    }

    private void showUsernameAndNickName(Pane pane){
        HBox nameBox = new HBox(20);

        Label usernameLabel = new Label("username: " + username);
        Label nicknameLabel = new Label("nickname:  " + nickname);

        nameBox.getChildren().addAll(usernameLabel, nicknameLabel);

        pane.getChildren().add(nameBox);

        nameBox.setLayoutX(stage.getScene().getWidth()/ 2);
        nameBox.setLayoutY(10);

    }


}
