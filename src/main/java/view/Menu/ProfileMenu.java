package view.Menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.Printer.Printer;
import view.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static view.Printer.Printer.setFailureResponseToLabel;

public class ProfileMenu extends Menu {

    @FXML
    public ImageView profilePicture;
    @FXML
    public Label address;
    @FXML
    public Label result;

    public static ProfileMenu instance = null;
    public Pane pane = new Pane();
    public static TextField nicknameTextField = new TextField();
    public static TextField currentPasswordTextField = new TextField();
    public static TextField newPasswordTextField = new TextField();
    public static Label changeNicknameResponse = new Label();
    public static Label changePasswordResponse = new Label();

    public String nickname;

    public ProfileMenu() {
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
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
        setUsername(username);
        this.nickname = nickname;
        setButtons();
        setResponseLabels();
        stage.setTitle("Profile Menu");
        stage.getScene().setRoot(pane);

    }

    public void setResponseLabels() {
        changeNicknameResponse.setLayoutX(200);
        changeNicknameResponse.setLayoutY(300);
        changePasswordResponse.setLayoutX(200);
        changePasswordResponse.setLayoutY(300);

    }

    public void clearResponseLabels(){
        changeNicknameResponse.setText("");
        changePasswordResponse.setText("");
    }

    public void setButtons() {


        VBox buttonBox = setSeveralChoiceButtons("change nickname", "change password", "change Picture");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> {clearResponseLabels();
            setChangeNicknameMenu();
        });

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> {clearResponseLabels();
            setChangePasswordMenu();
        });

        buttonBox.getChildren().get(2).setOnMouseClicked(event -> {
            clearResponseLabels();
            setChangePictureMenu();
        });

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());


        Image image = new Image("UserProfilePicture/" + username + ".jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(300);
        imageView.setFitWidth(200);
        pane.getChildren().addAll(buttonBox, imageView, backButton);
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
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
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

    public void setChangePasswordMenu() {

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
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
        pane.getChildren().addAll(changePasswordGrid, changePasswordResponse, backButton);
        showUsernameAndNickName(pane);

        stage.getScene().setRoot(pane);

    }

    public void setChangePictureMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/ChangePicture.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            Button backButton = new Button();
            setBackButton(backButton);
            backButton.setOnMouseClicked(event -> run(username, nickname));
            anchorPane.getChildren().add(backButton);
            readyFxmlButtonsForCursor(anchorPane);
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

    public void showUsernameAndNickName(Pane pane){
        HBox nameBox = new HBox(20);

        Label usernameLabel = new Label("username: " + username);
        usernameLabel.setTextFill(Color.RED);
        usernameLabel.setFont(new Font(16));
        Label nicknameLabel = new Label("nickname:  " + nickname);
        nicknameLabel.setTextFill(Color.RED);
        nicknameLabel.setFont(new Font(16));

        nameBox.getChildren().addAll(usernameLabel, nicknameLabel);

        pane.getChildren().add(nameBox);

        nameBox.setLayoutX(stage.getScene().getWidth()/ 2);
        nameBox.setLayoutY(10);

    }


    public void choosePicture() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("jpg files (*.jpg)","*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image("file:///" + file.getPath());
            address.setText(file.getPath());
            profilePicture.setImage(image);
        }
    }

    public void submit() {
        String url = profilePicture.getImage().getUrl();
        if (profilePicture.getImage() != null) {
            try {
                File dir = new File("src/main/resources/UserProfilePicture");
                for (File file : dir.listFiles()) {
                    if (file.getName().equals(username + ".jpg")) {
                        file.delete();
                    }
                }
                Files.copy(new File(url.substring(6)).toPath(), new File("src/main/resources/UserProfilePicture/" + username + ".jpg").toPath());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
