package view.Menu;

import controller.DataBaseControllers.CSVDataBaseController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import javafx.scene.Scene;
import model.Card.Card;

public class CardCreating extends Application{


    @FXML
    public ImageView choosenPicture;

    @FXML
    private TextField imageURL;

    @FXML
    private TextField nameBox;

    @FXML
    private TextField attackBox;

    @FXML
    private TextField defenseBox;

    @FXML
    private TextField cloneBox;

    private static Stage stage;

    public void start(Stage primaryStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/CardCreating.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            primaryStage.setScene(new Scene(anchorPane));
            stage = primaryStage;
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
        launch(args);
    }

    public void choosePicture(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("jpg files (*.jpg)","*.jpg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Image image = new Image("file:///" + file.getPath());
            imageURL.setText(file.getPath());
            choosenPicture.setImage(image);
        }
    }

    public void submit(MouseEvent mouseEvent) {
        try {
            String cardName = nameBox.getText();
            int attack = Integer.parseInt(attackBox.getText());
            int defense = Integer.parseInt(defenseBox.getText());
            String cloneCardName = cloneBox.getText();
            Card card = CSVDataBaseController.getCardByCardName(cloneCardName);
            if (card != null) {
                CSVDataBaseController.addCard(card, cardName);
            }
        } catch (NumberFormatException e) {
            System.out.println("invalid Format");
        }
    }
}
