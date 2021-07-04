package view.Menu;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Enums.RockPaperScissorResult;

import java.io.IOException;

public class RockPaper extends Application {

    @FXML
    Label choiceText;

    RockPaperScissorResult firstPlayerChoice;


    @Override
    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/RockPaper.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            primaryStage.setScene(new Scene(anchorPane));
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
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

    }

    public void chooseScissor(MouseEvent mouseEvent) {
    }

    public void chooseRock(MouseEvent mouseEvent) {
    }
}
