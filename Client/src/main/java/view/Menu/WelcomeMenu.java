package view.Menu;

import AnythingIWant.ClientNetwork;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import view.Printer.Printer;
import view.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WelcomeMenu extends Menu {

    static WelcomeMenu instance = null;
    private Pane pane = new Pane();

    public static WelcomeMenu getInstance() {

        if (instance == null) {
            instance = new WelcomeMenu();
        }
        return instance;
    }

    private WelcomeMenu() {
        super("Login Menu");
    }

    public void run() {
        pane = new Pane();
        setButtons();
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
        stage.setTitle("Welcome Menu");
        stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
        stage.getScene().setRoot(pane);
    }

    public void setButtons() {

        VBox buttonBox = setSeveralChoiceButtons("signup", "login");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> Signup.run());

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> Login.run());

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> {
            ClientNetwork.getInstance().disconnect();
            System.exit(0);
        });

        pane.getChildren().addAll(buttonBox, backButton);
    }

    private static class Signup {
        private static TextField usernameField = new TextField();
        private static TextField passwordField = new TextField();
        private static TextField nicknameField = new TextField();
        private static final Label responseLabel = new Label();

        public static void run() {
            setSignupMenu();
        }

        private static void setSignupMenu() {
            HBox box = textFieldGridToEnterInfo("enter a username:",
                    "enter a password:", "enter a nickname:");
            box.setLayoutX(200);
            box.setLayoutY(100);

            usernameField = (TextField) ((VBox) box.getChildren().get(1))
                    .getChildren().get(0);
            passwordField = (TextField) ((VBox) box.getChildren().get(1))
                    .getChildren().get(1);
            nicknameField = (TextField) ((VBox) box.getChildren().get(1))
                    .getChildren().get(2);

            Button submit = new Button("submit");
            submit.setOnMouseClicked(event -> signup());
            readyCursorForButton(submit);
            ((VBox) box.getChildren().get(1)).getChildren().add(submit);

            Button backButton = new Button();
            setBackButton(backButton);
            backButton.setOnMouseClicked(event -> {
                responseLabel.setText("");
                WelcomeMenu.getInstance().run();
            });

            responseLabel.setLayoutX(200);
            responseLabel.setLayoutY(300);

            Pane pane = new Pane();
            pane.getStylesheets().add("CSS/Css.css");
            pane.setId("shopBackGround");
            pane.getChildren().addAll(box, backButton, responseLabel);

            stage.setTitle("Signup Menu");
            stage.getScene().setRoot(pane);

        }

        private static void signup() {

            String username = usernameField.getText();
            String password = passwordField.getText();
            String nickname = nicknameField.getText();

            if (username.equals("")) {
                Printer.setFailureResponseToLabel(responseLabel, "please enter a username");
                return;
            }

            if (password.equals("")) {
                Printer.setFailureResponseToLabel(responseLabel, "please enter a password");
                clearTextFields();
                return;
            }

            if (nickname.equals("")) {
                Printer.setFailureResponseToLabel(responseLabel, "please enter a nickname");
                clearTextFields();
                return;
            }

            clearTextFields();

            if (!Utils.checkFormatValidity(responseLabel, Utils.getHashMap("username", username,
                    "password", password, "nickname", nickname))) {
                return;
            }

            if (Utils.isPasswordWeak(password)) {
                Printer.setFailureResponseToLabel(responseLabel, "password is weak");
                return;
            }

            String commandToSendToServer = "user create --username " + username +
                    " --nickname " + nickname + " --password " + password;

            DataForClientFromServer data = sendDataToServer
                    (new DataForServerFromClient(commandToSendToServer, null, WelcomeMenu.getInstance().menuName));

            Printer.setAppropriateResponseToLabelFromData(data, responseLabel);
            if (data.getMessageType().equals(MessageType.SUCCESSFUL)) {
                File files = new File("src/main/resources/RandomPictures");
                ArrayList<File> allFiles = new ArrayList<>(Arrays.asList(files.listFiles()));
                Collections.shuffle(allFiles);
                try {
                    Files.copy(allFiles.get(0).toPath(),
                            new File("src/main/resources/UserProfilePicture/" + username + ".jpg").toPath());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void clearTextFields() {
            usernameField.clear();
            passwordField.clear();
            nicknameField.clear();
        }
    }

    private static class Login {
        private static TextField usernameField = new TextField();
        private static TextField passwordField = new TextField();
        private static final Label responseLabel = new Label();

        public static void run() {
            setLoginMenu();
        }

        private static void setLoginMenu() {
            HBox box = textFieldGridToEnterInfo("enter your username:",
                    "enter your password:");
            box.setLayoutX(200);
            box.setLayoutY(100);

            usernameField = (TextField) ((VBox) box.getChildren().get(1))
                    .getChildren().get(0);
            passwordField = (TextField) ((VBox) box.getChildren().get(1))
                    .getChildren().get(1);

            Button submit = new Button("submit");
            submit.setOnMouseClicked(event -> {
                responseLabel.setText("");
                login();
            });
            readyCursorForButton(submit);
            ((VBox) box.getChildren().get(1)).getChildren().add(submit);

            Button backButton = new Button();
            setBackButton(backButton);
            backButton.setOnMouseClicked(event -> {
                WelcomeMenu.getInstance().run();
            });

            responseLabel.setLayoutX(200);
            responseLabel.setLayoutY(200);

            Pane pane = new Pane();
            pane.getStylesheets().add("CSS/Css.css");
            pane.setId("shopBackGround");
            pane.getChildren().addAll(box, backButton, responseLabel);

            stage.setTitle("Login Menu");
            stage.getScene().setRoot(pane);

        }

        private static void login() {

            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals("")) {
                Printer.setFailureResponseToLabel(responseLabel, "please enter your username");
                return;
            }

            if (password.equals("")) {
                Printer.setFailureResponseToLabel(responseLabel, "please enter your password");
                clearTextFields();
                return;
            }

            clearTextFields();

            if (!Utils.checkFormatValidity(responseLabel, Utils.getHashMap("username", username,
                    "password", password))) {
                return;
            }

            String commandToSendToServer = "user login --username " + username +
                    " --password " + password;

            DataForClientFromServer data = sendDataToServer
                    (new DataForServerFromClient(commandToSendToServer, null, WelcomeMenu.getInstance().menuName));

            if (data.getMessageType().equals(MessageType.SUCCESSFUL)) {
                WelcomeMenu.token = data.getMessage().split(":")[1].trim();
                Menu.username = username;
                goToMainMenu(username);
            } else {
                Printer.setFailureResponseToLabel(responseLabel, data.getMessage());
            }
        }

        private static void clearTextFields() {
            usernameField.clear();
            passwordField.clear();
        }

        private static void goToMainMenu(String username) {
            MainMenu.getInstance(username).run();
        }
    }

}
