package view.Menu;

import AnythingIWant.ClientNetwork;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class TVMenu extends Menu{
    public TVMenu() {
        super("TV Menu");
    }

    public void run(){
        stage.setTitle("TV Menu");
        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());
        Pane pane = new Pane();
        pane.getChildren().add(backButton);
        stage.setOnCloseRequest(event -> ClientNetwork.getInstance().disconnect());
        stage.getScene().setRoot(pane);
    }
}
