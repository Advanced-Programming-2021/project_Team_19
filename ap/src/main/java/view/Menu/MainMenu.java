package view.Menu;


import controller.DuelControllers.DuelMenuController;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import view.graphic.Shop;

public class MainMenu extends Menu {


    private static MainMenu instance = null;
    private static String username;
    private Pane pane = new Pane();


    private MainMenu() {
        super("Main Menu");
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance =  new MainMenu();
        }
        return instance;
    }


    public void run(String username) {

        MainMenu.username = username;

        setMainMenu();
        stage.setTitle("Main Menu");
        stage.getScene().setRoot(pane);

    }

    private void setMainMenu() {

        VBox buttonBox = setSeveralChoiceButtons("Profile Menu", "Deck Menu",
                "Duel Menu", "Shop Menu", "Scoreboard Menu", "Import/Export Menu");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> ProfileMenu.getInstance().run(username));

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> DeckMenu.getInstance().run(username));

        buttonBox.getChildren().get(2).setOnMouseClicked(event -> new DuelMenuController("Duel Menu").run(username));

        buttonBox.getChildren().get(3).setOnMouseClicked(event -> new Shop().run(username));

        buttonBox.getChildren().get(4).setOnMouseClicked(event -> new ScoreBoardMenu().run());

        buttonBox.getChildren().get(5).setOnMouseClicked(event -> ImportAndExportMenu.getInstance().run());

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> WelcomeMenu.getInstance().run());

        pane.getChildren().addAll(buttonBox, backButton);
    }

}
