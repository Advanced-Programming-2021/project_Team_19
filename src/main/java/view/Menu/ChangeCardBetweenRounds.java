package view.Menu;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.DuelControllers.DeckModifierBetweenGames;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Card.Card;
import model.Deck;
import model.User;
import view.graphic.CardView;

import java.io.IOException;

public class ChangeCardBetweenRounds extends Menu {

    private User user;

    private DeckModifierBetweenGames deckModifierBetweenGames;

    @FXML
    private TextField cardName;
    @FXML
    private ScrollPane cardsInMainDeck;
    @FXML
    private ScrollPane cardsInSideDeck;
    @FXML
    private Label result;


    public ChangeCardBetweenRounds() {
        super("ChangeCardBetweenRounds Menu");
    }

    public void run(User user) {
        this.user = user;
        deckModifierBetweenGames = new DeckModifierBetweenGames(user);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/OneDeck.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        updateCard();
    }

    private Deck getDeck() {
        return DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName());
    }

    public void updateCard() {
        Deck deck = getDeck();
        HBox mainMenuCards = new HBox();
        for (Card card : deck.getAllMainCardsSorted()) {
            CardView cardView = new CardView(card, 2, false, true);
            cardView.setOnMouseClicked(e -> {
                this.cardName.setText(card.getName());
            });
            mainMenuCards.getChildren().add(cardView);
        }
        cardsInMainDeck.setContent(mainMenuCards);
        HBox sideMenuCards = new HBox();
        for (Card card : deck.getAllSideCardsSorted()) {
            CardView cardView = new CardView(card, 2, false, true);
            cardView.setOnMouseClicked(e -> {
                this.cardName.setText(card.getName());
            });
            sideMenuCards.getChildren().add(cardView);
        }
        cardsInSideDeck.setContent(sideMenuCards);
    }

    public void addToMainDeck(MouseEvent mouseEvent) {
        Deck deck = getDeck();
        String cardNameToMove = cardName.getText();
        int cnt = 1;
        for (Card card : deck.getAllSideCardsSorted()) {
            if (card.getName().equals(cardNameToMove)) {
                deckModifierBetweenGames.runForGraphic("--side " + cnt);
                result.setText("Successful");
                return;
            }
        }
        result.setText("Sorry there is no such card in side Deck!");
    }

    public void addToSideDeck(MouseEvent mouseEvent) {
        Deck deck = getDeck();
        String cardNameToMove = cardName.getText();
        int cnt = 1;
        for (Card card : deck.getAllMainCardsSorted()) {
            if (card.getName().equals(cardNameToMove)) {
                deckModifierBetweenGames.runForGraphic("--main " + cnt);
                result.setText("Successful");
                return;
            }
            cnt++;
        }
        result.setText("Sorry there is no such card in side Deck!");
    }

    public void finishChange(MouseEvent mouseEvent) {
        String resultFromLogic = deckModifierBetweenGames.runForGraphic("finish");
        result.setText(resultFromLogic);
    }
}
