package view.graphic;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.MenuControllers.DeckMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;
import view.Menu.Menu;

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


    public Deck() {
        super("Deck Menu");
    }

    private static User user;

    public void run (User user){
        Deck.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Deck.fxml"));
        try {
            AnchorPane anchorPane = fxmlLoader.load();
            stage.getScene().setRoot(anchorPane);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        updateDeckScroll();
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
            VBox vBox = new VBox();
            int cnt = 0;
            for(Card card : deck.getAllCardsSorted()) {
                if(cnt < 5) {
                    vBox.getChildren().add(new CardView(card, 3, false));
                    cnt++;
                }
            }
            deckFullDescription.setCenter(vBox);
            deckFullDescription.setTop(new Label(deck.getName()));
            allDecks.getChildren().add(deckFullDescription);
        }
        deckBar.setContent(allDecks);
    }
}
