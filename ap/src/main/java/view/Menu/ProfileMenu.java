package view.Menu;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import view.Utils;

import static view.Printer.Printer.setFailureResponseToLabel;
import static view.Printer.Printer.setSuccessResponseToLabel;

public class ProfileMenu extends Menu {

    private static ProfileMenu instance = null;
    private Pane pane = new Pane();
    private static TextField nicknameTextField = new TextField();
    private static TextField currentPasswordTextField = new TextField();
    private static TextField newPasswordTextField = new TextField();
    private static Label changeNicknameResponse = new Label();
    private static Label changePasswordResponse = new Label();

    private ProfileMenu() {
        super("Profile Menu");
    }

    public static ProfileMenu getInstance() {

        if (instance == null) {
            instance = new ProfileMenu();
        }
        return instance;

    }

    public void run(String username) {
        pane = new Pane();
        setUsername(username);
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

    public void setButtons() {

        VBox buttonBox = setTwoChoiceButtons("change nickname", "change password");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> setChangeNicknameMenu());

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> setChangePasswordMenu());
        Button backButton = new Button();
        setBackButton(backButton);

        pane.getChildren().addAll(buttonBox, backButton);
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
        backButton.setOnMouseClicked(event -> run(username));

        ((VBox) changeNicknameBox.getChildren().get(1)).getChildren().add(submitNickname);

        pane = new Pane();
        pane.getChildren().addAll(changeNicknameBox, changeNicknameResponse, backButton);

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

        if (data.getMessageType().equals(MessageType.SUCCESSFUL)) {
            setSuccessResponseToLabel(changeNicknameResponse, data.getMessage());
        } else {
            setFailureResponseToLabel(changeNicknameResponse, data.getMessage());
        }

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
        backButton.setOnMouseClicked(event -> run(username));

        changePasswordGrid.setLayoutX(200);
        changePasswordGrid.setLayoutY(100);
        ((VBox) changePasswordGrid.getChildren().get(1)).getChildren().add(submit);

        pane = new Pane();
        pane.getChildren().addAll(changePasswordGrid, changePasswordResponse, backButton);

        stage.getScene().setRoot(pane);

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

        if (data.getMessageType().equals(MessageType.SUCCESSFUL)) {
            setSuccessResponseToLabel(changePasswordResponse, data.getMessage());
        } else {
            setFailureResponseToLabel(changePasswordResponse, data.getMessage());
        }


    }


}
