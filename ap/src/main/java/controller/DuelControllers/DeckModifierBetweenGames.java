package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.Utils;
import model.Deck;
import model.Enums.CardNames;
import model.Gamer;
import model.User;
import view.GetInput;
import view.Printer.Printer;

import java.util.Iterator;
import java.util.regex.Matcher;

public class DeckModifierBetweenGames {

    private Deck deck;
    private int initialMainDeckSize;
    private Gamer gamer;

    public DeckModifierBetweenGames(User user) {
        deck = DeckDataBaseController.getDeckByName(user.getActiveDeckName());
        initialMainDeckSize = deck.getMainDeckCards().size();
    }

    public void run() {
        String command;
        while (true) {
            deck.showDeckForModifying();
            Printer.print("""
                    select an id to move that card:
                    format: --main <id>      --side <id>
                    enter finish when you are done""");
            command = GetInput.getString();

            if (command.matches("finish") && canEndModification()) {
                break;
            } else if (command.matches("--(main|side) \\d+")) {
                moveCard(Utils.getMatcher(command, "--(.+) (\\d+)"));
            } else {
                Printer.printInvalidCommand();
            }
        }

        DeckDataBaseController.changeDeck(gamer.getUsername(), deck);

    }

    private boolean wantsToSaveChanges() {
        while (true) {
            Printer.print("""
                    do you want to save the changes you made to your deck:
                    1- yes
                    2- no""");
            String command = GetInput.getString();
            switch (command) {
                case "1":
                    return true;
                case "2":
                    return false;
                default:
                    Printer.printInvalidCommand();
            }
        }
    }

    private void moveCard(Matcher matcher) {
        matcher.find();
        if (matcher.group(1).equals("main")) {
            moveFromMainDeckToSideDeckCommand(Integer.parseInt(matcher.group(2)));
            return;
        }
        moveFromSideDeckToMainDeckCommand(Integer.parseInt(matcher.group(2)));
        Printer.print("card successfully moved");
    }

    private void moveFromSideDeckToMainDeckCommand(int id) {
        if (id > deck.getSideDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return;
        }
        Iterator<String> it = deck.getSideDeckCards().iterator();
        int i = 0;
        String current = null;
        while (it.hasNext()) {
            i++;
            current = it.next();
            if(i==id){
                break;
            }
        }

        deck.getSideDeckCards().remove(current);
        deck.getMainDeckCards().add(current);

    }

    private void moveFromMainDeckToSideDeckCommand(int id) {
        if (id > deck.getMainDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return;
        }

        Iterator<String> it = deck.getSideDeckCards().iterator();
        int i = 0;
        String current = null;
        while (it.hasNext()) {
            i++;
            current = it.next();
            if(id==i){
                break;
            }
        }

        deck.getMainDeckCards().remove(current);
        deck.getSideDeckCards().add(current);
    }

    private boolean canEndModification() {
        if (deck.getMainDeckCards().size() == initialMainDeckSize)
            return true;
        int difference = deck.getMainDeckCards().size() - initialMainDeckSize;

        if (difference > 0) {
            Printer.print("you have to move " + difference / 2 + " more cards from your main deck to your side deck");
        } else {
            Printer.print("you have to move " + -difference / 2 + " more cards from your side deck to your main deck");
        }
        return false;
    }
}
