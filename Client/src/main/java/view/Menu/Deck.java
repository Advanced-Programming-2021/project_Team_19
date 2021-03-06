package view.Menu;

import AnythingIWant.ClientNetwork;
import controller.DataBaseControllers.CSVDataBaseController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
import java.util.ArrayList;
import java.util.Arrays;

public class Deck extends Menu {


    @FXML
    private ScrollPane deckBar;
    @FXML
    private TextField deckNameTextField;
    @FXML
    private Label result;
    @FXML
    private Label activeDeckName;
    @FXML
    private Button backButton;


    public Deck() {
        super("Deck Menu");
    }

    private static String username;

    public void run (String username){
        Deck.username = username;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/Deck.fxml"));
        try {
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
        updateDeckScroll();
        DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("deck show --active", token, "Deck Menu"));
        activeDeckName.setText(data.getMessage());
    }

    public void createDeck() {
        String deckName = deckNameTextField.getText();
        DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("deck create " + deckName,
                token, "Deck Menu"));
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)) {
            result.setTextFill(Color.RED);
        }
        else{
            result.setTextFill(Color.GREEN);
            updateDeckScroll();
        }
    }

    public void removeDeck() {
        String deckName = deckNameTextField.getText();
        DataForClientFromServer data = Menu.sendDataToServer(new DataForServerFromClient("deck delete " + deckName, token, "Deck Menu"));
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)) {
            result.setTextFill(Color.RED);
        }
        else{
            result.setTextFill(Color.GREEN);
            updateDeckScroll();
        }
    }

    private void updateDeckScroll() {
        VBox allDecks = new VBox();
        String[] deckNames = Menu.sendDataToServer(new DataForServerFromClient("deck show --all",
                token, "Deck Menu")).getMessage().split("\n");
        for (String deckName : deckNames) {
            ArrayList<String> allCards = new ArrayList<>();
            allCards.addAll(Arrays.asList(Menu.sendDataToServer(new DataForServerFromClient("deck show --deck-name " + deckName,
                    token, "Deck Menu")).getMessage().split("\n")));
            allCards.addAll(Arrays.asList(Menu.sendDataToServer(new DataForServerFromClient("deck show --deck-name " + deckName + " --side",
                    token, "Deck Menu")).getMessage().split("\n")));
            BorderPane deckFullDescription = new BorderPane();
            HBox hBox = new HBox();
            int cnt = 0;
            for (String cardName : allCards) {
                Card card = CSVDataBaseController.getCardByCardName(cardName);
                if (cnt < 5 && card != null) {
                    hBox.getChildren().add(new CardView(card, 4, false, true));
                    cnt++;
                }
            }
            hBox.setSpacing(-60);
            deckFullDescription.setCenter(hBox);
            Label currentDeckName = new Label(deckName + " :");
            currentDeckName.setFont(new Font(16));
            deckFullDescription.setTop(currentDeckName);
            hBox.setMinWidth(150);
            hBox.setMinHeight(150);
            hBox.setOnMouseClicked(e -> OneDeck.getInstance().run(deckName, username));
            allDecks.getChildren().add(deckFullDescription);
        }
        allDecks.setSpacing(10);
        deckBar.setContent(allDecks);
    }

    public void setAsActiveDeck() {
        String activeDeckText = deckNameTextField.getText();
        DataForClientFromServer data = MainMenu.sendDataToServer(new DataForServerFromClient("deck set-active " + activeDeckText,
                token, "Deck Menu"));
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)) {
            result.setTextFill(Color.RED);
        }
        else{
            result.setTextFill(Color.GREEN);
            DataForClientFromServer data1 = Menu.sendDataToServer(new DataForServerFromClient("deck show --active", token, "Deck Menu"));
            activeDeckName.setText(data1.getMessage());
            updateDeckScroll();
        }
    }

    public void getBack() {
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());
    }
}
