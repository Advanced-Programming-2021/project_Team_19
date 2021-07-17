package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.*;
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
import model.Card.Card;
import model.Gamer;
import model.User;
import view.GetInput;
import view.Menu.ChangeCardBetweenRounds;
import view.Menu.Menu;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static view.graphic.GameView.getIndexById;
import static view.graphic.GameView.getIndexByRivalId;

public class GameGraphicController extends Menu {

    public Stage stage2;
    public GameView gameView1;
    GameView gameView2;
    Game game;
    int rounds;
    int gameStarterWins = 0;
    int invitedGamerWins = 0;
    boolean isInverted;
    private static int cnt;

    public static ArrayList<User> allUser = new ArrayList<>();
    public static ArrayList<DeckModifierBetweenGames> allDeckModifiers = new ArrayList<>();


    public GameGraphicController(boolean ai) {
        super("test game");
        if (ai)
            TestInit2();
        else
            TestInit();
    }

    public GameGraphicController(int rounds, Stage first, Stage second, Gamer firstGamer, Gamer secondGamer, boolean isInverted) {
        super("game test");
        this.isInverted = isInverted;
        stage = first;
        stage2 = second;
        this.rounds = rounds;
        Scene scene = new Scene(new Pane(), menuGraphic.sceneX, menuGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        second.setScene(scene);
        GameData gameData = new GameData(firstGamer, secondGamer);
        game = new Game(gameData, rounds);
        if (!isInverted) {
            gameView1 = new GameView(first, this, firstGamer, secondGamer, game);
            gameView2 = new GameView(second, this, secondGamer, firstGamer, game);
        } else {
            gameView1 = new GameView(second, this, firstGamer, secondGamer, game);
            gameView2 = new GameView(first, this, secondGamer, firstGamer, game);
        }

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
    }

    public GameGraphicController(int rounds, Stage first, Stage second, Gamer firstGamer, Gamer secondGamer, boolean isInverted,
                                 int gameStarterWins, int invitedGamerWins) {
        super("game test");
        System.err.println(123);
        this.isInverted = isInverted;
        stage = first;
        stage2 = second;
        this.rounds = rounds;
        Scene scene = new Scene(new Pane(), menuGraphic.sceneX, menuGraphic.sceneY);
        scene.getStylesheets().add("CSS/Css.css");
        second.setScene(scene);
        GameData gameData = new GameData(firstGamer, secondGamer);
        game = new Game(gameData, rounds);
        if (!isInverted) {
            gameView1 = new GameView(first, this, firstGamer, secondGamer, game);
            gameView2 = new GameView(second, this, secondGamer, firstGamer, game);
        } else {
            gameView1 = new GameView(second, this, firstGamer, secondGamer, game);
            gameView2 = new GameView(first, this, secondGamer, firstGamer, game);
        }

        gameView2.setRivalGameView(gameView1);
        gameView1.setRivalGameView(gameView2);
        this.invitedGamerWins = invitedGamerWins;
        this.gameStarterWins = gameStarterWins;

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

    @Deprecated
    public void TestInit2() {
        User user1 = UserDataBaseController.getUserByUsername("mohammad");
        Gamer gamer1 = new Gamer(user1);
        Gamer gamer2 = AI.getGamer(0);
        GameData gameData = new GameData(gamer1, gamer2);
        game = new Game(gameData, 1);
        gameView1 = new GameView(stage, this, gamer1, gamer2, game);
    }

    public boolean AIMod() {
        try {
            if (AI.getGamer(0).equals(game.gameData.getCurrentGamer())) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    public void startGame() {
        gameView1.run();
        try {
            gameView2.run();
        } catch (NullPointerException ignored) {

        }

        new Timeline(new KeyFrame(Duration.millis(3500), event -> {
            graphicsForEvents(game.run(new DataForGameRun
                    ("start game", gameView1.self)), null, 0);
        })).play();
    }

    private GameView getTheOtherGameView(GameView gameView) {
        if (gameView == null) {
            return gameView2 == null ? gameView1 : gameView2;
        }
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
            try {
                gameView.handleChangePhase();
            } catch (NullPointerException ignored) {

            }

            try {
                otherGameView.handleChangePhase();
            } catch (NullPointerException ignored) {

            }


        }
        return 1000;
    }

    public void graphicsForEvents(ArrayList<DataFromGameRun> events, CardView cardView, int index) {

        double time = 500;

        if (events.size() == 0) {
            if (AIMod()) {

                new Timeline(new KeyFrame(Duration.millis(1000), EventHandler -> graphicsForEvents
                        (game.run(new DataForGameRun(GetInput.getAICommand(), AI.getGamer(0))),
                                null, 0))).play();
            }
            return;
        }


        GameView gameView = null;

        if (gameView1 == null) {
            if (events.get(index).gamerOfAction.equals(gameView2.self)) {
                gameView = gameView2;
            }
        } else if (gameView2 == null) {
            if (events.get(index).gamerOfAction.equals(gameView1.self)) {
                gameView = gameView1;
            }
        } else {
            gameView = events.get(index).gamerOfAction.equals(gameView1.self) ? gameView1 : gameView2;
        }

        GameView otherGameView = getTheOtherGameView(gameView);

        String response = events.get(index).event;

        System.err.println(response);

        if (response.startsWith("activate trap")) {
            time = graphicsHandlingForSpells
                    (gameView, gameView.spellZoneCards.get(
                            getIndexById(Integer.parseInt(response.split(":")[0].substring(14)))
                    ), response.split(":")[3].replace("activate spell ", ""));

            new Timeline(new KeyFrame(Duration.millis(time), EventHandler -> {
                CardActionManager.setMode(actionManagerMode.NORMAL_MODE);
            }
            )).play();
        } else if (response.startsWith("ask gamer for trap:")) {
            CardActionManager.setMode(actionManagerMode.ACTION_NOT_ALLOWED_MODE);
            try {
                if (gameView == null) {
                    handleAI();
                } else {
                    gameView.askForTrap(response.split(":")[1]);
                }
            } catch (NullPointerException ignored) {
            }
        } else if (response.matches("summon \\d")) {
            int cardIndex = Integer.parseInt(response.substring(7));
            try {
                otherGameView.handleSummonRivalMonsterGraphic(cardView.card, getIndexByRivalId(cardIndex));
            } catch (NullPointerException ignored) {
            }
            try {
                time = gameView.handleSummonGraphic(cardView.card, getIndexById(cardIndex));
            } catch (NullPointerException ignored) {
            }
        } else if (response.equals("add card to hand")) {
            try {
                otherGameView.handleAddRivalCardFromDeckToHandGraphic(events.get(index).cardsForEvent.get(0));
            } catch (NullPointerException ignored) {
            }

            try {
                time = gameView.handleAddCardFromDeckToHandGraphic(events.get(index).cardsForEvent.get(0));
            } catch (NullPointerException ignored) {

            }

        } else if (response.matches("set spell \\d")) {
            int cardIndex = Integer.parseInt(response.substring(10));
            try {
                otherGameView.handleSetRivalSpellGraphic(cardView.card, getIndexByRivalId(cardIndex));
            } catch (NullPointerException ignored) {
            }
            try {
                time = gameView.handleSetSpellGraphic(cardView.card, getIndexById(cardIndex));
            } catch (NullPointerException ignored) {
            }

        } else if (response.startsWith("activate spell ")) {

            time = graphicsHandlingForSpells(gameView, cardView, response.replace("activate spell ", ""));
        } else if (response.matches("position changed to (attack|defence)")) {
            String position = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(response, "position changed to (attack|defence)"));
            try {
                otherGameView.handleChangePositionOfRivalMonsterGraphicBOOCN(cardView.card, position);
            } catch (NullPointerException ignored) {

            }
            try {
                time = gameView.handleChangePositionGraphicForSelfMonsters(cardView.card, position);
            } catch (NullPointerException ignored) {

            }

        } else if (response.matches("get \\d monsters")) {
            try {
                gameView.initForSummonOrSetBySacrifice(Integer.parseInt(response.substring(4, 5)), cardView);
            } catch (NullPointerException ignored) {

            }

        } else if (response.equals("attack monster")) {
            try {
                gameView.initForAttackMonster(cardView);
            } catch (NullPointerException ignored) {

            }

        } else if (response.matches("rival loses \\d+")) {
            time = handleIncreaseLP(gameView, response);
        } else if (response.matches("increase lp \\d+")) {
            time = handleIncreaseLPByCheatCode(gameView, response);
        } else if (response.matches("set monster \\d")) {
            int cardIndex = Integer.parseInt(response.substring(12));

            try {
                otherGameView.handleSetRivalMonsterGraphicBOOCN(cardView.card, getIndexByRivalId(cardIndex));
            } catch (NullPointerException ignored) {
            }

            try {
                time = gameView.handleSetMonsterGraphic
                        (cardView.card, getIndexById(cardIndex));
            } catch (NullPointerException ignored) {
            }

        } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
            Matcher matcher = Utils.getMatcher(response,
                    "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp");
            try {
                otherGameView.handleRivalAttackResultGraphic(matcher);
            } catch (NullPointerException ignored) {

            }
            try {
                matcher.reset();
                time = gameView.handleAttackResultGraphic(matcher);
            } catch (NullPointerException ignored) {

            }


        } else if (response.equals("flip summoned successfully")) {
            try {
                otherGameView.handleRivalFlipSummonGraphicBOOCN(cardView.getCard());
            } catch (NullPointerException ignored) {

            }
            try {
                time = gameView.handleFlipSummonGraphic(cardView.card);
            } catch (NullPointerException ignored) {

            }


        } else if (response.matches("summon \\d sacrifice( \\d)+")) {
            try {
                otherGameView.handleRivalSummonSetWithSacrificeGraphic(cardView.getCard(),
                        getIndexByRivalId(Integer.parseInt(response.substring(7, 8))), false,
                        getSacrificesIndex(response.substring(19), false));
            } catch (NullPointerException ignored) {

            }
            try {
                time = gameView.handleSummonSetWithSacrificeGraphics(cardView.card,
                        getIndexById(Integer.parseInt(response.substring(7, 8))), false,
                        getSacrificesIndex(response.substring(19), true));
            } catch (NullPointerException ignored) {

            }


        } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
            try {
                otherGameView.handleRivalSummonSetWithSacrificeGraphic(cardView.getCard(),
                        getIndexByRivalId(Integer.parseInt(response.substring(12, 13))), true,
                        getSacrificesIndex(response.substring(24), false));
            } catch (NullPointerException ignored) {

            }
            try {
                time = gameView.handleSummonSetWithSacrificeGraphics(cardView.card,
                        getIndexById(Integer.parseInt(response.substring(12, 13))), true,
                        getSacrificesIndex(response.substring(24), true));
            } catch (NullPointerException ignored) {

            }


        } else if (response.startsWith("game finished ")) {
            String name = response.split(" ")[2];
            if (rounds == 1) {
                stage2.close();
                if (gameView1.self.getUsername().equals(name)) {
                    DuelMenuController.finishDuel(gameView1.self, GameData.getGameData(), rounds);
                } else {
                    DuelMenuController.finishDuel(gameView1.rival, GameData.getGameData(), rounds);
                }
            } else {
                if (gameView1.self.getUsername().equals(name)) {
                    gameStarterWins++;
                } else {
                    invitedGamerWins++;
                }

                if (gameStarterWins == 2) {
                    stage2.close();
                    DuelMenuController.finishDuel(gameView1.self, GameData.getGameData(), rounds);
                } else if (invitedGamerWins == 2) {
                    stage2.close();
                    DuelMenuController.finishDuel(gameView1.rival, GameData.getGameData(), rounds);
                } else {
                    cnt = 0;
                    ChangeCardBetweenRounds.setToZero();
                    allDeckModifiers.add(new DeckModifierBetweenGames(gameView1.self.getUser()));
                    allDeckModifiers.add(new DeckModifierBetweenGames(gameView1.rival.getUser()));
                    allUser.add(gameView1.self.getUser());
                    allUser.add(gameView1.rival.getUser());
                    new ChangeCardBetweenRounds().run(this, stage);
                    new ChangeCardBetweenRounds().run(this, stage2);
                }
            }
        } else {
            time = responseIsForPhaseChange(gameView, response);
        }
        if (index < events.size() - 1) {
            new Timeline(new KeyFrame(Duration.millis(time),
                    EventHandler -> graphicsForEvents(events, cardView, index + 1))).play();
        }

        if (index == events.size() - 1) {
            handleAI();
        }
    }

