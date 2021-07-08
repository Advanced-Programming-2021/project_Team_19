package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.DuelMenuController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Gamer;
import model.User;
import model.Data.graphicDataForServerToNotifyOtherClient;
import view.Menu.Menu;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static view.graphic.GameView.getIndexById;

public class GameGraphicControllerForTest extends Menu {

    Stage stage2;
    GameView gameView1;
    GameView gameView2;
    Game game;

    public GameGraphicControllerForTest(Stage first, Stage second, Gamer firstGamer, Gamer secondGamer) {
        super("game test");
//        TestInit();
        Scene scene = new Scene(new Pane(), menuGraphic.sceneX, menuGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        second.setScene(scene);
        GameData gameData = new GameData(firstGamer, secondGamer);
        game = new Game(gameData);
        gameView1 = new GameView(first, this, firstGamer, secondGamer, game);
        gameView2 = new GameView(second, this, secondGamer, firstGamer, game);

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
    }

    public void testRun() {
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

    public void run() {
        startGame();
    }

    @Deprecated
    public void TestInit() {
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

    public void startGame() {
        gameView1.run();
        gameView2.run();
        new Timeline(new KeyFrame(Duration.millis(2500), event -> {
            graphicsForEvents(game.run(new DataForGameRun
                    ("start game", gameView1.self)), null, 0);
        })).play();
    }

    private GameView getTheOtherGameView(GameView gameView) {
        return gameView.equals(gameView1) ? gameView2 : gameView1;
    }

    public void notifyOtherGameViewToDoSomething
            (GameView notifier, graphicDataForServerToNotifyOtherClient data) {

        GameView otherGameView = getTheOtherGameView(notifier);

        switch (data.command) {
            case "change phase" -> otherGameView.handleChangePhaseBecauseOfOtherClientNotification();

            case "set position attack" -> otherGameView.handleChangePositionGraphicBOOCN(data.card, "attack");
            case "set position defence" -> otherGameView.handleChangePositionGraphicBOOCN(data.card, "defence");

            case "summon" -> otherGameView.handleSummonRivalMonsterGraphicBOOCN(data.card, data.index);
            case "set monster" -> otherGameView.handleSetRivalMonsterGraphicBOOCN(data.card, data.index);
            case "set spell" -> otherGameView.handleSetRivalSpellGraphicBOOCN(data.card, data.index);
            case "activate spell" -> otherGameView.handleActivateRivalSpellGraphicBOOCN(data.card, data.index);
            case "flip summon" -> otherGameView.handleRivalFlipSummonGraphicBOOCN(data.card);
            case "flip" -> otherGameView.handleFlipCardGraphicBOOCN(data.card);
            case "increase rival lp" -> otherGameView.handleIncreaseLpGraphicBOOCN(data.index, false);
            case "increase self lp" -> otherGameView.handleIncreaseLpGraphicBOOCN(data.index, true);
            case "add card from deck to hand" -> otherGameView.handleAddRivalCardFromDeckToHandGraphicBOOCN(data.card);
            case "add card to self graveyard" -> otherGameView.handleAddCardToGraveYardGraphicBOOTN
                    (data.card, true);
            case "add card to rival graveyard" -> otherGameView.handleAddCardToGraveYardGraphicBOOTN
                    (data.card, false);

            default -> handleOtherCommands(otherGameView, data);
        }


    }

    private void handleOtherCommands(GameView otherGameView, graphicDataForServerToNotifyOtherClient data) {
        if (data.command.startsWith("destroy card from")) {
            String[] strings = data.command.split(":");
            boolean isSelf = strings[1].equals("self");
            int zone = strings[2].equals("monster zone") ? 0 : (strings[2].equals("spell zone") ? 1 : 2);
            otherGameView.handleDestroyCardFromFieldOrHandBOOCN(data.index, zone, isSelf);
        }
    }


    private double responseIsForPhaseChange(GameView gameView, String phaseChangeResponse) {
        if (phaseChangeResponse.equals("draw phase")) {
            gameView.handleChangePhase();
        } else if (phaseChangeResponse.equals("stand by phase")) {
            gameView.handleChangePhase();
//            todo    standby phase
        } else if (phaseChangeResponse.equals("end phase")) {
            gameView.handleChangePhase();
//            todo    end phase
        } else if (phaseChangeResponse.equals("main phase 1")) {
            gameView.handleChangePhase();
//            todo    main phase 1
        } else if (phaseChangeResponse.equals("battle phase")) {
            gameView.handleChangePhase();
//            todo    battle phase
        } else if (phaseChangeResponse.equals("main phase 2")) {
            gameView.handleChangePhase();
//            todo    main phase 2
        } else if (phaseChangeResponse.matches("game finished \\w+")) {
//           todo     finish game
        }
        return 500;
    }

    public void graphicsForEvents(ArrayList<DataFromGameRun> events, CardView cardView, int index) {

        double time = 500;

        GameView gameView = events.get(index).gamerOfAction.equals(gameView1.self) ? gameView1 : gameView2;

        String response = events.get(index).event;

        if (response.matches("summon \\d")) {
            time = gameView.handleSummonGraphic(cardView, getIndexById(Integer.parseInt(response.substring(7))));
        } else if (response.equals("add card to hand")) {
            time = gameView.handleAddCardFromDeckToHandGraphic(events.get(index).cardsForEvent.get(0));
        } else if (response.matches("set spell \\d")) {
            time = gameView.handleSetSpellGraphic(cardView, getIndexById(Integer.parseInt(response.substring(10))));
        } else if (response.matches("position changed to (attack|defence)")) {
            time = gameView.handleChangePositionGraphic(cardView, Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(response, "position changed to (attack|defence)")));
        } else if (response.matches("get \\d monsters")) {
            gameView.initForSummonOrSetBySacrifice(Integer.parseInt(response.substring(4, 5)), cardView);
        } else if (response.equals("attack monster")) {
            gameView.initForAttackMonster(cardView);
        } else if (response.matches("rival loses \\d+")) {
            time = gameView.handleIncreaseLpGraphic(-Integer.parseInt(response.substring(12)), false);
        } else if (response.matches("set monster \\d")) {
            time = gameView.handleSetMonsterGraphic(cardView, getIndexById(Integer.parseInt(response.substring(12))));
        } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
            time = gameView.handleAttackResultGraphic(Utils.getMatcher(response,
                    "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp"));
        } else if (response.equals("flip summoned successfully")) {
            time = gameView.handleFlipSummonGraphic(cardView);
        } else if (response.matches("summon \\d sacrifice( \\d)+")) {
            time = gameView.handleSummonSetWithSacrificeGraphics(cardView,
                    getIndexById(Integer.parseInt(response.substring(7, 8))), response.substring(19), false);
        } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
            time = gameView.handleSummonSetWithSacrificeGraphics(cardView,
                    getIndexById(Integer.parseInt(response.substring(12, 13))), response.substring(24), true);
        } else if (response.startsWith("game finished ")) {
            String name = response.split(" ")[2];
            if (gameView1.self.getUsername().equals(name)) {
                DuelMenuController.finishDuel(gameView1.self, GameData.getGameData());
            } else {

            }
        } else {
            time = responseIsForPhaseChange(gameView, response);
        }
        if (index < events.size() - 1) {
            GameView finalGameView = gameView;
            new Timeline(new KeyFrame(Duration.millis(time),
                    EventHandler -> graphicsForEvents(events, cardView, index + 1))).play();
        }
    }

}