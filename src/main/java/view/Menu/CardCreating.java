package view.Menu;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.Utils;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.scene.Scene;
import model.Card.Card;
import model.Card.Monster;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.User;
import view.graphic.CardView;

import static view.Menu.Menu.sendDataToServer;

public class CardCreating extends Menu{


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

    @FXML
    private TextArea descriptionBox;

    @FXML
    private TextField levelBox;

    @FXML
    private TextField result;

    @FXML
    private ScrollPane allCards;

    static User user;

    public CardCreating() {
        super("Card creating Menu");
    }


    public void run(User user) {
        CardCreating.user = user;
        try {
            CSVDataBaseController.load();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/CardCreating.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }


    public void initialize() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        DataForClientFromServer data =
                sendDataToServer(new DataForServerFromClient("shop show --all", user.getUsername(), menuName));
        System.out.println(data.getMessage());
        String[] cards = data.getMessage().split("\n");
        for(String card : cards) {
            String tempCardName = card.split(":")[0].trim();
            Card cardToAddToScroll = controller.Utils.getCardByName(tempCardName);
            try {
                CardView cardViewToAddToScroll = new CardView(cardToAddToScroll, 2.5, false, true);
                vBox.getChildren().add(cardViewToAddToScroll);
                cardViewToAddToScroll.setOnMouseClicked(e -> {
                    cloneBox.setText(tempCardName);
                });
            } catch (Exception e) {
                System.out.println(tempCardName + "-----------------------------------------------");
            }
        }
        allCards.setContent(vBox);
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
            String cloneCardName = cloneBox.getText();
            String description = descriptionBox.getText();
            Card card = CSVDataBaseController.getCardByCardName(cloneCardName);
            System.out.println(card);
            if (card != null && choosenPicture.getImage() != null) {
                String result;
                if (card instanceof Monster) {
                    int attack = Integer.parseInt(attackBox.getText());
                    int defense = Integer.parseInt(defenseBox.getText());
                    int level = Integer.parseInt(levelBox.getText());
                    result = CSVDataBaseController.addCard(card, cardName, attack, defense, level, description.replaceAll("\n", " "));
                    if (result.equals("Successful!")){
                        sendPictureToServer(cardName, true);
                    }
                } else {
                    result = CSVDataBaseController.addCard(card, cardName, 0, 0, 0, description.replaceAll("\n", " "));
                    if (result.equals("Successful!")) {
                        sendPictureToServer(cardName, false);
                    }
                }
                this.result.setText(result);
            } else if (card == null) {
                this.result.setText("invalid card to clone");
            } else {
                this.result.setText("no picture is selected yet!");
            }
        } catch (NumberFormatException e) {
            this.result.setText("invalid Format");
        }
    }

    private void sendPictureToServer(String name, boolean isMonster) {
        String url = choosenPicture.getImage().getUrl();;
        try {
            if (isMonster) {
                Files.copy(new File(url.substring(6)).toPath(), new File("src/main/resources/Assets/Cards/Monsters/" + Utils.getPascalCase(name) + ".jpg").toPath());
            } else {
                Files.copy(new File(url.substring(6)).toPath(), new File("src/main/resources/Assets/Cards/SpellTrap/" + Utils.getPascalCase(name) + ".jpg").toPath());
            }
        } catch(IOException e) {

            e.printStackTrace();
        }
    }



}
