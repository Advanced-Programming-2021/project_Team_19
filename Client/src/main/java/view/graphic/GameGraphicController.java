package view.graphic;

import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.User;
import view.Menu.Menu;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static view.graphic.GameView.getIndexById;
import static view.graphic.GameView.getIndexByRivalId;

public class GameGraphicController extends Menu {

    public MyThread checkerThread;
    public GameView gameView;
    int rounds;
    int gameStarterWins = 0;
    int invitedGamerWins = 0;

    public static ArrayList<User> allUser = new ArrayList<>();

    String username1;
    String username2;
    String gameCode;

    public GameGraphicController(String gameCode, int rounds, String username1, String username2) {
        super("game");
        this.rounds = rounds;
        this.username1 = username1;
        this.username2 = username2;
        gameView = new GameView(stage, this, username, username.equals(username1) ?
                username2: username1);
        this.gameCode = gameCode;
        checkerThread = new MyThread(this);
    }

    public void run() {
        startGame();
    }

    public ArrayList<DataFromGameRun> sendDataToServer(String command){
        return Menu.sendDataToServer
                (new DataForGameRun(gameCode + "&" + command)).gameGraphicData;
    }

    public void sendDataAndRun(String command){
        ArrayList<DataFromGameRun> datas = sendDataToServer(command);
        run(datas, 0);
    }

    private void run(ArrayList<DataFromGameRun> datas, int index){
        if(datas.size() == 0){
            return;
        }

        DataFromGameRun data = datas.get(index);
        double time;

        if(data.gamerOfActionName.equals(username)){
            time = handleSelfActionGraphic(data);
        } else {
            time = handleRivalActionsGraphic(data);
        }

        if(index < datas.size() - 1) {
            new Timeline(new KeyFrame(Duration.millis(time), Event -> {
                run(datas, index + 1);
            })).play();
        }
    }

    public void startGame() {
        gameView.run();
        new Timeline(new KeyFrame(Duration.millis(3500), event -> {
            sendDataAndRun("start game");
        })).play();
    }

    public void sendGameRunDataToServer(DataForGameRun data){
        sendDataToServer(data);
    }


    private double responseIsForPhaseChange(String phaseChangeResponse) {

        boolean changePhase = phaseChangeResponse.equals("draw phase") ||
                phaseChangeResponse.equals("stand by phase") ||
                phaseChangeResponse.equals("battle phase") ||
                phaseChangeResponse.equals("main phase 1") ||
                phaseChangeResponse.equals("main phase 2") ||
                phaseChangeResponse.equals("end phase");

        if (changePhase) {
            gameView.handleChangePhase();
        }
        return 1000;
    }

