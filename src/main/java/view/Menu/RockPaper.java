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

import java.io.IOException;

public class RockPaper extends Application {

    @FXML
    private Label playerNumber;

    @FXML
    private Label choiceText;


    private static RockPaperScissorResult firstPlayerChoice;

    private static RockPaperScissorResult secondPlayerChoice;

    private static Stage firstPlayerStage;

    private static Stage secondPlayerStage;


    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/RockPaper.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                for (Object obj : anchorPane.getChildren()) {
                    if (obj instanceof Label) {
                        Label label = (Label) obj;
                        label.setText("First player");
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
                        label.setText("Second player");
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

    public static void main(String[] args) {
        launch(args);
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
        if (playerNumber.getText().equals("First player")) {
            firstPlayerChoice = RockPaperScissorResult.PAPER;
            firstPlayerStage.close();
        } else {
            secondPlayerChoice = RockPaperScissorResult.PAPER;
            secondPlayerStage.close();
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        }
    }

    public void chooseScissor(MouseEvent mouseEvent) {
        if (playerNumber.getText().equals("First player")) {
            firstPlayerChoice = RockPaperScissorResult.SCISSOR;
            firstPlayerStage.close();
        } else {
            secondPlayerChoice = RockPaperScissorResult.SCISSOR;
            secondPlayerStage.close();
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        }
    }

    public void chooseRock(MouseEvent mouseEvent) {
        if (playerNumber.getText().equals("First player")) {
            firstPlayerChoice = RockPaperScissorResult.ROCK;
            firstPlayerStage.close();
        } else {
            secondPlayerChoice = RockPaperScissorResult.ROCK;
            secondPlayerStage.close();
        }
        if (firstPlayerChoice != null && secondPlayerChoice != null) {
            finishGame();
        }
    }

    private void finishGame() {
        if (firstPlayerChoice.equals(secondPlayerChoice)) {
            start(new Stage());
        } else {
            if (firstPlayerChoice.equals(RockPaperScissorResult.PAPER) &&
            secondPlayerChoice.equals(RockPaperScissorResult.ROCK)) {
                System.out.println("first player is the winner");
            } else if (firstPlayerChoice.equals(RockPaperScissorResult.ROCK) &&
            secondPlayerChoice.equals(RockPaperScissorResult.SCISSOR)) {
                System.out.println("first player is the winner");
            } else if (firstPlayerChoice.equals(RockPaperScissorResult.SCISSOR) &&
            secondPlayerChoice.equals(RockPaperScissorResult.PAPER)) {
                System.out.println("first player is the winner");
            } else {
                System.out.println("second player is the winner");
            }
        }
    }
}