    private double handleIncreaseLP(GameView gameView, String response) {
        double time = 500;
        int lp = -Integer.parseInt(response.substring(12));
        try {
            time = getTheOtherGameView(gameView).handleIncreaseLpGraphic(lp, true);
        } catch (NullPointerException ignored) {

        }
        try {
            time = gameView.handleIncreaseLpGraphic(lp, false);
        } catch (NullPointerException ignored) {

        }
        return time;
    }

    private double handleIncreaseLPByCheatCode(GameView gameView, String response){
        double time = 500;
        int lp = Integer.parseInt(response.substring(12));
        try {
            time = getTheOtherGameView(gameView).handleIncreaseLpGraphic(lp, false);
        } catch (NullPointerException ignored) {

        }
        try {
            time = gameView.handleIncreaseLpGraphic(lp, true);
        } catch (NullPointerException ignored) {

        }
        return time;
    }

    private void handleAI() {
        if (AIMod()) {

            new Timeline(new KeyFrame(Duration.millis(1000), EventHandler -> {
                graphicsForEvents
                        (game.run(new DataForGameRun(GetInput.getAICommand(), AI.getGamer(0))),
                                null, 0);
            })).play();
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

        double time = 500;

        int index = spellCommand.matches("\\d .*") ?
                getIndexById(Integer.parseInt(spellCommand.split(" ")[0])) : -1;

        spellCommand = spellCommand.replaceFirst("(-|)\\d ", "");

        System.out.println(index);
        System.out.println(spellCommand);

        GameView otherGameView = getTheOtherGameView(gameView);
        if (spellCommand.equals("destroy this spell")) {
            handleDestroySpell(gameView, cardView, index);

        } else if (spellCommand.matches("destroy rival monsters([ \\d]*)")) {
            String ids = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(spellCommand, "destroy rival monsters([ \\d]*)"));
            try {
                gameView.activateSpell1(index, cardView.card, true, ids);
            } catch (NullPointerException ignored) {

            }

            try {
                otherGameView.activateSpell1(index, cardView.card, false, ids);
            } catch (NullPointerException ignored) {

            }


        } else if (spellCommand.matches("destroy rival monsters([ \\d]*) self monsters([ \\d]*)")) {

            Matcher idMatcher = Utils.getMatcher(spellCommand,
                    "destroy rival monsters([ \\d]*) self monsters([ \\d]*)");
            idMatcher.find();

            try {
                gameView.activateSpell2(index, cardView.card, true, idMatcher.group(1), idMatcher.group(2));
            } catch (NullPointerException ignored) {
            }
            try {
                otherGameView.activateSpell2(index, cardView.card, false, idMatcher.group(1), idMatcher.group(2));
            } catch (NullPointerException ignored) {
            }

        } else if (spellCommand.matches("destroy rival spells([ \\d]*)")) {
            Matcher idMatcher = Utils.getMatcher(spellCommand, "destroy rival spells([ \\d]*)");
            idMatcher.find();
            try {
                gameView.activateSpell3(index, cardView.card, true, idMatcher.group(1));
            } catch (NullPointerException ignored) {
            }
            try {
                otherGameView.activateSpell3(index, cardView.card, false, idMatcher.group(1));
            } catch (NullPointerException ignored) {
            }


        } else if (spellCommand.startsWith("field spell ")) {
            Matcher matcher = Utils.getMatcher(spellCommand, "field spell (.*)");
            matcher.find();
            changeStages(gameView, cardView.card, matcher.group(1));
        } else if (spellCommand.matches("destroy spell and rival loses \\d+")) {
            handleIncreaseLP(gameView, spellCommand.substring(18));
            handleDestroySpell(gameView, cardView, index);
        }
        return time;
    }

