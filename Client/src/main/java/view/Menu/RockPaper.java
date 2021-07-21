package view.Menu;

import AnythingIWant.ClientNetwork;
import controller.DataBaseControllers.UserDataBaseController;
import controller.DuelControllers.DuelMenuController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Enums.RockPaperScissorResult;
import model.Gamer;
import model.Pair;
import model.User;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RockPaper extends Menu {

    @FXML
    private Label playerNumber;

    @FXML
    private Label choiceText;

    @FXML
    private Label result;

    @FXML
    private Label choice;





    public RockPaper() {
        super("Rock Paper");
    }

    public void run() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/RockPaper.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            stage.getScene().setRoot(anchorPane);
            stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePaper() {
        choiceText.setText("Paper");
    }

    public void writeScissor() {
        choiceText.setText("Scissor");
    }

    public void writeRock() {
        choiceText.setText("Rock");
    }

    public void choosePaper() {
        choice.setText("You choosed Paper Please wait for the other" +
                "player to choose too");
    }

    public void chooseScissor() {
        choice.setText("You choosed Scissor Please wait for the other" +
                "player to choose too");
    }

    public void chooseRock() {
        choice.setText("You choosed Rock Please wait for the other" +
                "player to choose too");
    }


}
