package view.Menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Enums.RockPaperScissorResult;
import model.Pair;
import model.User;

import java.io.IOException;

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

    private static Label prevChoice;

    private static Label prevResult;

    public RockPaper() {
        super("Rock Paper");
    }

    public void run(User main, User invited) {
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
                Stage stage = new Stage();
                stage.setScene(new Scene(anchorPane));
                firstPlayerStage = stage;
                stage.show();
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

    public void writePaper(MouseEvent mouseEvent) {
        choiceText.setText("Paper");
    }

    public void writeScissor(MouseEvent mouseEvent) {
        choiceText.setText("Scissor");
    }

    public void writeRock(MouseEvent mouseEvent) {
        choiceText.setText("Rock");
    }

    public void choosePaper(MouseEvent mouseEvent) {
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

    public void chooseScissor(MouseEvent mouseEvent) {
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

    public void chooseRock(MouseEvent mouseEvent) {
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
            if (firstPlayerChoice.equals(RockPaperScissorResult.PAPER) &&
            secondPlayerChoice.equals(RockPaperScissorResult.ROCK)) {
                result.setText(main.getUsername() + " is the winner");
                prevResult.setText(main.getUsername() + "is the winner");
            } else if (firstPlayerChoice.equals(RockPaperScissorResult.ROCK) &&
            secondPlayerChoice.equals(RockPaperScissorResult.SCISSOR)) {
                result.setText(main.getUsername() + " is the winner");
                prevResult.setText(main.getUsername() + "is the winner");
            } else if (firstPlayerChoice.equals(RockPaperScissorResult.SCISSOR) &&
            secondPlayerChoice.equals(RockPaperScissorResult.PAPER)) {
                result.setText(main.getUsername() + " is the winner");
                prevResult.setText(main.getUsername() + "is the winner");
            } else {
                result.setText(invited.getUsername() + " is the winner");
                prevResult.setText(invited.getUsername() + "is the winner");
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
