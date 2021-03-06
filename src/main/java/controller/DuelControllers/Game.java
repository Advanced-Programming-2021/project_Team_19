package controller.DuelControllers;

import AnythingIWant.GameGraphicController;
import AnythingIWant.Network;
import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.Actions.*;
import controller.DuelControllers.Phases.DrawPhase;
import controller.DuelControllers.Phases.StandbyPhase;
import controller.Utils;
import model.*;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Traps.SpeedEffectTrap;
import model.Data.DataForClientFromServer;
import model.Data.TriggerActivationData;
import model.Enums.GameEvent;
import view.Printer.Printer;

import java.util.ArrayList;


public class Game {

    public GameData gameData;
    public Card multiActionCard;
    public CardActionManager manager;
    public GameGraphicController gameController;
    public int round;

    public Game(GameData gameData, int round, GameGraphicController gameGraphicController) {
        this.gameData = gameData;
        this.round = round;
        this.gameController = gameGraphicController;
        this.manager = new CardActionManager(gameData);
    }

    private Gamer getGamerByUser(User user){
        return gameData.getInvitedGamer().getUser().getUsername().equals(user.getUsername()) ?
                gameData.getInvitedGamer() : gameData.getGameStarter();
    }

    public synchronized DataForClientFromServer run(String command, User user, DataForGameRun dataForGameRun) {
        gameData.resetDataFromGameRuns();
        gameData.dataSender = gameData.getGamerByUsername(user.getUsername());
        Gamer gamer = getGamerByUser(user);
        System.out.println("\n" + command + "\n");
        switch (command) {
            case "activate trap" -> {
                TriggerLabel label = gameData.triggerLabel;
                TriggerActivationData data = (TriggerActivationData) new ActivateTriggerTrapEffect
                        (label.action).handleActivate();
                String message = data.message;
                String[] messages = message.split(":");
                new DataFromGameRun(gameData, message);
                if (messages[1].equals("change turn")) {
                    gameData.changeTurn();
                }
                label.inProgress = false;
                label.shouldRunAgain = label.shouldRunAgain && !data.hasActionStopped;
            }
            case "set trigger trap mode" -> {
                manager.setMode(actionManagerMode.TRIGGER_TRAP_MODE);
            }
            case "cancel activate trap" -> {
                gameData.triggerLabel.inProgress = false;
                if (!gameData.getCurrentGamer().equals(gameData.getTurnOwner())) {
                    gameData.changeTurn();
                }
            }
            case "start game" -> {
                runServerSideGameEvents();
            }
            case "set" -> {
                new DataFromGameRun(gameData, new Set(gameData).run(null));
            }
            case "get init hand data" -> {
                ArrayList<String> cards = new ArrayList<>();
                ArrayList<String> rivalCards = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    cards.add(gamer.getGameBoard().getHand().getCardsInHand().get(i).getName());
                    rivalCards.add(gameData.getOtherGamer
                            (gamer).getGameBoard().getHand().getCardsInHand().get(i).getName());
                }
                DataFromGameRun data = new DataFromGameRun(gameData, "init hand");
                data.cardNames.addAll(cards);
                data.cardNames.addAll(rivalCards);
                return new DataForClientFromServer(gameData.dataFromGameRuns);
            }
            case "set with sacrifice" -> {
                multiActionCard = gameData.getSelectedCard();
                manager.setMode(actionManagerMode.SET_MODE);
                new DataFromGameRun(gameData, new Set(gameData).actionIsValid());
            }
            case "normal summon" -> {
                new DataFromGameRun(gameData, new NormalSummon(gameData).run(null));
            }
            case "summon with sacrifice" -> {
                multiActionCard = gameData.getSelectedCard();
                manager.setMode(actionManagerMode.SUMMON_MODE);
                new DataFromGameRun(gameData, new NormalSummon(gameData).actionIsValid());
            }
            case "attack direct" -> {
                new DirectAttack(gameData).run(true);
            }
            case "activate spell" -> {
                new DataFromGameRun(gameData, new ActivateSpellOrTrapNormally(gameData).run());
            }
            case "attack monster" -> {
                multiActionCard = gameData.getSelectedCard();
                manager.setMode(actionManagerMode.ATTACK_MONSTER_MODE);
                new DataFromGameRun(gameData, new AttackMonster(gameData, 0).actionIsValid());
            }
            case "flip summon" -> {
                new DataFromGameRun(gameData, new FlipSummon(gameData).run());
            }
            case "get Atk|Def" -> {
                new DataFromGameRun(gameData, getAtkDef(gameData));
            }
            case "get valid actions" -> {
                new DataFromGameRun(gameData, "valid actions", getValidCommandsForCard(dataForGameRun));
            }
            case "next phase" -> {
                if (user.getUsername().equals(gameData.getCurrentGamer().getUsername())){
                    if(gameData.getCurrentPhase().equals(Phase.END) ||
                            gameData.getCurrentPhase().equals(Phase.DRAW) ||
                            gameData.getCurrentPhase().equals(Phase.STANDBY)){

                    } else {
                        String nextPhaseName = goToNextPhase(gameData);
                        new DataFromGameRun(gameData, nextPhaseName);
                    }
                }
            }
        }
        if (command.matches("sacrifice \\d( \\d)*")) {
            gameData.setSelectedCard(multiActionCard);
            String[] summonOrSetResponse = new String[2];
            if (manager.mode.equals(actionManagerMode.SUMMON_MODE)) {
                summonOrSetResponse = new NormalSummon(gameData).run(command.substring(10));
            } else if (manager.mode.equals(actionManagerMode.SET_MODE)) {
                summonOrSetResponse = new Set(gameData).run(command.substring(10));
            }
            manager.setMode(actionManagerMode.NORMAL_MODE);
            new DataFromGameRun(gameData, summonOrSetResponse);
        } else if (command.matches("attack \\d")) {
            gameData.setSelectedCard(multiActionCard);
            new AttackMonster(gameData, Integer.parseInt(command.substring(7))).run(true);
        } else if (command.matches("set position (attack|defence)")) {
            new DataFromGameRun(gameData, new SetPosition(gameData).run(Utils.getMatcher(command, "set position (.*)")));
        } else if (command.matches("game button @\\d+@")) {
            String lpIncrease = command.split(" ")[2].replace("@", "");
            CheatCodes.increaseLifePoint(gameData, lpIncrease);
            new DataFromGameRun(gameData, "increase lp " + lpIncrease);
        }


