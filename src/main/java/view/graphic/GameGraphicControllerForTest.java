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
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
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

import static view.graphic.GameView.getIndexById;
import static view.graphic.GameView.getIndexByRivalId;

public class GameGraphicControllerForTest extends Menu {

    Stage stage2;
    GameView gameView1;
    GameView gameView2;
    Game game;
    int rounds;

    public GameGraphicControllerForTest() {
        super("test game");
        TestInit();
    }

    public GameGraphicControllerForTest(int rounds, Stage first, Stage second, Gamer firstGamer, Gamer secondGamer) {
        super("game test");
        this.rounds = rounds;
        Scene scene = new Scene(new Pane(), menuGraphic.sceneX, menuGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        second.setScene(scene);
        GameData gameData = new GameData(firstGamer, secondGamer);
        game = new Game(gameData, rounds);
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
        game = new Game(gameData, rounds);

        gameView1 = new GameView(stage, this, gamer1, gamer2, game);
        gameView2 = new GameView(stage2, this, gamer2, gamer1, game);

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
    }

    public void startGame() {
        gameView1.run();
        gameView2.run();

        new Timeline(new KeyFrame(Duration.millis(3500), event -> {
            graphicsForEvents(game.run(new DataForGameRun
                    ("start game", gameView1.self)), null, 0);
        })).play();
    }

    private GameView getTheOtherGameView(GameView gameView) {
        return gameView.equals(gameView1) ? gameView2 : gameView1;
    }

    private double responseIsForPhaseChange(GameView gameView, String phaseChangeResponse) {
        GameView otherGameView = getTheOtherGameView(gameView);

        boolean changePhase = phaseChangeResponse.equals("draw phase") ||
                phaseChangeResponse.equals("stand by phase") ||
                phaseChangeResponse.equals("battle phase") ||
                phaseChangeResponse.equals("main phase 1") ||
                phaseChangeResponse.equals("main phase 2") ||
                phaseChangeResponse.equals("end phase");

        if (changePhase) {
            gameView.handleChangePhase();
            otherGameView.handleChangePhase();
        } else if (phaseChangeResponse.matches("game finished \\w+")) {
//           todo     finish game
        }
        return 1000;
    }

    public void graphicsForEvents(ArrayList<DataFromGameRun> events, CardView cardView, int index) {

        double time = 500;

        GameView gameView = events.get(index).gamerOfAction.equals(gameView1.self) ? gameView1 : gameView2;
        GameView otherGameView = getTheOtherGameView(gameView);

        String response = events.get(index).event;

        if (response.matches("summon \\d")) {
            int cardIndex = Integer.parseInt(response.substring(7));
            otherGameView.handleSummonRivalMonsterGraphic(cardView.card, getIndexByRivalId(cardIndex));
            time = gameView.handleSummonGraphic(cardView.card, getIndexById(cardIndex));
        } else if (response.equals("add card to hand")) {
            otherGameView.handleAddRivalCardFromDeckToHandGraphic(events.get(index).cardsForEvent.get(0));
            time = gameView.handleAddCardFromDeckToHandGraphic(events.get(index).cardsForEvent.get(0));
        } else if (response.matches("set spell \\d")) {
            int cardIndex = Integer.parseInt(response.substring(10));
            otherGameView.handleSetRivalSpellGraphic(cardView.card, getIndexByRivalId(cardIndex));
            time = gameView.handleSetSpellGraphic(cardView.card, getIndexById(cardIndex));
        } else if (response.startsWith("activate spell ")) {
            time = graphicsHandlingForSpells(gameView, cardView, response.replace("activate spell ", ""));
        } else if (response.matches("position changed to (attack|defence)")) {
            String position = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(response, "position changed to (attack|defence)"));
            otherGameView.handleChangePositionOfRivalMonsterGraphicBOOCN(cardView.card, position);
            time = gameView.handleChangePositionGraphicForSelfMonsters(cardView.card, position);
        } else if (response.matches("get \\d monsters")) {
            gameView.initForSummonOrSetBySacrifice(Integer.parseInt(response.substring(4, 5)), cardView);
        } else if (response.equals("attack monster")) {
            gameView.initForAttackMonster(cardView);
        } else if (response.matches("rival loses \\d+")) {
            int lp = -Integer.parseInt(response.substring(12));
            otherGameView.handleIncreaseLpGraphic(lp, true);
            time = gameView.handleIncreaseLpGraphic(lp, false);
        } else if (response.matches("increase lp \\d+")) {
            int lp = Integer.parseInt(response.substring(12));
            otherGameView.handleIncreaseLpGraphic(lp, false);
            time = gameView.handleIncreaseLpGraphic(lp, true);
        } else if (response.matches("set monster \\d")) {
            int cardIndex = Integer.parseInt(response.substring(12));
            otherGameView.handleSetRivalMonsterGraphicBOOCN(cardView.card, getIndexByRivalId(cardIndex));
            time = gameView.handleSetMonsterGraphic
                    (cardView.card, getIndexById(cardIndex));
        } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
            Matcher matcher = Utils.getMatcher(response,
                    "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp");
            otherGameView.handleRivalAttackResultGraphic(matcher);
            matcher.reset();
            time = gameView.handleAttackResultGraphic(matcher);
        } else if (response.equals("flip summoned successfully")) {
            otherGameView.handleRivalFlipSummonGraphicBOOCN(cardView.getCard());
            time = gameView.handleFlipSummonGraphic(cardView.card);
        } else if (response.matches("summon \\d sacrifice( \\d)+")) {
            otherGameView.handleRivalSummonSetWithSacrificeGraphic(cardView.getCard(),
                    getIndexByRivalId(Integer.parseInt(response.substring(7, 8))), false,
                    getSacrificesIndex(response.substring(19), false));
            time = gameView.handleSummonSetWithSacrificeGraphics(cardView.card,
                    getIndexById(Integer.parseInt(response.substring(7, 8))), false,
                    getSacrificesIndex(response.substring(19), true));
        } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
            otherGameView.handleRivalSummonSetWithSacrificeGraphic(cardView.getCard(),
                    getIndexByRivalId(Integer.parseInt(response.substring(12,13))), true,
                    getSacrificesIndex(response.substring(24), false));
            time = gameView.handleSummonSetWithSacrificeGraphics(cardView.card,
                    getIndexById(Integer.parseInt(response.substring(12, 13))), true,
                    getSacrificesIndex(response.substring(24), true));
        } else if (response.startsWith("game finished ")) {
            String name = response.split(" ")[2];
            if (gameView1.self.getUsername().equals(name)) {
                DuelMenuController.finishDuel(gameView1.self, GameData.getGameData(), game.round);
            } else {
                DuelMenuController.finishDuel(gameView.rival, GameData.getGameData(), game.round);
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

    private ArrayList<Integer> getSacrificesIndex(String sacrificeData, boolean isSelf) {
        ArrayList<Integer> sacrificesIndex = new ArrayList<>();
        for (String indexStr : sacrificeData.split(" ")) {
            if (isSelf)
                sacrificesIndex.add(getIndexById(Integer.parseInt(indexStr)));
            else
                sacrificesIndex.add(getIndexByRivalId(Integer.parseInt(indexStr)));
        }
        return sacrificesIndex;
    }

    private double graphicsHandlingForSpells(GameView gameView, CardView cardView, String spellCommand) {
        System.out.println(spellCommand);

        int index = spellCommand.matches("\\d .*") ?
                Integer.parseInt(spellCommand.split(" ")[0]) : -1;

        spellCommand = spellCommand.replaceFirst("(-|)\\d ", "");

        System.out.println(index);
        System.out.println(spellCommand);


        GameView otherGameView = getTheOtherGameView(gameView);
        if (spellCommand.equals("destroy this spell")) {
            gameView.justDestroyActivatedSpellOrTrap(index, cardView.card, true);
            otherGameView.justDestroyActivatedSpellOrTrap(index, cardView.card, false);
        } else if (spellCommand.matches("destroy rival monsters([ \\d]*)")) {
            String ids = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(spellCommand, "destroy rival monsters([ \\d]*)"));
            gameView.activateSpell1(index, cardView.card, true, ids);
            otherGameView.activateSpell1(index, cardView.card, false, ids);
        } else if (spellCommand.matches("destroy rival monsters([ \\d]*) self monsters([ \\d]*)")) {
            Matcher idMatcher = Utils.getMatcher(spellCommand,
                    "destroy rival monsters([ \\d]*) self monsters([ \\d]*)");
            idMatcher.find();
            gameView.activateSpell2(index, cardView.card, true, idMatcher.group(1), idMatcher.group(2));
            otherGameView.activateSpell2(index, cardView.card, false, idMatcher.group(1), idMatcher.group(2));

        } else if (spellCommand.matches("destroy rival spells([ \\d]*)")) {
            Matcher idMatcher = Utils.getMatcher(spellCommand, "destroy rival spells([ \\d]*)");
            idMatcher.find();
            gameView.activateSpell3(index, cardView.card, true, idMatcher.group(1));
            otherGameView.activateSpell3(index, cardView.card, false, idMatcher.group(1));
        } else if (spellCommand.startsWith("field spell ")){
            Matcher matcher = Utils.getMatcher(spellCommand, "field spell (.*)");
            matcher.find();
            changeStages(matcher.group(1));
        }
        return 0;
    }

    private void changeStages(String fieldSpellName) {
//        todo move card to field zone
        String backgroundName = "";
        switch (fieldSpellName){
            case "Yami" -> backgroundName = "yami";
            case "Forest" -> backgroundName = "gaia";
            case "Umii Ruka" -> backgroundName = "umi";
            case "Closed Forest" -> backgroundName = "mori";
        }
        Image image = new Image("Assets/Field/fie_" + backgroundName + ".bmp");
        ((Rectangle) gameView1.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
        ((Rectangle) gameView2.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
    }

}