package view.Menu;

import controller.MenuControllers.ShopMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import model.User;
import view.graphic.CardView;

import javax.print.DocFlavor;
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
    @FXML
    private Label coinShower;
    @FXML
    private ScrollPane userCards;
    @FXML
    private Button buyButton;

    private Card currentCard;

    private static User user;

    public Shop() {
        super("Shop Menu");
    }

    public void run (User user){
        Shop.user = user;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/Shop.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {

        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName("Battle OX"), 2, true, true));

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
                    cardPic.getChildren().clear();
                    cardPic.getChildren().add(new CardView(cardToAddToScroll, 2, false, true));
                    currentCard = cardToAddToScroll;
                    buyButton.setDisable(user.getCredit() < cardToAddToScroll.getPrice());
                });
            } catch (Exception e) {
                System.out.println(tempCardName + "-----------------------------------------------");
            }
        }
        cardsScrolling.setContent(vBox);
        coinShower.setText(String.valueOf(user.getCredit()));
        coinShower.setTextFill(Color.WHITE);
        coinShower.setFont(new Font(16));
        HBox hBox = new HBox();
        for (Card card : user.getCardsSorted()) {
            try {
                CardView cardViewToAddToScroll = new CardView(card, 2.5, false, true);
                hBox.getChildren().add(cardViewToAddToScroll);
            } catch (Exception e) {
                System.out.println(card.getName()+ "-----------------------------------------------");
            }
        }
        hBox.setSpacing(10);
        userCards.setContent(hBox);
        buyButton.setDisable(true);
    }

    public void getCardName(MouseEvent mouseEvent) {
        String text = cardName.getText();
        Card card = controller.Utils.getCardByName(text);
        if(card == null){
            messageBox.setText("This card does not exist");
            messageBox.setTextFill(Color.RED);
            messageBox.setFont(new Font(16));
            currentCard = null;
            buyButton.setDisable(true);
        }
        else{
            messageBox.setText(null);
            CardView cardView = new CardView(card,2, false, true);
            currentCard = card;
            cardPic.getChildren().clear();
            cardPic.getChildren().add(cardView);
            if (user.getCredit() >= card.getPrice()) {
                buyButton.setDisable(false);
            }
            cardName.clear();
        }
    }

    public void getBack(MouseEvent mouseEvent) {
        backButton.setOnMouseClicked(event -> MainMenu.getInstance().run(username));
    }

    public void clearChoice(MouseEvent mouseEvent) {
        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName
                ("Battle OX"), 2, true, true));
        currentCard = null;
        buyButton.setDisable(true);
        cardName.clear();
        messageBox.setText(null);
    }

    public void buyCard(MouseEvent mouseEvent) {
        if(currentCard == null) {
            messageBox.setText("No card is chosen yet!");
            messageBox.setTextFill(Color.RED);
            messageBox.setFont(new Font(16));
        }
        else{
            DataForClientFromServer data =
                    ShopMenuController.getInstance().run(user, "shop buy " + currentCard.getName() );
            messageBox.setText(data.getMessage());
            if (data.getMessageType().equals(MessageType.ERROR)){
                messageBox.setTextFill(Color.RED);
                messageBox.setFont(new Font(16));
            }
            else{
                messageBox.setTextFill(Color.GREEN);
                coinShower.setText(String.valueOf(user.getCredit()));
                coinShower.setTextFill(Color.WHITE);
                coinShower.setFont(new Font(16));
                HBox hBox = new HBox();
                for (Card card : user.getCardsSorted()) {
                    try {
                        CardView cardViewToAddToScroll = new CardView(card, 2.5, false, true);
                        hBox.getChildren().add(cardViewToAddToScroll);
                    } catch (Exception e) {
                        System.out.println(card.getName()+ "-----------------------------------------------");
                    }
                }
                userCards.setContent(hBox);
                if (user.getCredit() < currentCard.getPrice()) {
                    buyButton.setDisable(true);
                }
            }
        }
    }
}
