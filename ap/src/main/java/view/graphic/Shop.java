package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.MenuControllers.ShopMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import view.Menu.MainMenu;
import view.Menu.Menu;

import java.io.IOException;

public class Shop extends Menu {

    @FXML
    private HBox cardPic;
    @FXML
    private TextField cardName;
    @FXML
    private Label messageBox;
    @FXML
    private ScrollPane cardsScrolling;
    @FXML
    private Label coinShower;
    @FXML
    private ScrollPane userCards;

    private Card currentCard;

    private static User user;


    public Shop() {
        super("Shop Menu");
    }


    public void run(String username) {
        Shop.username = username;
        user = UserDataBaseController.getUserByUsername(username);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Shop.fxml"));
        try {
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
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName("Battle OX"), 2, true));
        DataForClientFromServer data = sendDataToServer(new DataForServerFromClient("shop show --all", "Taha1506", menuName));
        System.out.println(data.getMessage());
        String[] cards = data.getMessage().split("\n");
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        for (String card : cards) {
            String tempCardName = card.split(":")[0].trim();
            Card cardToAddToScroll = controller.Utils.getCardByName(tempCardName);
            try {
                CardView cardViewToAddToScroll = new CardView(cardToAddToScroll, 2.5, false);
                vBox.getChildren().add(cardViewToAddToScroll);
                cardViewToAddToScroll.setOnMouseClicked(e -> {
                    cardPic.getChildren().clear();
                    cardPic.getChildren().add(new CardView(cardToAddToScroll, 2, false));
                    currentCard = cardToAddToScroll;
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
                CardView cardViewToAddToScroll = new CardView(card, 2.5, false);
                hBox.getChildren().add(cardViewToAddToScroll);
            } catch (Exception e) {
                System.out.println(card.getName() + "-----------------------------------------------");
            }
        }
        hBox.setSpacing(10);
        userCards.setContent(hBox);
    }

    public void getCardName() {
        String text = cardName.getText();
        Card card = controller.Utils.getCardByName(text);
        if (card == null) {
            messageBox.setText("This card does not exist");
            messageBox.setTextFill(Color.RED);
            messageBox.setFont(new Font(16));
            currentCard = null;
        } else {
            messageBox.setText(null);
            CardView cardView = new CardView(card, 2, false);
            currentCard = card;
            cardPic.getChildren().clear();
            cardPic.getChildren().add(cardView);
            cardName.clear();
        }
    }

    public void getBack() {
        MainMenu.getInstance().run(username);
    }

    public void clearChoice() {
        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(controller.Utils.getCardByName("Battle OX"), 2, true));
        currentCard = null;
        cardName.clear();
        messageBox.setText(null);
    }

    public void buyCard() {
        if (currentCard == null) {
            messageBox.setText("No card is chosen yet!");
            messageBox.setTextFill(Color.RED);
            messageBox.setFont(new Font(16));
        } else {
            DataForClientFromServer data =
                    ShopMenuController.getInstance().run(user, "shop buy " + currentCard.getName());
            messageBox.setText(data.getMessage());
            if (data.getMessageType().equals(MessageType.ERROR)) {
                messageBox.setTextFill(Color.RED);
                messageBox.setFont(new Font(16));
            } else {
                messageBox.setTextFill(Color.GREEN);
                coinShower.setText(String.valueOf(user.getCredit()));
                coinShower.setTextFill(Color.WHITE);
                coinShower.setFont(new Font(16));
                HBox hBox = new HBox();
                for (Card card : user.getCardsSorted()) {
                    try {
                        CardView cardViewToAddToScroll = new CardView(card, 2.5, false);
                        hBox.getChildren().add(cardViewToAddToScroll);
                    } catch (Exception e) {
                        System.out.println(card.getName() + "-----------------------------------------------");
                    }
                }
                userCards.setContent(hBox);
            }
        }
    }
}
