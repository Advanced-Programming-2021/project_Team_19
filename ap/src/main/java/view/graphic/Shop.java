package view.graphic;

import controller.DataBaseControllers.CSVDataBaseController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Card.Card;

import java.io.IOException;

public class Shop extends Application{

    @FXML
    private HBox cardPic;
    @FXML
    private TextField cardName;

    public void start (Stage primaryStage){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Shop.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            primaryStage.setScene(new Scene(anchorPane, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void getCardName(MouseEvent mouseEvent) {
        mouseEvent.getButton().name().toUppercase().lambda;
        String text = cardName.getText();
        Card card = CSVDataBaseController.getCardByCardName(text);
        CardView cardView = new CardView(card,2, false);
        if(card == null){
            System.out.println("This card does not exist");
        }
        else{
            cardPic.getChildren().clear();
            System.out.println(cardView);
            cardPic.getChildren().add(cardView);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
