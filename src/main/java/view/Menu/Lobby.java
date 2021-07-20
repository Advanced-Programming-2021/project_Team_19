package view.Menu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Lobby extends Menu{
    @FXML
    private Button oneRoundButton;
    @FXML
    private Button threeRoundButton;
    @FXML
    private Button cancelButton;


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
    }


    public void sendDataForThreeRounds(MouseEvent mouseEvent) {
        cancelButton.setDisable(false);
        oneRoundButton.setDisable(true);
        threeRoundButton.setDisable(true);
    }


    public void cancelRequest(MouseEvent mouseEvent) {
        cancelButton.setDisable(true);
        oneRoundButton.setDisable(false);
        threeRoundButton.setDisable(false);
    }
}
