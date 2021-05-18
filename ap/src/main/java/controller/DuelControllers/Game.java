package controller.DuelControllers;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.Phases.DrawPhase;
import controller.DuelControllers.Phases.StandbyPhase;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Gamer;
import model.Phase;
import view.GetInput;
import view.Printer.Printer;

import java.util.Locale;

public class Game {

    public Gamer run(GameData gameData) {

        String command;

        while (true) {

            checkLabels(gameData);

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

                if(c(gameData)){
                    f(gameData);
                }

                continue;
            }

            command = GetInput.getString();

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
            } else if (command.matches("attack ([1-5])")) {
                new AttackMonster(gameData).run
                        (Utils.getMatcher(command, "attack ([1-5])"));
            } else if (command.matches("attack direct")) {
                new DirectAttack(gameData).run();
            } else if (command.startsWith("select")) {
                new Select(gameData).select(command);
            } else if (command.matches("card show --selected")) {
                new Select(gameData).select(command);
            } else if (command.matches("summon")) {
                new NormalSummon(gameData).run();
            } else if (command.matches("set")) {
                normalSetCommand(gameData);
            } else if (command.matches("show hand")) {
                gameData.getCurrentGamer().getGameBoard().getHand().showHand();
            } else if (command.matches("set --position (attack|defence)")) {
                new SetPosition(gameData).run(Utils.getMatcher(command, "set --position (.*)"));
            } else if (command.matches("flip-summon")) {
                new FlipSummon(gameData).flipByCommand();
            } else if (command.matches("next phase")) {
                goToNextPhase(gameData);
            } else if (command.matches("show graveyard")) {
                gameData.getCurrentGamer().getGameBoard().getGraveYard().printGraveYard();
            } else if (command.matches("activate effect")) {
                new ActivateSpellOrTrapNormally(gameData).run();
            } else if (command.matches("help")) {
                help(gameData);
            } else if (command.equals("show board")) {
                gameData.showBoard();
            } else if (command.equals("show AD")) {//attack and defense
                showAtkDef(gameData);
            } else {
                Printer.printInvalidCommand();
            }

        }
    }

    private void normalSetCommand(GameData gameData) {
        if (gameData.getSelectedCard().getName().equals("Gate Guardian")) {
            Printer.print("you cannot set Gate Guardian");
            return;
        }

        new Set(gameData).run();
    }

    private void help(GameData gameData) {
        GameHelp.run(gameData.getCurrentPhase());
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

    public static void goToNextPhase(GameData gameData) {

        gameData.goToNextPhase();
        Printer.print(gameData.getCurrentPhase().getPhaseName());
    }

    private void checkLabels(GameData gameData) {
        checkLabelsOfGamer(gameData.getCurrentGamer());
        checkLabelsOfGamer(gameData.getSecondGamer());
    }

    private void checkLabelsOfGamer(Gamer gamer) {

        for (EffectLabel label : gamer.getEffectLabels()) {
            if (label.checkLabel()) {
                String message = (label.runEffect()).message;
                if (!message.equals("")) {
                    Printer.print(message);
                }
            }
        }
    }

    private void f(GameData gameData) {

        if(c(gameData)){
            new ActivateSpeedEffect(gameData).run();
        }
    }

    private boolean c(GameData gameData){
        for(SpellAndTraps card :
                gameData.getNextTurnOwner().getGameBoard().getSpellAndTrapCardZone().getAllCards()){
            if(card instanceof Trap){
                if(card.canActivate(gameData)){
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
            System.out.println(((Monster) card).getDefence());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
