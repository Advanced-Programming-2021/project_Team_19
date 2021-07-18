package view.Menu;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.User;
import view.graphic.CardView;

import java.io.File;
import java.io.IOException;

public class ImportAndExportMenu extends Menu {

    @FXML
    public ScrollPane allCards;
    @FXML
    public HBox importedCard;
    @FXML
    public TextField cardToExport;
    @FXML
    public Label result;

    public static ImportAndExportMenu instance = null;
    public Pane pane = new Pane();
    static User user;

    public ImportAndExportMenu() {
        super("Import/Export Menu");
    }

    public static ImportAndExportMenu getInstance() {
        if (instance == null) {
            instance = new ImportAndExportMenu();
        }
        return instance;
    }


    public void run(User user) {
        ImportAndExportMenu.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../graphic/fxml/importAndExportMenu.fxml"));
        try {
            pane = fxmlLoader.load();
            readyFxmlButtonsForCursor(pane);
        } catch(IOException e) {
            e.printStackTrace();
        }
        stage.getScene().setRoot(pane);
    }

    public void initialize() {
        updateCards();
    }

    public void updateCards() {
        VBox vBox = new VBox();
        DataForClientFromServer data =
                sendDataToServer(new DataForServerFromClient("shop show --all", user.getUsername(), "Shop Menu"));
        System.out.println(data.getMessage());
        String[] cards = data.getMessage().split("\n");
        for(String card : cards) {
            String tempCardName = card.split(":")[0].trim();
            Card cardToAddToScroll = CSVDataBaseController.getCardByCardName(tempCardName);
            try {
                CardView cardViewToAddToScroll = new CardView(cardToAddToScroll, 2.5, false, true);
                vBox.getChildren().add(cardViewToAddToScroll);
                cardViewToAddToScroll.setOnMouseClicked(e -> cardToExport.setText(tempCardName));
            } catch (Exception e) {
                System.out.println(tempCardName + "-----------------------------------------------");
            }
        }
        allCards.setContent(vBox);
    }

    public void chooseCardJson() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("json files (*.json)","*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            Card card = (Card) DataBaseController.getObjectByGson(DataBaseController.readDataFromFile(file.getPath()),
                    Card.class);
            try {
                String name = card.getName();
                Card mainCard = (Card) DataBaseController.getObjectByGson(DataBaseController.readDataFromFile(file.getPath()),
                        CSVDataBaseController.getClassByName(name));
                CardView cardView = new CardView(mainCard, 2.5, false, true);
                importedCard.getChildren().add(cardView);
            } catch(NullPointerException e) {
                result.setText("no such card!");
            }
        }
    }

    public void exportCard() {

        String cardName = cardToExport.getText();

        Card card = CSVDataBaseController.getCardByCardName(cardName);

        if (card == null) {
            result.setText("no such card");
        } else {
            String resultText = DataBaseController.makeObjectJson(card);
            DataBaseController.createFileByPathAndData("Resource/Cards/" + Utils.getPascalCase(cardName) + ".json", resultText);
            result.setText("successful");
        }
    }

    public void getBack() {
        MainMenu.getInstance(null).run();
    }
}
