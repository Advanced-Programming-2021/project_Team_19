package controller.DuelControllers;

import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.Actions.*;
import controller.DuelControllers.Phases.DrawPhase;
import controller.DuelControllers.Phases.StandbyPhase;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Traps.SpeedEffectTrap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.GameEvent;
import model.Gamer;
import model.Phase;
import model.TriggerLabel;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Locale;

import static controller.DuelControllers.actionManagerMode.HEHE;


public class Game {

    public GameData gameData;
    public Card multiActionCard;
    public int round;


    public Game(GameData gameData, int round) {
        this.gameData = gameData;
        this.round = round;
    }

    public ArrayList<DataFromGameRun> run(DataForGameRun dataFromClient) {
        DataFromGameRun.reset();
        String command = dataFromClient.getCommand();
        switch (command) {

            case "activate trap" -> {
                TriggerLabel label = gameData.triggerLabel;
                TriggerActivationData data = (TriggerActivationData) new ActivateTriggerTrapEffect
                        (label.action).handleActivate();
                String message = data.message;
                String[] messages = message.split(":");
                new DataFromGameRun(message);
                if (messages[1].equals("change turn")) {
                    gameData.changeTurn();
                }
                label.inProgress = false;
                label.shouldRunAgain = label.shouldRunAgain && !data.hasActionStopped;
            }
            case "cancel activate trap" -> {
                gameData.triggerLabel.inProgress = false;
                if(!gameData.getCurrentGamer().equals(gameData.getTurnOwner())){
                    gameData.changeTurn();
                }
            }
            case "start game" -> {
                runServerSideGameEvents();
            }
            case "set" -> {
                new DataFromGameRun(new Set(gameData).run(null));
            }
            case "set with sacrifice" -> {
                multiActionCard = gameData.getSelectedCard();
                CardActionManager.setMode(actionManagerMode.SET_MODE);
                new DataFromGameRun(new Set(gameData).actionIsValid());
            }
            case "normal summon" -> {
                String summonResponse = new NormalSummon(gameData).run(null);
                new DataFromGameRun(summonResponse);
            }
            case "summon with sacrifice" -> {
                multiActionCard = gameData.getSelectedCard();
                CardActionManager.setMode(actionManagerMode.SUMMON_MODE);
                new DataFromGameRun(new NormalSummon(gameData).actionIsValid());
            }
            case "attack direct" -> {
                new DirectAttack(gameData).run(true);
//                new DataFromGameRun(new DirectAttack(gameData).run());
            }
            case "activate spell" -> {
                new DataFromGameRun("activate spell " + new ActivateSpellOrTrapNormally(gameData).run());
            }
            case "attack monster" -> {
                multiActionCard = gameData.getSelectedCard();
                CardActionManager.setMode(actionManagerMode.ATTACK_MONSTER_MODE);
                new DataFromGameRun(new AttackMonster(gameData, 0).actionIsValid());
            }
            case "flip summon" -> {
                new DataFromGameRun(new FlipSummon(gameData).run());
            }
            case "get Atk|Def" -> {
                new DataFromGameRun(getAtkDef(gameData));
            }
            case "next phase" -> {
                String nextPhaseName = goToNextPhase(gameData);
                new DataFromGameRun(nextPhaseName);
            }
        }
        if (command.matches("sacrifice \\d( \\d)*")) {
            gameData.setSelectedCard(multiActionCard);
            String summonOrSetResponse = "";
            if (CardActionManager.mode.equals(actionManagerMode.SUMMON_MODE)) {
                summonOrSetResponse = new NormalSummon(gameData).run(command.substring(10));
            } else if (CardActionManager.mode.equals(actionManagerMode.SET_MODE)) {
                summonOrSetResponse = new Set(gameData).run(command.substring(10));
            }
            CardActionManager.setMode(actionManagerMode.NORMAL_MODE);
            new DataFromGameRun(summonOrSetResponse);
        } else if (command.matches("attack \\d")) {
            gameData.setSelectedCard(multiActionCard);
            /*String attackResponse =*/ new AttackMonster(gameData, Integer.parseInt(command.substring(7))).run(true);
//            CardActionManager.setMode(actionManagerMode.NORMAL_MODE);
//            new DataFromGameRun(attackResponse);
        } else if (command.matches("set position (attack|defence)")) {
            String setPositionResponse = new SetPosition(gameData).run(Utils.getMatcher(command, "set position (.*)"));
            new DataFromGameRun(setPositionResponse);
        } else if (command.matches("game button @\\d+@")) {
            String lpIncrease = command.split(" ")[2].replace("@", "");
            CheatCodes.increaseLifePoint(gameData, lpIncrease);
            new DataFromGameRun("increase lp " + lpIncrease);
        }


        runServerSideGameEvents();

        return DataFromGameRun.eventDataFromServer;
    }


