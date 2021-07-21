package view.Menu;

import AnythingIWant.LobbyHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.graphic.GameGraphicController;

import java.io.IOException;

public class Lobby extends Menu{
    @FXML
    private Button oneRoundButton;
    @FXML
    private Button threeRoundButton;
    @FXML
    private Button cancelButton;

    private boolean isCancelButtonPressed;


    public Lobby() {
        super("Lobby");
    }

    public void run() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/Lobby.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        cancelButton.setDisable(true);
    }

    public void sendDataForOneRound(MouseEvent mouseEvent) {
        cancelButton.setDisable(false);
        oneRoundButton.setDisable(true);
        threeRoundButton.setDisable(true);
        new Thread(() -> {
            DataForClientFromServer data = Menu.sendDataToServer
                    (new DataForServerFromClient("lobby --play --rounds " + 1,
                    token, "Lobby Menu"));

                while (!isCancelButtonPressed) {

                    if (data.getMessage().startsWith("match started")) {
                        System.out.println("match started");
                        break;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    data = Menu.sendDataToServer(new DataForServerFromClient
                            ("lobby --getStatus", token, "Lobby Menu"));
                }

            if (isCancelButtonPressed) {
                Menu.sendDataToServer(new DataForServerFromClient("lobby exit", token, "Lobby Menu"));
            } else{
                DataForClientFromServer finalData = data;
                Platform.runLater(() -> {
                    cancelButton.setDisable(true);
                    oneRoundButton.setDisable(false);
                    threeRoundButton.setDisable(false);
                    String gameCode = finalData.getMessage().split(":")[1];
                    String username1 = finalData.getMessage().split(":")[2];
                    String username2 = finalData.getMessage().split(":")[3];
                    new GameGraphicController(gameCode, 1, username1, username2).run();
//                    new RockPaper().run();
                });
            }
        }).start();

    }


    public void sendDataForThreeRounds(MouseEvent mouseEvent) {
        isCancelButtonPressed = false;
        cancelButton.setDisable(false);
        oneRoundButton.setDisable(true);
        threeRoundButton.setDisable(true);
        new Thread(() -> {
            DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("lobby --play --rounds " + 3,
                    token, "Lobby Menu"));
            if (data.getMessage().startsWith("match started")) {
                System.out.println("match started");
            } else {
                while (!isCancelButtonPressed) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    data = Menu.sendDataToServer(new DataForServerFromClient("lobby --getStatus", token, "Lobby Menu"));
                    if (data.getMessage().startsWith("match started")) {
                        System.out.println("match started");
                        break;
                    }
                }

            }
            if (isCancelButtonPressed) {
                Menu.sendDataToServer(new DataForServerFromClient("lobby exit", token, "Lobby Menu"));
            } else{
                Platform.runLater(() -> {
                    cancelButton.setDisable(true);
                    oneRoundButton.setDisable(false);
                    threeRoundButton.setDisable(false);
                    new RockPaper().run();
                });
            }
        }).start();
    }


    public void cancelRequest(MouseEvent mouseEvent) {
        isCancelButtonPressed = true;
        cancelButton.setDisable(true);
        oneRoundButton.setDisable(false);
        threeRoundButton.setDisable(false);
    }

    public void getBack(MouseEvent mouseEvent) {
        cancelRequest(mouseEvent);
        MainMenu.getInstance(username).run();
    }
}
