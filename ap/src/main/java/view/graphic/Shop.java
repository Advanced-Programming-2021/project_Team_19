package view.graphic;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.MenuControllers.ShopMenuController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import model.User;
import view.Menu.Menu;

import java.io.IOException;

public class Shop extends Menu {

    @FXML
    private HBox cardPic;
    @FXML
    private TextField cardName;
    @FXML
    private Button backButton;
    @FXML
    private Label messageBox;
    @FXML
    private ScrollPane cardsScrolling;

    private Card currentCard;

    private static User user;

    public Shop() {
        super("Shop Menu");
    }

    public void run (User user){
        Shop.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Shop.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            stage.getScene().setRoot(anchorPane);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName("Battle OX"), 2, true));
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient("shop show --all", "Taha1506", menuName));
        System.out.println(data.getMessage());
        String[] cards = data.getMessage().split("\n");
        VBox vBox = new VBox();
        for(String card : cards) {
            String tempCardName = card.split(":")[0].trim();
            Card cardToAddToScroll = controller.Utils.getCardByName(tempCardName);
            try {
                CardView cardViewToAddToScroll = new CardView(cardToAddToScroll, 2.5, false);
                vBox.getChildren().add(cardViewToAddToScroll);
            } catch (Exception e) {
                System.out.println(tempCardName + "-----------------------------------------------");
            }
        }
        cardsScrolling.setContent(vBox);
    }

    public void getCardName(MouseEvent mouseEvent) {
        String text = cardName.getText();
        Card card = controller.Utils.getCardByName(text);
        if(card == null){
            messageBox.setText("This card does not exist");
            messageBox.setTextFill(Color.GREEN);
            currentCard = null;
        }
        else{
            messageBox.setText(null);
            CardView cardView = new CardView(card,2, false);
            currentCard = card;
            cardPic.getChildren().clear();
            cardPic.getChildren().add(cardView);
            cardName.clear();
        }
    }

    public void getBack(MouseEvent mouseEvent) {
        System.out.println("Hello world");
    }

    public void clearChoice(MouseEvent mouseEvent) {
        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName("Battle OX"), 2, true));
        currentCard = null;
        cardName.clear();
        messageBox.setText(null);
    }

    public void buyCard(MouseEvent mouseEvent) {
        if(currentCard == null) {
            messageBox.setText("No card is chosen yet!");
            messageBox.setTextFill(Color.RED);
        }
        else{
            DataForClientFromServer data =
                    ShopMenuController.getInstance().run(user, "shop buy " + currentCard.getName() );
            messageBox.setText(data.getMessage());
            if (data.getMessageType().equals(MessageType.ERROR)){
                messageBox.setTextFill(Color.RED);
            }
            else{
                messageBox.setTextFill(Color.GREEN);
            }
        }
    }
}