    public double handleRivalActionsGraphic(DataFromGameRun event) {

        double time = 500;

        int cardId = event.cardId;
        String response = event.event;

        if (response.matches("summon \\d")) {
            int cardIndex = Integer.parseInt(response.substring(7));
            time = gameView.handleSummonRivalMonsterGraphic
                    (cardId - 1, getIndexByRivalId(cardIndex));
        } else if (response.equals("add card to hand")) {
            time = gameView.handleAddRivalCardFromDeckToHandGraphic
                    (event.cardNames.get(0));
        } else if (response.matches("set spell \\d")) {
            int cardIndex = Integer.parseInt(response.substring(10));
            time = gameView.handleSetRivalSpellGraphic(cardId - 1, getIndexByRivalId(cardIndex));


        } else if (response.startsWith("activate spell ")) {

            String spellCommand = response.replace("activate spell ", "");
            time = 500;

            int newIndex = spellCommand.matches("\\d .*") ?
                    getIndexById(Integer.parseInt(spellCommand.split(" ")[0])) : -1;

            spellCommand = spellCommand.replaceFirst("(-|)\\d ", "");

            int OldCardIndex = newIndex == -1 ? getIndexById(cardId) : cardId - 1;
            int rivalOldCardIndex = newIndex == -1 ? getIndexByRivalId(cardId) : cardId - 1;

            if (spellCommand.equals("destroy this spell")) {
                gameView.justDestroyActivatedSpellOrTrap(newIndex, rivalOldCardIndex, false);

            } else if (spellCommand.matches("destroy rival monsters([ \\d]*)")) {
                String ids = Utils.getFirstGroupInMatcher(
                        Utils.getMatcher(spellCommand, "destroy rival monsters([ \\d]*)"));
                gameView.activateSpell1(newIndex, rivalOldCardIndex, false, ids);

            } else if (spellCommand.matches("destroy rival monsters([ \\d]*) self monsters([ \\d]*)")) {

                Matcher idMatcher = Utils.getMatcher(spellCommand,
                        "destroy rival monsters([ \\d]*) self monsters([ \\d]*)");
                idMatcher.find();

                gameView.activateSpell2
                        (newIndex, rivalOldCardIndex, false, idMatcher.group(1), idMatcher.group(2));

            } else if (spellCommand.matches("destroy rival spells([ \\d]*)")) {
                Matcher idMatcher = Utils.getMatcher(spellCommand, "destroy rival spells([ \\d]*)");
                idMatcher.find();

                gameView.activateSpell3(newIndex, rivalOldCardIndex, false, idMatcher.group(1));

            } else if (spellCommand.startsWith("field spell ")) {
                Matcher matcher = Utils.getMatcher(spellCommand, "field spell (.*)");
                matcher.find();
                time = 1000;
                gameView.activateFieldSpell(cardId - 1, false);

                new Timeline(new KeyFrame(Duration.millis(time), EventHandler -> {

                    String backgroundName = "";
                    switch (matcher.group(1)) {
                        case "Yami" -> backgroundName = "yami";
                        case "Forest" -> backgroundName = "gaia";
                        case "Umii Ruka" -> backgroundName = "umi";
                        case "Closed Forest" -> backgroundName = "mori";
                    }
                    Image image = new Image("Assets/Field/fie_" + backgroundName + ".bmp");
                    ((Rectangle) gameView.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
                })).play();


            } else if (spellCommand.matches("destroy spell and rival loses \\d+")) {
//                handleIncreaseLP(gameView, spellCommand.substring(18));
                handleDestroySpell(gameView, OldCardIndex, rivalOldCardIndex, newIndex);
            }

        } else if (response.matches("position changed to (attack|defence)")) {
            String position = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(response, "position changed to (attack|defence)"));
            time =
                    gameView.handleChangePositionOfRivalMonsterGraphicBOOCN(getIndexByRivalId(cardId), position);


        } else if (response.matches("get \\d monsters")) {
            gameView.initForSummonOrSetBySacrifice(Integer.parseInt(response.substring(4, 5)), getIndexById(cardId));
        } else if (response.equals("attack monster")) {
            gameView.initForAttackMonster(getIndexById(cardId));
        } else if (response.matches("rival loses \\d+")) {
            time = handleIncreaseRivalLp(gameView, response);
        } else if (response.matches("increase lp \\d+")) {
            time = handleIncreaseLPByCheatCode(gameView, response);
        } else if (response.matches("set monster \\d")) {
            int cardIndex = Integer.parseInt(response.substring(12));

            time = gameView.handleSetRivalMonsterGraphicBOOCN(cardId - 1, getIndexByRivalId(cardIndex));


        } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
            Matcher matcher = Utils.getMatcher(response,
                    "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp");
            time = gameView.handleRivalAttackResultGraphic(matcher);


        } else if (response.equals("flip summoned successfully")) {
            time = gameView.handleRivalFlipSummonGraphicBOOCN(getIndexByRivalId(cardId));


        } else if (response.matches("summon \\d sacrifice( \\d)+")) {
            time =
                    gameView.handleRivalSummonSetWithSacrificeGraphic(cardId - 1,
                            getIndexByRivalId(Integer.parseInt(response.substring(7, 8))), false,
                            getSacrificesIndex(response.substring(19), false));


        } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
            time = gameView.handleRivalSummonSetWithSacrificeGraphic(cardId - 1,
                    getIndexByRivalId(Integer.parseInt(response.substring(12, 13))), true,
                    getSacrificesIndex(response.substring(24), false));


        } else if (response.startsWith("game finished ")) {
            String name = response.split(" ")[2];
            //todo hi hi va ha ha
        } else {
            time = responseIsForPhaseChange(response);
        }

        return time;

    }

    private double handleSelfActionGraphic(DataFromGameRun event) {

        double time = 500;

        int cardId = event.cardId;
        String response = event.event;

        System.err.println(response);

        if (response.startsWith("activate trap")) {
//            time = graphicsHandlingForSpells
//                    (gameView, Integer.parseInt(response.split(":")[0].substring(14))
//                            , response.split(":")[3].replace("activate spell ", ""));
//
//            new Timeline(new KeyFrame(Duration.millis(time), EventHandler -> {
//                CardActionManager.setMode(actionManagerMode.NORMAL_MODE);
//            }
//            )).play();
        } else if (response.startsWith("ask gamer for trap:")) {
//            CardActionManager.setMode(actionManagerMode.ACTION_NOT_ALLOWED_MODE);
            gameView.askForTrap(response.split(":")[1]);
        } else if (response.matches("summon \\d")) {
            int cardIndex = Integer.parseInt(response.substring(7));
            time = gameView.handleSummonGraphic(cardId - 1, getIndexById(cardIndex));
        } else if (response.equals("add card to hand")) {
            time = gameView.handleAddCardFromDeckToHandGraphic(event.cardNames.get(0));
        } else if (response.matches("set spell \\d")) {
            int cardIndex = Integer.parseInt(response.substring(10));
            time = gameView.handleSetSpellGraphic(cardId - 1, getIndexById(cardIndex));
        } else if (response.startsWith("activate spell ")) {

            String spellCommand = response.replace("activate spell ", "");

            int newIndex = spellCommand.matches("\\d .*") ?
                    getIndexById(Integer.parseInt(spellCommand.split(" ")[0])) : -1;

            spellCommand = spellCommand.replaceFirst("(-|)\\d ", "");

            int OldCardIndex = newIndex == -1 ? getIndexById(cardId) : cardId - 1;
            int rivalOldCardIndex = newIndex == -1 ? getIndexByRivalId(cardId) : cardId - 1;

            if (spellCommand.equals("destroy this spell")) {
                handleDestroySpell(gameView, OldCardIndex, rivalOldCardIndex, newIndex);

            } else if (spellCommand.matches("destroy rival monsters([ \\d]*)")) {
                String ids = Utils.getFirstGroupInMatcher(
                        Utils.getMatcher(spellCommand, "destroy rival monsters([ \\d]*)"));
                gameView.activateSpell1(newIndex, OldCardIndex, true, ids);


            } else if (spellCommand.matches("destroy rival monsters([ \\d]*) self monsters([ \\d]*)")) {

                Matcher idMatcher = Utils.getMatcher(spellCommand,
                        "destroy rival monsters([ \\d]*) self monsters([ \\d]*)");
                idMatcher.find();
                gameView.activateSpell2(newIndex, OldCardIndex, true, idMatcher.group(1), idMatcher.group(2));


            } else if (spellCommand.matches("destroy rival spells([ \\d]*)")) {
                Matcher idMatcher = Utils.getMatcher(spellCommand, "destroy rival spells([ \\d]*)");
                idMatcher.find();
                gameView.activateSpell3(newIndex, OldCardIndex, true, idMatcher.group(1));


            } else if (spellCommand.startsWith("field spell ")) {

                Matcher matcher = Utils.getMatcher(spellCommand, "field spell (.*)");
                matcher.find();
                time = gameView.activateFieldSpell(cardId - 1, true);
                new Timeline(new KeyFrame(Duration.millis(time), EventHandler -> {

                    String backgroundName = "";
                    switch (matcher.group(1)) {
                        case "Yami" -> backgroundName = "yami";
                        case "Forest" -> backgroundName = "gaia";
                        case "Umii Ruka" -> backgroundName = "umi";
                        case "Closed Forest" -> backgroundName = "mori";
                    }
                    Image image = new Image("Assets/Field/fie_" + backgroundName + ".bmp");
                    ((Rectangle) gameView.gamePane.getChildren().get(0)).setFill(new ImagePattern(image));
                })).play();

            } else if (spellCommand.matches("destroy spell and rival loses \\d+")) {
                handleIncreaseRivalLp(gameView, spellCommand.substring(18));
                handleDestroySpell(gameView, OldCardIndex, rivalOldCardIndex, newIndex);
            }

        } else if (response.matches("position changed to (attack|defence)")) {
            String position = Utils.getFirstGroupInMatcher(
                    Utils.getMatcher(response, "position changed to (attack|defence)"));
            time = gameView.handleChangePositionGraphicForSelfMonsters(getIndexById(cardId), position);
        } else if (response.matches("get \\d monsters")) {
            gameView.initForSummonOrSetBySacrifice
                    (Integer.parseInt(response.substring(4, 5)), getIndexById(cardId));
        } else if (response.equals("attack monster")) {
            gameView.initForAttackMonster(getIndexById(cardId));
        } else if (response.matches("rival loses \\d+")) {
            time = handleIncreaseRivalLp(gameView, response);
        } else if (response.matches("increase lp \\d+")) {
            time = handleIncreaseLPByCheatCode(gameView, response);
        } else if (response.matches("set monster \\d")) {
            int cardIndex = Integer.parseInt(response.substring(12));
            time = gameView.handleSetMonsterGraphic
                    (cardId - 1, getIndexById(cardIndex));
        } else if (response.matches("attack \\d (destroy|stay) \\d (destroy|stay) (flip |)(self|rival) loses \\d+ lp")) {
            Matcher matcher = Utils.getMatcher(response,
                    "attack (\\d) (destroy|stay) (\\d) (destroy|stay) (flip |)(self|rival) loses (\\d+) lp");
            time = gameView.handleAttackResultGraphic(matcher);


        } else if (response.equals("flip summoned successfully")) {
            time = gameView.handleFlipSummonGraphic(getIndexById(cardId));


        } else if (response.matches("summon \\d sacrifice( \\d)+")) {
            time = gameView.handleSummonSetWithSacrificeGraphics(
                    cardId - 1, getIndexByRivalId(Integer.parseInt(response.substring(7, 8))),
                    false,
                    getSacrificesIndex(response.substring(19), true));
        } else if (response.matches("set monster \\d sacrifice( \\d)+")) {
            time = gameView.handleSummonSetWithSacrificeGraphics(cardId - 1,
                    getIndexById(Integer.parseInt(response.substring(12, 13))), true,
                    getSacrificesIndex(response.substring(24), true));

        } else if (response.startsWith("game finished ")) {
        } else {
            time = responseIsForPhaseChange(response);
        }
        return time;
    }

    private double handleIncreaseRivalLp(GameView gameView, String response) {
        double time = 500;
        int lp = -Integer.parseInt(response.substring(12));
        time = gameView.handleIncreaseLpGraphic(lp, false);
        return time;
    }

    private double handleIncreaseLPByCheatCode(GameView gameView, String response) {
        double time = 500;
        int lp = Integer.parseInt(response.substring(12));
        time = gameView.handleIncreaseLpGraphic(lp, true);
        return time;
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

    private void handleDestroySpell(GameView gameView, int cardIndex, int rivalCardIndex, int index) {
        gameView.justDestroyActivatedSpellOrTrap(index, cardIndex, true);

    }

}


class MyThread extends Thread{

    GameGraphicController controller;
    public MyThread(GameGraphicController controller){
        this.controller = controller;
    }

    @Override
    public void run() {

        while(true){
            controller.sendDataAndRun("چه خبر");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}