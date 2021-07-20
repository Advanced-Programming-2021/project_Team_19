package view.Menu;

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

    private static User main;

    private static User invited;


    private static RockPaperScissorResult firstPlayerChoice;

    private static RockPaperScissorResult secondPlayerChoice;

    private static Stage firstPlayerStage;

    private static Stage secondPlayerStage;

    private static Label prevChoice = new Label();

    private static Label prevResult = new Label();

    private static DuelMenuController duelMenuController;

    private BlockingQueue<Boolean> getResult = new LinkedBlockingQueue<>();

    public RockPaper() {
        super("Rock Paper");
    }

    public void run(User main, User invited, Stage before, DuelMenuController duelMenuController) {
        firstPlayerChoice = null;
        secondPlayerChoice = null;
        RockPaper.duelMenuController = duelMenuController;
        RockPaper.main = main;
        RockPaper.invited = invited;
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/RockPaper.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                for (Object obj : anchorPane.getChildren()) {
                    if (obj instanceof Label) {
                        Label label = (Label) obj;
                        if (label.getText().equals("Player")) {
                            label.setText(main.getUsername());
                        }
                    }
                }
                before.getScene().setRoot(anchorPane);
                firstPlayerStage = stage;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/RockPaper.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                for (Object obj : anchorPane.getChildren()) {
                    if (obj instanceof Label) {
                        Label label = (Label) obj;
                        if (label.getText().equals("Player")) {
                            label.setText(invited.getUsername());
                        }
                    }
                }
                Stage stage = new Stage();
                stage.setScene(new Scene(anchorPane));
                secondPlayerStage = stage;
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
        if (playerNumber.getText().equals(main.getUsername()) && firstPlayerChoice == null) {
            firstPlayerChoice = RockPaperScissorResult.PAPER;
            choice.setText("You choosed Paper Please wait for the other" +
                    "player to choose too");
        } else if (secondPlayerChoice == null){
            secondPlayerChoice = RockPaperScissorResult.PAPER;
            choice.setText("You choosed Paper Please wait for the other" +
                    "player to choose too");
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        } else {
            prevChoice = choice;
            prevResult = result;
        }
    }

    public void chooseScissor() {
        if (playerNumber.getText().equals(main.getUsername()) && firstPlayerChoice == null) {
            firstPlayerChoice = RockPaperScissorResult.SCISSOR;
            choice.setText("You choosed Scissor Please wait for the other" +
                    "player to choose too");
        } else if (secondPlayerChoice == null) {
            secondPlayerChoice = RockPaperScissorResult.SCISSOR;
            choice.setText("You choosed Scissor Please wait for the other" +
                    "player to choose too");
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        } else {
            prevChoice = choice;
            prevResult = result;
        }
    }

    public void chooseRock() {
        if (playerNumber.getText().equals(main.getUsername()) && firstPlayerChoice == null) {
            firstPlayerChoice = RockPaperScissorResult.ROCK;
            choice.setText("You choosed Rock Please wait for the other" +
                    "player to choose too");
        } else if (secondPlayerChoice == null){
            secondPlayerChoice = RockPaperScissorResult.ROCK;
            choice.setText("You choosed Rock Please wait for the other" +
                    "player to choose too");
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        } else {
            prevChoice = choice;
            prevResult = result;
        }
    }

    private void finishGame() {
        if (firstPlayerChoice.equals(secondPlayerChoice)) {
            firstPlayerChoice = null;
            secondPlayerChoice = null;
            result.setText("Tie please try again!");
            prevResult.setText("Tie please try again!");
            prevChoice.setText(null);
            choice.setText(null);

        } else {
            try {
                if (firstPlayerChoice.equals(RockPaperScissorResult.PAPER) &&
                        secondPlayerChoice.equals(RockPaperScissorResult.ROCK)) {
                    showWinResult();
                } else if (firstPlayerChoice.equals(RockPaperScissorResult.ROCK) &&
                        secondPlayerChoice.equals(RockPaperScissorResult.SCISSOR)) {
                    showWinResult();
                } else if (firstPlayerChoice.equals(RockPaperScissorResult.SCISSOR) &&
                        secondPlayerChoice.equals(RockPaperScissorResult.PAPER)) {
                    showWinResult();
                } else {
                    result.setText(invited.getUsername() + " is the winner");
                    prevResult.setText(invited.getUsername() + "is the winner");
                    DuelMenuController.rivalGamer = new Gamer(DuelMenuController.user);
                    DuelMenuController.gameStarter = new Gamer(UserDataBaseController.getUserByUsername(duelMenuController.rivalUserNameTextField.getText()));
                    Thread.sleep(1000);
                    duelMenuController.handleDuel(Integer.parseInt(((String) duelMenuController.numberOfRounds.getSelectionModel().getSelectedItem()).substring(0, 1)),
                            firstPlayerStage, secondPlayerStage, true);
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showWinResult() throws InterruptedException {
        result.setText(main.getUsername() + " is the winner");
        prevResult.setText(main.getUsername() + "is the winner");
        DuelMenuController.gameStarter = new Gamer(DuelMenuController.user);
        DuelMenuController.rivalGamer = new Gamer(UserDataBaseController.getUserByUsername(duelMenuController.rivalUserNameTextField.getText()));
        Thread.sleep(1000);
        duelMenuController.handleDuel(Integer.parseInt(((String) duelMenuController.numberOfRounds.getSelectionModel().getSelectedItem()).substring(0, 1)),
                firstPlayerStage, secondPlayerStage, false);
    }

    public Pair<Pair<Stage, Stage>, Boolean> getResult2() {
        try {
            Boolean tempResult = getResult.take();
            return new Pair<>(new Pair<>(firstPlayerStage, secondPlayerStage), tempResult);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
