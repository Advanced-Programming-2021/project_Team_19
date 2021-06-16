package view.graphic;

import controller.DataBaseControllers.DataBaseController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.Menu.LoginMenu;

public class RegisterMenu extends GraphicMenu{

    public void run(){

        Button button = new Button("start");
        BorderPane borderPane = new BorderPane();
        button.setOnAction(e -> {
            LoginMenu.getInstance().run();
        });
        borderPane.setCenter(button);
        stage.getScene().setRoot(borderPane);
    }
}
