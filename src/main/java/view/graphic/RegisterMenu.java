package view.graphic;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import view.Menu.WelcomeMenu;

public class RegisterMenu extends menuGraphic {

    public void run(){

        Button button = new Button("start");
        BorderPane borderPane = new BorderPane();
        button.setOnAction(e -> {
            WelcomeMenu.getInstance().run();
        });
        borderPane.setCenter(button);
        stage.getScene().setRoot(borderPane);
    }
}
