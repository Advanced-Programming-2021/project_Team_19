package view.Menu;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.MenuControllers.DeckMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;
import view.graphic.CardView;

import java.io.IOException;

public class OneDeck extends Menu {
    @FXML
    private TextField cardName;
    @FXML
    private ScrollPane availableCards;
    @FXML
    private ScrollPane cardsInMainDeck;
    @FXML
    private ScrollPane cardsInSideDeck;
    @FXML
    private Label result;


    static OneDeck instance = null;

    private Pane pane = new Pane();

    static User user;

    static model.Deck deck;

    public static OneDeck getInstance() {
        if (instance == null) {
            instance = new OneDeck();
        }
        return instance;
    }

    public OneDeck() {
        super("One Deck");
    }

    public void initialize() {
        updateCards();
    }

    public void run(model.Deck deck, User user) {
        pane = new Pane();
        OneDeck.user = user;
        OneDeck.deck = deck;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/OneDeck.fxml"));
        try {
            pane = fxmlLoader.load();
            readyFxmlButtonsForCursor(pane);
            stage.getScene().setRoot(pane);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addToMainDeck(MouseEvent mouseEvent) {
        DataForClientFromServer data = DeckMenuController.getInstance().run(user,
                "deck add-card --card " + cardName.getText() + " --deck " + deck.getName());
        updateCards();
    }

    public void addToSideDeck(MouseEvent mouseEvent) {
        DataForClientFromServer data = DeckMenuController.getInstance().run(user,
                "deck add-card --card " + cardName.getText() + " --deck " + deck.getName() + " --side");
        updateCards();
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)){
            result.setTextFill(Color.RED);
        }
        else {
            result.setTextFill(Color.GREEN);
        }
    }

    public void removeFromMainDeck(MouseEvent mouseEvent) {
        DataForClientFromServer data = DeckMenuController.getInstance().run(user,
                "deck rm-card --card " + cardName.getText() + " --deck " + deck.getName());
        updateCards();
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)){
            result.setTextFill(Color.RED);
        }
        else {
            result.setTextFill(Color.GREEN);
        }
    }

    public void removeFromSideDeck(MouseEvent mouseEvent) {
        DataForClientFromServer data = DeckMenuController.getInstance().run(user,
                "deck rm-card --card " + cardName.getText() + " --deck " + deck.getName() + " --side");
        updateCards();
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)){
            result.setTextFill(Color.RED);
        }
        else {
            result.setTextFill(Color.GREEN);
        }
    }

    private void updateCards() {
        deck = DeckDataBaseController.getDeckByName(user.getUsername() + "_" + deck.getName());
        HBox hBox = new HBox();
        for (Card card : user.getCardsSorted()) {
            try {
                CardView cardViewToAddToScroll = new CardView(card, 2.5, false);
                hBox.getChildren().add(cardViewToAddToScroll);
                cardViewToAddToScroll.setOnMouseClicked(e -> {
                    cardName.setText(card.getName());
                });
            } catch (Exception e) {
                System.out.println(card.getName()+ "-----------------------------------------------");
            }
        }
        hBox.setSpacing(10);
        availableCards.setContent(hBox);
        HBox mainMenuCards = new HBox();
        for (Card card : deck.getAllMainCardsSorted()) {
            CardView cardView = new CardView(card, 2, false);
            cardView.setOnMouseClicked(e -> {
                this.cardName.setText(card.getName());
            });
            mainMenuCards.getChildren().add(cardView);
        }
        cardsInMainDeck.setContent(mainMenuCards);
        HBox sideMenuCards = new HBox();
        for (Card card : deck.getAllSideCardsSorted()) {
            CardView cardView = new CardView(card, 2, false);
            cardView.setOnMouseClicked(e -> {
                this.cardName.setText(card.getName());
            });
            sideMenuCards.getChildren().add(cardView);
        }
        cardsInSideDeck.setContent(sideMenuCards);
    }

    public void getBack(MouseEvent mouseEvent) {
        new Deck().run(user);
    }
}
