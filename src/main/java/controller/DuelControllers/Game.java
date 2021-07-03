package controller.DuelControllers;

import controller.DuelControllers.Actions.*;
import controller.DuelControllers.Phases.DrawPhase;
import controller.DuelControllers.Phases.StandbyPhase;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Traps.SpeedEffectTrap;
import model.EffectLabel;
import model.Enums.GameEvent;
import model.Gamer;
import model.Phase;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Locale;

import static controller.DuelControllers.CardActionManager.destroyCurrentActionManager;

public class Game {

    public GameData gameData;


    public Game(GameData gameData) {
        this.gameData = gameData;
    }


    public String run(String command) {
        switch (command) {
            case "set" -> {
                destroyCurrentActionManager();
                return new Set(gameData).run();
            }
            case "normal summon" -> {
                destroyCurrentActionManager();
                return new NormalSummon(gameData).run();
            }
            case "attack direct" -> {
                destroyCurrentActionManager();
                return new DirectAttack(gameData).run();
            }
            case "flip summon" -> {
                destroyCurrentActionManager();
                return new FlipSummon(gameData).run();
            }
            case "next phase" -> {
                destroyCurrentActionManager();
                goToNextPhase(gameData);
                return "phase changed successfully";
            }
            case "select" -> {
                return CardActionManager.getInstance(null).addCardForMultiCardAction();
            }
            default -> {
                return "";
            }
        }
    }


    public ArrayList<String> getValidCommandsForCard(Card card) {

        return CardActionManager.getInstance(card).getValidActions();
    }


    @Deprecated
    public Gamer DeprecatedRun() {

        String command;

        while (true) {

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

    public void goToNextPhase(GameData gameData) {

        gameData.goToNextPhase();
        Printer.print(gameData.getCurrentPhase().getPhaseName());
        gameData.hasAskedForSpellsThisPhase = false;
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
                    goToNextPhase(label.gameData);
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

    public static void showAtkDef(GameData gameData) {

        Card card = gameData.getSelectedCard();
        try {
            System.out.println(((Monster) card).getAttack(gameData));
            System.out.println(((Monster) card).getDefence(gameData));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
