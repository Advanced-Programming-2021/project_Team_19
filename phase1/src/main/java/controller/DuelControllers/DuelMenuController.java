package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.DataBaseControllers.UserDataBaseController;
import controller.Utils;
import model.Enums.GameEvent;
import model.Gamer;
import model.User;
import view.GetInput;
import view.Menu.Menu;
import view.Printer.Printer;

import java.util.regex.Matcher;

public class DuelMenuController extends Menu {

    private User user;
    private Gamer gameStarter;
    private Gamer rivalGamer;
    private static boolean gameIsHappening = false;

    public DuelMenuController() {
        super("Duel Menu");
    }

    public void run(String username) {

        this.user = UserDataBaseController.getUserByUsername(username);
        String command;

        while (true) {

            command = GetInput.getString();

            if (command.equals("menu exit")) {
                break;
            } else if (command.startsWith("Menu")) {
                handleMenuOrders(command);
            } else if (command.startsWith("duel --new")) {
                startDuel(command);
            } else if (command.matches("help")) {
                help();
            } else {
                Printer.printInvalidCommand();
            }
        }

    }

    private void help() {
        System.out.println("""
                duel --new --ai --rounds <1|3>
                duel --new --second-player <second player username> --rounds <1|3>
                help
                menu show-current
                menu enter [menu name]
                menu exit""");
    }


    public void startDuel(String command) {

        if (command.matches("duel --new --ai --rounds (\\d)")) {
            startDuelWithAi(Utils.getMatcher(command, "duel --new --ai --rounds (\\d)"));
        } else if (command.matches("duel --new --second-player (\\S+) --rounds (\\d)")) {
            startDuelWithTowPlayer(Utils.getMatcher(command, "duel --new --second-player (\\S+) --rounds (\\d)"));
        } else {
            Printer.printInvalidCommand();
        }
    }

    private void startDuelWithAi(Matcher matcher) {

        matcher.matches();

        int rounds = Integer.parseInt(matcher.group(1));

        if (user.getActiveDeckName() == null) {
            Printer.print(user.getUsername() + " has no active deck");
        } else if (!DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName()).isDeckValid()) {
            Printer.print(user.getUsername() + "’s deck is invalid");
        } else if (rounds != 1 && rounds != 3) {
            Printer.print("number of rounds is not supported");
        } else {
            gameStarter = new Gamer(user);
            rivalGamer = AI.getGamer(0);
            handleDuel(rounds);
        }

    }

    private void startDuelWithTowPlayer(Matcher matcher) {

        matcher.find();

        User rival = UserDataBaseController.getUserByUsername(matcher.group(1));
        int rounds = Integer.parseInt(matcher.group(2));
        if (rival == null) {
            Printer.print("there is no player with this username");
        } else if (user.getActiveDeckName() == null) {
            Printer.print(user.getUsername() + " has no active deck");
        } else if (rival.getActiveDeckName() == null) {
            Printer.print(rival.getUsername() + " has no active deck");
        } else if (!DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName()).isDeckValid()) {
            Printer.print(user.getUsername() + "’s deck is invalid");
        } else if (!DeckDataBaseController.getDeckByName(rival.getUsername() + "_" + rival.getActiveDeckName()).isDeckValid()) {
            Printer.print(rival.getUsername() + "’s deck is invalid");
        } else if (rounds != 1 && rounds != 3) {
            Printer.print("number of rounds is not supported");
        } else {
            gameStarter = new Gamer(user);
            rivalGamer = new Gamer(rival);
            handleDuel(rounds);
        }

    }


    private void handleDuel(int rounds) {
        gameIsHappening = true;
        if (rounds == 1) {
            GameData gameData = new GameData(gameStarter, rivalGamer);
            finishDuel(new Game().run(gameData), gameData, 1);
        } else {
            int userWins = 0;
            int rivalWins = 0;
            GameData gameData = new GameData(gameStarter, rivalGamer);
            while (true) {
                if ((new Game().run(gameData)).equals(gameStarter))
                    userWins++;
                else
                    rivalWins++;
                if (userWins == 2 || rivalWins == 2)
                    break;
                changeDecks();
                gameData = new GameData(gameStarter, rivalGamer);
            }
            if (userWins == 2) {
                finishDuel(gameStarter, gameData, 3);
            } else {
                finishDuel(rivalGamer, gameData, 3);
            }
        }
    }

    private void changeDecks() {

        GameData gameData = GameData.getGameData();

        gameData.setEvent(GameEvent.ASK_FOR_SIDE_DECK);

        if (!gameData.getCurrentGamer().equals(gameStarter)){
            gameData.changeTurn();
        }

        if (Utils.askForConfirmation(gameStarter.getUsername() + ", do you want to swap cards between your main deck and side deck?"))
            new DeckModifierBetweenGames(gameStarter.getUser()).run();

        gameData.changeTurn();

        if (Utils.askForConfirmation(rivalGamer.getUsername() + ", do you want to swap cards between your main deck and side deck?"))
            new DeckModifierBetweenGames(rivalGamer.getUser()).run();


        GameData.getGameData().setEvent(null);
    }

    private void finishDuel(Gamer winner, GameData gameData, int rounds) {
        gameIsHappening = false;
        Gamer loser = gameData.getCurrentGamer();
        if (loser.equals(winner))
            loser = gameData.getSecondGamer();

        Printer.print(winner.getUsername() + " won the whole match with score: " +
                gameData.getGameStarter().getCurrentScoreInDuel() + " - " +
                gameData.getInvitedGamer().getCurrentScoreInDuel());
        increaseCreditAndScoreAfterGame(winner, loser, rounds);
    }

    public static boolean isGameIsHappening() {
        return gameIsHappening;
    }

    private void increaseCreditAndScoreAfterGame(Gamer winner, Gamer loser, int rounds) {
        winner.increaseCredit(1000 * rounds + winner.getMaxLifePointsInDuel());
        loser.increaseCredit(1000 * rounds);
        winner.increaseUserScore(1000 * rounds);
        UserDataBaseController.saveChanges(winner.getUser());
        UserDataBaseController.saveChanges(loser.getUser());
    }

}