    private void handleDestroySpell(GameView gameView, CardView cardView, int index) {
        try {
            gameView.justDestroyActivatedSpellOrTrap(index, cardView.card, true);
        } catch (NullPointerException ignored) {
        }

        try {
            getTheOtherGameView(gameView).justDestroyActivatedSpellOrTrap(index, cardView.card, false);
        } catch (NullPointerException ignored) {

        }
    }

    private double changeStages(GameView gameView, Card card, String fieldSpellName) {
        double time = 1000;

        try {
            getTheOtherGameView(gameView).activateFieldSpell(card, false);
        } catch (NullPointerException ignored) {

        }
        try {
            time = gameView.activateFieldSpell(card, true);
        } catch (NullPointerException ignored) {

        }


        new Timeline(new KeyFrame(Duration.millis(time), EventHandler -> {

            String backgroundName = "";
            switch (fieldSpellName) {
                case "Yami" -> backgroundName = "yami";
                case "Forest" -> backgroundName = "gaia";
                case "Umii Ruka" -> backgroundName = "umi";
                case "Closed Forest" -> backgroundName = "mori";
            }
            Image image = new Image("Assets/Field/fie_" + backgroundName + ".bmp");

            try {
                ((Rectangle) gameView1.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
            } catch (NullPointerException ignored) {

            }
            try {
                ((Rectangle) gameView2.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
            } catch (NullPointerException ignored) {

            }

        })).play();

        return time;
    }

    public void setHasFirstChoosed() {
        cnt ++;
        if (cnt == 2) {
            new GameGraphicController(rounds, stage, stage2, gameView1.self, gameView1.rival,
                    isInverted, gameStarterWins, invitedGamerWins).run();
        }
    }

    public void setHasSecondChoosed() {
        cnt ++;
        if (cnt == 2) {
            new GameGraphicController(rounds, stage, stage2, gameView1.self, gameView1.rival,
                    isInverted, gameStarterWins, invitedGamerWins).run();
        }
    }

}