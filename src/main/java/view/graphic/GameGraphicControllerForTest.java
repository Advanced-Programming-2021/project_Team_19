package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
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

import java.util.ArrayList;

import static view.graphic.GameView.getIndexById;

public class GameGraphicControllerForTest extends Menu {

    Stage stage2;
    GameView gameView1;
    GameView gameView2;
    Game game;

    public GameGraphicControllerForTest() {
        super("game test");
        TestInit();
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
            case "change phase" -> otherGameView.handleChangePhaseBecauseOfOtherClientNotification();

            case "set position attack" ->
                    otherGameView.handleChangePositionGraphicBOOCN(data.card, "attack");
            case "set position defence" ->
                    otherGameView.handleChangePositionGraphicBOOCN(data.card, "defence");

            case "summon" -> otherGameView.handleSummonRivalMonsterGraphicBOOCN(data.card, data.index);
            case "set monster" ->  otherGameView.handleSetRivalMonsterGraphicBOOCN(data.card, data.index);
            case "set spell" -> otherGameView.handleSetRivalSpellGraphicBOOCN(data.card, data.index);
            case "activate spell" -> otherGameView.handleActivateRivalSpellGraphicBOOCN(data.card, data.index);
            case "flip summon" -> otherGameView.handleRivalFlipSummonGraphicBOOCN(data.card);
            case "flip" -> otherGameView.handleFlipCardGraphicBOOCN(data.card);
            case "increase rival lp" -> otherGameView.handleIncreaseLpGraphicBOOCN(data.index, false);
            case "increase self lp" -> otherGameView.handleIncreaseLpGraphicBOOCN(data.index, true);
            case "add card from deck to hand" -> otherGameView.handleAddRivalCardFromDeckToHandGraphicBOOCN(data.card);
            case "add card to self graveyard"-> otherGameView.handleAddCardToGraveYardGraphicBOOTN
                    (data.card, true);
            case "add card to rival graveyard" -> otherGameView.handleAddCardToGraveYardGraphicBOOTN
                    (data.card, false);

            default -> handleOtherCommands(otherGameView, data);
        }


    }

    private void handleOtherCommands(GameView otherGameView, graphicDataForServerToNotifyOtherClient data) {
        if(data.command.startsWith("destroy card from")){
            String [] strings = data.command.split(":");
            boolean isSelf = strings[1].equals("self");
            int zone = strings[2].equals("monster zone") ? 0 : (strings[2].equals("spell zone") ? 1 : 2);
            otherGameView.handleDestroyCardFromFieldOrHandBOOCN(data.index, zone, isSelf);
        }
    }




    private void responseIsForPhaseChange(GameView gameView, String phaseChangeResponse) {
        if (phaseChangeResponse.equals("draw phase")) {
            gameView.handleChangePhase();
//            gameView.handleAddCardFromDeckToHandGraphic(game.gameData.getCurrentGamer().getGameBoard().getHand().getCard(game.gameData.getCurrentGamer().getGameBoard().getHand().getSize() - 1));
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
    }

    public void graphicsForEvents(GameView gameView, ArrayList<String> events, CardView cardView) {
        for (String response : events) {
            if (response.matches("summon \\d")) {
                gameView.handleSummonGraphic(cardView, getIndexById(Integer.parseInt(response.substring(7))));
            } else if (response.matches("set spell \\d")) {
                gameView.handleSetSpellGraphic(cardView, getIndexById(Integer.parseInt(response.substring(10))));
            } else if (response.matches("position changed to (attack|defence)")) {
                gameView.handleChangePositionGraphic(cardView, Utils.getFirstGroupInMatcher(
                        Utils.getMatcher(response, "position changed to (attack|defence)")));
            } else if (response.matches("get \\d monsters")) {
                gameView.initForSummonOrSetBySacrifice(Integer.parseInt(response.substring(4, 5)), cardView);
            } else if (response.equals("attack monster")) {
                gameView.initForAttackMonster(cardView);
            } else if (response.matches("rival loses \\d+")) {
                gameView.handleIncreaseLpGraphic(-Integer.parseInt(response.substring(12)), false);
            } else if (response.matches("set monster \\d")) {
                gameView.handleSetMonsterGraphic(cardView, getIndexById(Integer.parseInt(response.substring(12))));
            } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
                gameView.handleAttackResultGraphic(Utils.getMatcher(response,
                        "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp"));
            } else if (response.equals("flip summoned successfully")) {
                gameView.handleFlipSummonGraphic(cardView);
            } else if (response.matches("summon \\d sacrifice( \\d)+")) {
                gameView.handleSummonSetWithSacrificeGraphics(cardView,
                        getIndexById(Integer.parseInt(response.substring(7, 8))), response.substring(19), false);
            } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
                gameView.handleSummonSetWithSacrificeGraphics(cardView,
                        getIndexById(Integer.parseInt(response.substring(13, 14))), response.substring(25), true);
            } else {
                responseIsForPhaseChange(gameView, response);
            }
        }
    }



}