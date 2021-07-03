package view.Menu;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import view.Printer.Printer;

public class ImportAndExportMenu extends Menu {

    private static ImportAndExportMenu instance = null;
    private Pane pane = new Pane();
    private Label responseLabel = new Label();
    private static TextField cardNameTextField;

    private ImportAndExportMenu() {
        super("Import/Export Menu");
    }

    public static ImportAndExportMenu getInstance() {
        if (instance == null) {
            instance = new ImportAndExportMenu();
        }
        return instance;
    }


    public void run() {

        setImportMenu();
        stage.setTitle("Import/Export Menu");
        stage.getScene().setRoot(pane);
    }

    private void setImportMenu() {
        HBox cardName = textFieldGridToEnterInfo("enter the name of the card you want to import/export:");
        cardName.setLayoutX(150);
        cardName.setLayoutY(100);

        cardNameTextField = (TextField) ((VBox) cardName.getChildren().get(1)).getChildren().get(0);

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        Button importButton = new Button("import card");
        readyCursorForButton(importButton);
        importButton.setOnMouseClicked(event -> {responseLabel.setText("");
            importCard();
        });

        Button exportButton = new Button("export card");
        readyCursorForButton(exportButton);
        exportButton.setOnMouseClicked(event -> {responseLabel.setText("");
            exportCard();
        });

        HBox buttonBox = new HBox(15);
        buttonBox.getChildren().addAll(importButton, exportButton);
        buttonBox.setLayoutX(280);
        buttonBox.setLayoutY(150);

        responseLabel = new Label();
        responseLabel.setLayoutX(150);
        responseLabel.setLayoutY(190);

        pane.getChildren().addAll(cardName, backButton, buttonBox, responseLabel);

    }

    private void importCard() {

        String cardName = cardNameTextField.getText();

        if (cardName.equals("")){
            Printer.setFailureResponseToLabel(responseLabel, "enter a card name");
            return;
        }

        clearTextField();

        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient("import card " + cardName, menuName));

        Printer.setAppropriateResponseToLabelFromData(data, responseLabel);
    }

    private void exportCard() {

        String cardName = cardNameTextField.getText();

        if (cardName.equals("")){
            Printer.setFailureResponseToLabel(responseLabel, "enter a card name");
            return;
        }

        clearTextField();

        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient("export card " + cardName, menuName));

        Printer.setAppropriateResponseToLabelFromData(data, responseLabel);
    }

    private void clearTextField(){
        cardNameTextField.clear();
    }
}
