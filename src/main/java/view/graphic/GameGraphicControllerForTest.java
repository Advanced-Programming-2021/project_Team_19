package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Gamer;
import model.User;
import view.Menu.Menu;

public class GameGraphicControllerForTest extends Menu {

    Stage stage2;
    GameView gameView1;
    GameView gameView2;
    Game game;

    public GameGraphicControllerForTest() {
        super("game test");
        init();
    }

    public void run(){

        Pane pane = new Pane();
        stage.getScene().setRoot(pane);
        Button button = new Button("hoh");
        pane.getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startGame();
            }
        });
    }

    public void init(){
        Scene scene = new Scene(new Pane(), GameGraphic.sceneX, GameGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        stage2 = new Stage();
        stage2.setScene(scene);
        User user1 = UserDataBaseController.getUserByUsername("mohammad");
        Gamer gamer1 =  new Gamer(user1);
        User user2 = UserDataBaseController.getUserByUsername("reza");
        Gamer gamer2 = new Gamer(user2);

        GameData gameData = new GameData(gamer1, gamer2);
        game = new Game(gameData);

        gameView1 = new GameView(stage,gamer1, gamer2, game);
        gameView2 = new GameView(stage2, gamer2, gamer1, game);

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
    }

    public void startGame(){
        gameView1.run();
        gameView2.run();
    }

}