    public ArrayList<String> getValidCommandsForCard(DataForGameRun data) throws Exception {
        if (gameData.getCurrentGamer().equals(data.getGamer())) {
            gameData.setSelectedCard(data.getCard());
            return (new CardActionManager(data.getCard())).getValidActions();
        }
        throw new Exception("not your turn");
    }

    public void checkTriggerData() {

        if (gameData.triggerLabel == null) {
            return;
        }

        if(gameData.triggerLabel.inProgress){
            return;
        }

        Action action = gameData.triggerLabel.action;

        gameData.addActionToCurrentActions(action);
        gameData.setActionIndexForTriggerActivation(gameData.getCurrentActions().indexOf(action));

        if (action.canTurnOwnerActivateTrapBecauseOfAnAction() &&
                gameData.triggerLabel.shouldAskFromFirstGamer){
            gameData.triggerLabel.inProgress = true;
            new DataFromGameRun("ask gamer for trap:" + action.getActionName());
        } else {
            if (action.canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction() &&
                    gameData.triggerLabel.shouldAskFromSecondGamer) {
                gameData.triggerLabel.inProgress = true;
                gameData.changeTurn();
                new DataFromGameRun("ask gamer for trap:" + action.getActionName());
            }
            gameData.triggerLabel.shouldAskFromSecondGamer = false;
        }
        gameData.triggerLabel.shouldAskFromFirstGamer = false;

        if(!gameData.triggerLabel.shouldAskFromFirstGamer
                && !gameData.triggerLabel.shouldAskFromSecondGamer
                && !gameData.triggerLabel.inProgress){
            if(gameData.triggerLabel.shouldRunAgain){
                String data = ((Attack)gameData.triggerLabel.action).run(false);
                new DataFromGameRun(data);
            }
            gameData.removeActionFromCurrentActions(gameData.triggerLabel.action);
            gameData.triggerLabel = null;
            gameData.setActionIndexForTriggerActivation(-1);
            CardActionManager.setMode(actionManagerMode.NORMAL_MODE);
        }
    }

    public void runServerSideGameEvents() {

        while (true) {

            checkTriggerData();

            if (checkLabels(gameData)) {
                continue;
            }

            if (!gameData.hasAskedForSpellsThisPhase) {

                if (canRivalActivateSpell(gameData)) {
                    Utils.changeTurn(gameData);
                    gameData.showBoard();

                    if (Utils.askForActivate("It's " + gameData.getCurrentPhase() + " phase")) {
                        handleActivatingSpellByRival(gameData);
                    }

                    Utils.changeTurn(gameData);
                }
                gameData.hasAskedForSpellsThisPhase = true;
            }


            if (gameData.isGameOver()) {
                new DataFromGameRun("game finished " + finishGame(gameData).getUsername());
//                return finishGame(gameData);
            }

            if (gameData.getCurrentPhase().equals(Phase.DRAW)) {
                Card card = new DrawPhase().run(gameData);
                goToNextPhase(gameData);
                new DataFromGameRun("add card to hand", card);
                new DataFromGameRun("draw phase");
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.STANDBY)) {
                new StandbyPhase().run(gameData);
                goToNextPhase(gameData);
                new DataFromGameRun("stand by phase");
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.END)) {
                gameData.setSelectedCard(null);
                gameData.turnFinished();
                goToNextPhase(gameData);
                new DataFromGameRun("end phase");
                continue;
            }

