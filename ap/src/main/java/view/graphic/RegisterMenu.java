package view.graphic;

import controller.DataBaseControllers.DataBaseController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.Menu.LoginMenu;

public class RegisterMenu extends Application {

    @Override
    public void start(Stage stage){
        Button button = new Button("start");
        BorderPane borderPane = new BorderPane();
        button.setOnAction(e -> {
            DataBaseController.makeResourceDirectory();

            LoginMenu.getInstance().run();
        });
        borderPane.setCenter(button);
        Scene scene = new Scene(borderPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
