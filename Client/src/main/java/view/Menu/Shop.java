package view.Menu;

import AnythingIWant.ClientNetwork;
import controller.DataBaseControllers.CSVDataBaseController;
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
import view.graphic.CardView;

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

    private static String username;

    public Shop() {
        super("Shop Menu");
    }

    public void run (String username){
        Shop.username = username;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/Shop.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
            stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
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
                sendDataToServer(new DataForServerFromClient("shop show --all", token, menuName));
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
                    buyButton.setDisable(getCredit() < cardToAddToScroll.getPrice());
                });
            } catch (Exception e) {
                System.out.println(tempCardName + "-----------------------------------------------");
            }
        }
        cardsScrolling.setContent(vBox);
        coinShower.setText(String.valueOf(getCredit()));
        coinShower.setTextFill(Color.WHITE);
        coinShower.setFont(new Font(16));
        HBox hBox = new HBox();
        for (String cardName : getCardsSorted()) {
            Card card = CSVDataBaseController.getCardByCardName(cardName);
            try {
                CardView cardViewToAddToScroll = new CardView(card, 2.5, false, true);
                hBox.getChildren().add(cardViewToAddToScroll);
            } catch (Exception e) {
                System.out.println(cardName+ "-----------------------------------------------");
            }
        }
        hBox.setSpacing(10);
        userCards.setContent(hBox);
        buyButton.setDisable(true);
    }

    public void getCardName(MouseEvent mouseEvent) {
        String cardName = this.cardName.getText();
        Card card = controller.Utils.getCardByName(cardName);
        if(card == null){
            messageBox.setText("This card does not exist");
            messageBox.setTextFill(Color.RED);
            messageBox.setFont(new Font(16));
            currentCard = null;
            buyButton.setDisable(true);
        }
        else{
            messageBox.setText(null);
            CardView cardView = new CardView(cardName,2, false, true);
            currentCard = card;
            cardPic.getChildren().clear();
            cardPic.getChildren().add(cardView);
            if (getCredit() >= card.getPrice()) {
                buyButton.setDisable(false);
            }
            this.cardName.clear();
        }
    }

    public void getBack(MouseEvent mouseEvent) {
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());
    }

    public void clearChoice(MouseEvent mouseEvent) {
        cardPic.getChildren().clear();
        cardPic.getChildren().add(new CardView(2));
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
                    Menu.sendDataToServer(new DataForServerFromClient("shop buy " + currentCard.getName() , token, "Shop Menu" ));
            messageBox.setText(data.getMessage());
            if (data.getMessageType().equals(MessageType.ERROR)){
                messageBox.setTextFill(Color.RED);
                messageBox.setFont(new Font(16));
            }
            else{
                messageBox.setTextFill(Color.GREEN);
                coinShower.setText(String.valueOf(getCredit()));
                coinShower.setTextFill(Color.WHITE);
                coinShower.setFont(new Font(16));
                HBox hBox = new HBox();
                for (String cardName : getCardsSorted()) {
                    try {
                        CardView cardViewToAddToScroll = new CardView(cardName, 2.5, false, true);
                        hBox.getChildren().add(cardViewToAddToScroll);
                    } catch (Exception e) {
                        System.out.println(cardName+ "-----------------------------------------------");
                    }
                }
                userCards.setContent(hBox);
                if (getCredit() < currentCard.getPrice()) {
                    buyButton.setDisable(true);
                }
            }
        }
    }

    private int getCredit() {
        DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("shop show --money", token, "Shop Menu"));
        return Integer.parseInt(data.getMessage());
    }

    private String[] getCardsSorted() {
        DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("deck show --cards", token, "Deck Menu"));
        return data.getMessage().split("\n");
    }


}
