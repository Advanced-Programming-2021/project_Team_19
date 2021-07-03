package view.Menu;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.MenuControllers.DeckMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;
import view.graphic.CardView;

import java.io.IOException;

public class Deck extends Menu {


    @FXML
    private ScrollPane deckBar;
    @FXML
    private Button deckCreateButton;
    @FXML
    private Button deckRemoveButton;
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

    private static User user;

    public void run (User user){
        Deck.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/Deck.fxml"));
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
        updateDeckScroll();

        activeDeckName.setText(user.getActiveDeckName());
    }

    public void createDeck() {
        String deckName = deckNameTextField.getText();
        DataForClientFromServer data = DeckMenuController.getInstance().run(user, "deck create " + deckName);
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
        DataForClientFromServer data = DeckMenuController.getInstance().run(user, "deck delete " + deckName);
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
        for (String deckName : user.getDeckNames()) {
            model.Deck deck = DeckDataBaseController.getDeckByName(user.getUsername() + "_" + deckName);
            BorderPane deckFullDescription = new BorderPane();
            HBox hBox = new HBox();
            int cnt = 0;
            for(Card card : deck.getAllCardsSorted()) {
                if(cnt < 5) {
                    hBox.getChildren().add(new CardView(card, 4, false));
                    cnt++;
                }
            }
            hBox.setSpacing(-60);
            deckFullDescription.setCenter(hBox);
            Label currentDeckName = new Label(deck.getName() + " :");
            currentDeckName.setFont(new Font(16));
            deckFullDescription.setTop(currentDeckName);
            hBox.setMinWidth(150);
            hBox.setMinHeight(150);
            hBox.setOnMouseClicked(e -> {
                OneDeck.getInstance().run(deck, user);
            });
            allDecks.getChildren().add(deckFullDescription);
        }
        allDecks.setSpacing(10);
        deckBar.setContent(allDecks);
    }

    public void setAsActiveDeck() {
        String activeDeckText = deckNameTextField.getText();
        DataForClientFromServer data = DeckMenuController.getInstance().run(user, "deck set-active " + activeDeckText);
        result.setText(data.getMessage());
        result.setFont(new Font(16));
        if (data.getMessageType().equals(MessageType.ERROR)) {
            result.setTextFill(Color.RED);
        }
        else{
            result.setTextFill(Color.GREEN);
            activeDeckName.setText(user.getActiveDeckName());
            updateDeckScroll();
        }
    }

    public void getBack(MouseEvent mouseEvent) {
        backButton.setOnMouseClicked(event -> MainMenu.getInstance().run(user.getUsername()));
    }
}
