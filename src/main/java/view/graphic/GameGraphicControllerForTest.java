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
import model.Data.graphicDataForServerToNotifyOtherClient;
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

    public void run() {
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

    public void init() {
        Scene scene = new Scene(new Pane(), menuGraphic.sceneX, menuGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        stage2 = new Stage();
        stage2.setScene(scene);
        User user1 = UserDataBaseController.getUserByUsername("mohammad");
        Gamer gamer1 = new Gamer(user1);
        User user2 = UserDataBaseController.getUserByUsername("reza");
        Gamer gamer2 = new Gamer(user2);

        GameData gameData = new GameData(gamer1, gamer2);
        game = new Game(gameData);

        gameView1 = new GameView(stage, this, gamer1, gamer2, game);
        gameView2 = new GameView(stage2, this, gamer2, gamer1, game);

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
    }

    public void startGame(){
        gameView1.run();
        gameView2.run();
    }

    private GameView getTheOtherGameView(GameView gameView) {
        return gameView.equals(gameView1) ? gameView2 : gameView1;
    }

    public void notifyOtherGameViewToDoSomething
            (GameView notifier, graphicDataForServerToNotifyOtherClient data) {

        GameView otherGameView = getTheOtherGameView(notifier);

        switch (data.command) {
            case "summon" -> otherGameView.handleRivalSummonGraphic(data.card, data.index);
            case "set monster" ->  otherGameView.handleRivalSetMonsterGraphic(data.card, data.index);
            case "set spell" -> otherGameView.handleRivalSetSpellGraphic(data.card, data.index);
            case "activate spell" -> otherGameView.handleRivalActivateSpellGraphic(data.card, data.index);
            case "flip summon" -> otherGameView.handleRivalFlipSummonGraphic(data.card);
            case "flip" -> otherGameView.handleRivalFlipCardGraphic(data.card);
            case "increase lp" -> otherGameView.handleRivalIncreaseLpGraphic(data.index);
            default -> throw new IllegalStateException("Unexpected value: " + data.command);
        }
    }


}