            gameData.setEvent(GameEvent.NORMAL);
            break;
        }
    }


    @Deprecated
    public Gamer DeprecatedRun() {

        String command;

        while (true) {

//            if (checkLabels(gameData)) {
//                continue;
//            }

            if (!gameData.hasAskedForSpellsThisPhase) {

                if (canRivalActivateSpell(gameData)) {
                    Utils.changeTurn(gameData);
                    gameData.showBoard();

                    if (Utils.askForActivate("It's " + gameData.getCurrentPhase() + " phase")) {
                        handleActivatingSpellByRival(gameData);
                    }

                    Utils.changeTurn(gameData);
                }
                gameData.hasAskedForSpellsThisPhase = true;
            }


            if (gameData.isGameOver()) {
                return finishGame(gameData);
            }
            if (gameData.getCurrentPhase().equals(Phase.DRAW)) {
                new DrawPhase().run(gameData);
                goToNextPhase(gameData);
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.STANDBY)) {
                new StandbyPhase().run(gameData);
                goToNextPhase(gameData);
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.END)) {
                gameData.setSelectedCard(null);
                gameData.turnFinished();
                goToNextPhase(gameData);
                continue;
            }

            gameData.setEvent(GameEvent.NORMAL);


            command = GetInput.getString();///////////////////////////////////////////////////////
            gameData.setEvent(null);

            if (gameData.isRitualSummoning() &&
                    command.matches("cancel")) {
                Printer.print("you successfully cancelled your ritual summon");
                gameData.removeRitualSummoning();
                continue;
            }

            if (gameData.isRitualSummoning() &&
                    (!command.matches("summon") && !command.startsWith("select"))) {
                Printer.print("you should ritual summon right now");
                continue;
            }

            if (gameData.getTurn() == 1) {
                if (command.equals("next phase")) {
                    gameData.goToEndPhase();
                    Printer.print(gameData.getCurrentPhase().getPhaseName());
                    continue;
                }
            }

            if (command.matches("surrender")) {
                if (askForSurrender())
                    return handleSurrender(gameData);
            } else if (command.matches("increase --LP \\d+")) {
                CheatCodes.increaseLifePoint(gameData, Utils.getFirstGroupInMatcher(Utils.getMatcher(command, "increase --LP (\\d+)")));
            } else if (command.matches("duel set-winner \\w+")) {
                if (CheatCodes.winGame(gameData, Utils.getFirstGroupInMatcher(Utils.getMatcher(command, "duel set-winner (\\w+)")))) {
                    return handleSurrender(gameData);
                }
            } else if (command.matches("set --position (attack|defence)")) {
                new SetPosition(gameData).run(Utils.getMatcher(command, "set --position (.*)"));
            } else if (command.matches("multiply --attack \\d+")) {
                CheatCodes.multiplyAttack(gameData, Utils.getFirstGroupInMatcher(Utils.getMatcher(command, "multiply --attack (\\d+)")));
            } else if (command.matches("activate")) {
                new ActivateSpellOrTrapNormally(gameData).run();
            } else if (command.equals("activate effect")) {
                new ActivateEffectMonster(gameData).run();
            } else {
                Printer.printInvalidCommand();
            }

        }
    }


    private boolean askForSurrender() {
        while (true) {
            Printer.print("do you want to surrender?");
            String command = GetInput.getString().toLowerCase(Locale.ROOT);
            if (command.matches("yes"))
                return true;
            else if (command.matches("no"))
                return false;
            else
                Printer.printInvalidCommand();
        }
    }

    private Gamer handleSurrender(GameData gameData) {
        gameData.surrender();
        return finishGame(gameData);
    }

    private Gamer finishGame(GameData gameData) {
        Gamer winner = determineWinner(gameData);

        gameData.finishGame();
        winner.wonGame();
        Printer.print(winner.getUsername() + " won the game and the score is: " +
                gameData.getGameStarter().getCurrentScoreInDuel() + " - " +
                gameData.getInvitedGamer().getCurrentScoreInDuel());
        return winner;
    }

    private Gamer determineWinner(GameData gameData) {
        if (gameData.getCurrentGamer().getLifePoint() == -1)
            return gameData.getSecondGamer();
        else if (gameData.getSecondGamer().getLifePoint() == -1)
            return gameData.getCurrentGamer();
        else {
            Gamer winner = gameData.getCurrentGamer();

            if (winner.getLifePoint() == 0)
                winner = gameData.getSecondGamer();

            return winner;

        }
    }

    public String goToNextPhase(GameData gameData) {

        gameData.goToNextPhase();
        Printer.print(gameData.getCurrentPhase().getPhaseName());
        gameData.hasAskedForSpellsThisPhase = false;
        return gameData.getCurrentPhase().getPhaseName();
    }

    private boolean checkLabels(GameData gameData) {

        boolean label1 = checkLabelsOfGamer(gameData.getCurrentGamer());
        boolean label2 = checkLabelsOfGamer(gameData.getSecondGamer());

        return label1 | label2;

    }

    private boolean checkLabelsOfGamer(Gamer gamer) {


        ArrayList<EffectLabel> tempArray = (ArrayList<EffectLabel>) gamer.getEffectLabels().clone();

        System.out.println(tempArray.size());

        for (EffectLabel label : tempArray) {
            if (label.checkLabel()) {

                String message = (label.runEffect()).message;
                if (!message.equals("")) {
                    Printer.print(message);
                }

                if (label.label == 1) {
                    String nextPhaseName = goToNextPhase(gameData);
                    new DataFromGameRun(nextPhaseName);
                }

                return true;
            }
        }
        return false;
    }

    private static void handleActivatingSpellByRival(GameData gameData) {

        if (canRivalActivateSpell(gameData)) {
            new ActivateSpeedEffect(gameData).run();
        }
    }

    private static boolean canRivalActivateSpell(GameData gameData) {
        for (SpellAndTraps card :
                gameData.getNextTurnOwner().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if (card instanceof SpeedEffectTrap) {
                if (card.canActivate(gameData)) {
                    return true;
                }
            }
        }
        return false;
    }


    //test

    public static String getAtkDef(GameData gameData) {

        Card card = gameData.getSelectedCard();
        try {
            return ((Monster) card).getAttack(gameData) + "|" + ((Monster) card).getDefence(gameData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
