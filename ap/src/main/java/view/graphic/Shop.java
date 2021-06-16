package view.graphic;

import controller.DataBaseControllers.CSVDataBaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Card.Card;

import java.io.IOException;

public class Shop extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Shop.fxml"));
//        BorderPane borderPane = fxmlLoader.load();
        BorderPane borderPane = new BorderPane();
        Card card = CSVDataBaseController.getCardByCardName("Curtain of the dark ones");
        System.out.println(card.getName());
        borderPane.setCenter(CSVDataBaseController.getCardByCardName("Curtain of the dark ones"));
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