        runServerSideGameEvents();

        return new DataForClientFromServer(gameData.dataFromGameRuns);
    }


    public ArrayList<String> getValidCommandsForCard(DataForGameRun data) {
        if (gameData.getCurrentGamer().getUsername().equals(Network.getUserByToken(data.token).getUsername())) {
            System.err.println(data.getZoneName());
            gameData.setSelectedCard(getCardByZoneAndId(data.getZoneName(), data.getId()));
            return (manager.getValidActions());
        }
        return new ArrayList<>();
    }

    public Card getCardByZoneAndId(String zoneName, int Id) {
        return switch (zoneName) {
            case ("Hand") -> gameData.getCurrentGamer().getGameBoard().getHand().getCard(Id);
            case ("Monster Card Zone") -> gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards().get(Id);
            case ("Spell And Trap Zone") -> gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards().get(Id);
            case ("Graveyard") -> gameData.getCurrentGamer().getGameBoard().getGraveYard().getCard(Id);
            case ("Field Zone") -> gameData.getCurrentGamer().getGameBoard().getFieldZone().getCard();
            case ("Hand Opponent") -> gameData.getSecondGamer().getGameBoard().getHand().getCard(Id);
            case ("Monster Card Zone Opponent") -> gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards().get(Id);
            case ("Spell And Trap Zone Opponent") -> gameData.getSecondGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards().get(Id);
            default -> throw new IllegalStateException("Unexpected value: " + zoneName);
        };
    }

    public void checkTriggerData() {

        if (gameData.triggerLabel == null) {
            return;
        }

        if (gameData.triggerLabel.inProgress) {
            return;
        }

        Action action = gameData.triggerLabel.action;

        gameData.addActionToCurrentActions(action);
        gameData.setActionIndexForTriggerActivation(gameData.getCurrentActions().indexOf(action));

        if (action.canTurnOwnerActivateTrapBecauseOfAnAction() &&
                gameData.triggerLabel.shouldAskFromFirstGamer) {
            gameData.triggerLabel.inProgress = true;
            new DataFromGameRun(gameData, "ask gamer for trap:" + action.getActionName());
        } else {
            if (action.canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction() &&
                    gameData.triggerLabel.shouldAskFromSecondGamer) {
                gameData.triggerLabel.inProgress = true;
                gameData.changeTurn();
                new DataFromGameRun(gameData, "ask gamer for trap:" + action.getActionName());
            }
            gameData.triggerLabel.shouldAskFromSecondGamer = false;
        }
        gameData.triggerLabel.shouldAskFromFirstGamer = false;

        if (!gameData.triggerLabel.shouldAskFromFirstGamer
                && !gameData.triggerLabel.shouldAskFromSecondGamer
                && !gameData.triggerLabel.inProgress) {
            if (gameData.triggerLabel.shouldRunAgain) {
                String[] data = ((Attack) gameData.triggerLabel.action).run(false);
                new DataFromGameRun(gameData, data);
            }
            gameData.removeActionFromCurrentActions(gameData.triggerLabel.action);
            gameData.triggerLabel = null;
            gameData.setActionIndexForTriggerActivation(-1);
            manager.setMode(actionManagerMode.NORMAL_MODE);
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

                    if (Utils.askForActivate("It's " + gameData.getCurrentPhase() + " phase", gameData)) {
                        handleActivatingSpellByRival(gameData);
                    }

                    Utils.changeTurn(gameData);
                }
                gameData.hasAskedForSpellsThisPhase = true;
            }


            if (gameData.isGameOver()) {
                new DataFromGameRun(gameData, "game finished " + finishGame(gameData).getUsername());
            }

            if (gameData.getCurrentPhase().equals(Phase.DRAW)) {
                Card card = new DrawPhase().run(gameData);
                goToNextPhase(gameData);
                new DataFromGameRun(gameData, "add card to hand &" +
                        gameData.getCurrentGamer().getUsername(), card.getName());
                new DataFromGameRun(gameData, "draw phase");
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.STANDBY)) {
                new StandbyPhase().run(gameData);
                goToNextPhase(gameData);
                new DataFromGameRun(gameData, "stand by phase");
                continue;
            }
            if (gameData.getCurrentPhase().equals(Phase.END)) {
                gameData.setSelectedCard(null);
                new DataFromGameRun(gameData, "end phase");
                gameData.turnFinished();
                goToNextPhase(gameData);
                continue;
            }

            gameData.setEvent(GameEvent.NORMAL);
            break;
        }
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


        for (EffectLabel label : tempArray) {
            if (label.checkLabel()) {

                String message = (label.runEffect()).message;
                if (!message.equals("")) {
                    Printer.print(message);
                }

                if (label.label == 1) {
                    String nextPhaseName = goToNextPhase(gameData);
                    new DataFromGameRun(gameData, nextPhaseName);
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
