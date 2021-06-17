package view.graphic;

import controller.DataBaseControllers.CSVDataBaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.Card.Card;

import java.io.IOException;

public class Shop extends menuGraphic {

    @FXML
    public Label cardNameBox;

    @FXML
    public Pane cardPic;

    public void run (){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Shop.fxml"));
        try {
            BorderPane borderPane = fxmlLoader.load();
            stage.getScene().setRoot(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getCardName(MouseEvent mouseEvent) {
        String text = cardNameBox.getText();
        Card card = CSVDataBaseController.getCardByCardName(text);
        if(card == null){
            System.out.println("This card does not exist");
        }
        else{
            cardPic.getChildren().add(new CardView(card, 2));
        }
    }
}
