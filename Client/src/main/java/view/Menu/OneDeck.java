package view.Menu;

import AnythingIWant.ClientNetwork;
import controller.DataBaseControllers.CSVDataBaseController;
import controller.DataBaseControllers.DeckDataBaseController;
import controller.MenuControllers.DeckMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import model.User;
import view.graphic.CardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

    static String username;

    static String deckName;

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

    public void run(String deckName, String username) {
        Pane pane;
        OneDeck.username = username;
        OneDeck.deckName = deckName;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/OneDeck.fxml"));
        try {
            pane = fxmlLoader.load();
            readyFxmlButtonsForCursor(pane);
            stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
            stage.getScene().setRoot(pane);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addToMainDeck() {
        DataForClientFromServer data = MainMenu.sendDataToServer(new DataForServerFromClient("deck add-card --card " + cardName.getText() + " --deck " + deckName,
                token, "Deck Menu"));
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)){
            result.setTextFill(Color.RED);
        }
        else {
            result.setTextFill(Color.GREEN);
        }
        updateCards();
    }

    public void addToSideDeck() {
        DataForClientFromServer data = MainMenu.sendDataToServer(new DataForServerFromClient("deck add-card --card " + cardName.getText() + " --deck " + deckName + " --side",
                token, "Deck Menu"));
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

    public void removeFromMainDeck() {
        DataForClientFromServer data = MainMenu.sendDataToServer(new DataForServerFromClient("deck rm-card --card " + cardName.getText() + " --deck " + deckName,
                token, "Deck Menu"));
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

    public void removeFromSideDeck() {
        DataForClientFromServer data = MainMenu.sendDataToServer(new DataForServerFromClient("deck rm-card --card " + cardName.getText() + " --deck " + deckName + " --side",
                token, "Deck Menu"));
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
        HBox hBox = new HBox();
        String[] cards = MainMenu.sendDataToServer(new DataForServerFromClient("deck show --cards",
                token, "Deck Menu")).getMessage().split("\n");
        for (String cardDescription : cards) {
            try {
                CardView cardViewToAddToScroll = new CardView(cardDescription, 2.5, false, true);
                hBox.getChildren().add(cardViewToAddToScroll);
                cardViewToAddToScroll.setOnMouseClicked(e -> cardName.setText(cardDescription));
            } catch (Exception e) {
                System.out.println(cardDescription+ "-----------------------------------------------");
            }
        }
        hBox.setSpacing(10);
        availableCards.setContent(hBox);
        HBox mainMenuCards = new HBox();
        ArrayList<String> allMainCards = new ArrayList<>();
        allMainCards.addAll(Arrays.asList(Menu.sendDataToServer(new DataForServerFromClient("deck show --deck-name " + deckName,
                token, "Deck Menu")).getMessage().split("\n")));
        for (String cardIdentity : allMainCards) {
            if (!cardIdentity.equals("")) {
                CardView cardView = new CardView(cardIdentity, 2, false, true);
                cardView.setOnMouseClicked(e -> this.cardName.setText(cardIdentity));
                mainMenuCards.getChildren().add(cardView);
            }
        }
        cardsInMainDeck.setContent(mainMenuCards);
        HBox sideMenuCards = new HBox();
        ArrayList<String> allSideCards = new ArrayList<>();
        allSideCards.addAll(Arrays.asList(Menu.sendDataToServer(new DataForServerFromClient("deck show --deck-name " + deckName + " --side",
                token, "Deck Menu")).getMessage().split("\n")));
        for (String cardIdentity : allSideCards) {
            if (!cardIdentity.equals("")) {
                CardView cardView = new CardView(cardIdentity, 2, false, true);
                cardView.setOnMouseClicked(e -> this.cardName.setText(cardIdentity));
                sideMenuCards.getChildren().add(cardView);
            }
        }
        cardsInSideDeck.setContent(sideMenuCards);
    }

    public void getBack() {
        new Deck().run(username);
    }
}
