package view.Menu;


import AnythingIWant.ClientNetwork;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Data.DataForServerFromClient;

public class MainMenu extends Menu {


    private static MainMenu instance = null;
    private static String username;
    private Pane pane = new Pane();


    private MainMenu(String username) {
        super("Main Menu");
        MainMenu.username = username;
    }

    public static MainMenu getInstance(String username) {
        if (instance == null || username != null) {
            instance =  new MainMenu(username);
        }
        return instance;
    }


    public void run() {

        setMainMenu();
        stage.setTitle("Main Menu");
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
        stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
        stage.getScene().setRoot(pane);

    }

    private void setMainMenu() {

        VBox buttonBox = setSeveralChoiceButtons("Profile Menu", "Deck Menu",
                "Shop Menu", "Scoreboard Menu", "Lobby", "Chat Room", "TV");

        buttonBox.getChildren().get(0).setOnMouseClicked(event -> ProfileMenu.getInstance().run(username, username));

        buttonBox.getChildren().get(1).setOnMouseClicked(event -> new Deck().run(username));

        buttonBox.getChildren().get(2).setOnMouseClicked(event -> new Shop().run(username));

        buttonBox.getChildren().get(3).setOnMouseClicked(event -> new ScoreBoardMenu().run(username));

        buttonBox.getChildren().get(4).setOnMouseClicked(event -> new Lobby().run());

        buttonBox.getChildren().get(5).setOnMouseClicked(event -> new ChatMenu().run(username));

        buttonBox.getChildren().get(6).setOnMouseClicked(event -> new TVMenu().run());

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> {
            Menu.sendDataToServer(new DataForServerFromClient("user logout", token, "Login Menu"));
            token = null;
            WelcomeMenu.getInstance().run();
        });

        pane.getChildren().addAll(buttonBox, backButton);
    }

}
