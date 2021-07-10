package view.Menu;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.DuelControllers.DeckModifierBetweenGames;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Card.Card;
import model.Deck;
import model.User;
import view.graphic.CardView;
import view.graphic.GameGraphicController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChangeCardBetweenRounds extends Menu {


    private  DeckModifierBetweenGames deckModifierBetweenGames;

    @FXML
    private TextField cardName;
    @FXML
    private ScrollPane cardsInMainDeck;
    @FXML
    private ScrollPane cardsInSideDeck;
    @FXML
    private Label result;
    @FXML
    private Button finishButton;


    private static int cnt;

    public static GameGraphicController gameGraphicController;

    private User user;




    public ChangeCardBetweenRounds() {
        super("ChangeCardBetweenRounds Menu");
    }

    public void run(GameGraphicController gameGraphicControllerForTest, Stage stage) {
        ChangeCardBetweenRounds.gameGraphicController = gameGraphicControllerForTest;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/ChangeCardBetweenRounds.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            readyFxmlButtonsForCursor(anchorPane);
            stage.getScene().setRoot(anchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        user = GameGraphicController.allUser.get(cnt);
        deckModifierBetweenGames = GameGraphicController.allDeckModifiers.get(cnt);
        cnt ++;
        updateCard();
    }

    private Deck getDeck() {
        return DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName());
    }

    public void updateCard() {
        Deck deck = getDeck();
        HBox mainMenuCards = new HBox();
        for (Card card : deck.getAllMainCardsSorted()) {
            CardView cardView = new CardView(card, 3, false, true);
//            cardView.setOnMouseClicked(e -> {
//                this.cardName.setText(card.getName());
//            });
            cardView.setOnDragDetected(e -> {
                Dragboard dragboard = cardView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent clipBoardContent = new ClipboardContent();
                clipBoardContent.putString(cardView.getCard().getName() + ":Main");
                dragboard.setContent(clipBoardContent);
                e.consume();
            });
            mainMenuCards.getChildren().add(cardView);
        }
        cardsInMainDeck.setContent(mainMenuCards);
        HBox sideMenuCards = new HBox();
        for (Card card : deck.getAllSideCardsSorted()) {
            CardView cardView = new CardView(card, 3, false, true);
//            cardView.setOnMouseClicked(e -> {
//                this.cardName.setText(card.getName());
//            });
            cardView.setOnDragDetected(e -> {
                Dragboard dragboard = cardView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(cardView.getCard().getName() + ":Side");
                dragboard.setContent(clipboardContent);
                e.consume();
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
                updateCard();
                return;
            }
            cnt ++;
        }
        result.setText("Sorry there is no such card in side Deck!");
        updateCard();
    }

    public void addToSideDeck(MouseEvent mouseEvent) {
        Deck deck = getDeck();
        String cardNameToMove = cardName.getText();
        int cnt = 1;
        for (Card card : deck.getAllMainCardsSorted()) {
            if (card.getName().equalsIgnoreCase(cardNameToMove)) {
                deckModifierBetweenGames.runForGraphic("--main " + cnt);
                result.setText("Successful");
                updateCard();
                return;
            }
            cnt++;
        }
        result.setText("Sorry there is no such card in side Deck!");
        updateCard();
    }

    public void finishChange(MouseEvent mouseEvent) {
        String resultFromLogic = deckModifierBetweenGames.runForGraphic("finish");
        result.setText(resultFromLogic);
        gameGraphicController.setHasSecondChoosed();
        if (result == null) {
            finishButton.setDisable(true);
        }
    }

    public void handleDraggingCardToMainDeck(DragEvent dragEvent) {
        String command = dragEvent.getDragboard().getString();
        String[] commandDetails = command.split(":");
        if (commandDetails[1].equals("Side")) {
            Deck deck = getDeck();
            String cardNameToMove = commandDetails[0];
            int cnt = 1;
            for (Card card : deck.getAllSideCardsSorted()) {
                if (card.getName().equalsIgnoreCase(cardNameToMove)) {
                    String res = deckModifierBetweenGames.runForGraphic("--main " + cnt);
                    result.setText(res);
                    updateCard();
                    return;
                }
                cnt++;
            }
            result.setText("Sorry there is no such card in side Deck!");
            updateCard();
        }
    }

    public void handleDraggingToSideDeck(DragEvent dragEvent) {
        String command = dragEvent.getDragboard().getString();
        String[] commandDetails = command.split(":");
        if (commandDetails[1].equals("Main")) {
            Deck deck = getDeck();
            String cardNameToMove = commandDetails[0];
            int cnt = 1;
            for (Card card : deck.getAllMainCardsSorted()) {
                if (card.getName().equals(cardNameToMove)) {
                    String res = deckModifierBetweenGames.runForGraphic("--main " + cnt);
                    result.setText(res);
                    updateCard();
                    return;
                }
                cnt++;
            }
            result.setText("Sorry there is no such card in side Deck!");
            updateCard();
        }
    }

    public static  void setToZero() {
        cnt = 0;
    }
}
