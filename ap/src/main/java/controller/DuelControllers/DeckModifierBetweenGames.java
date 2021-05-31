package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.Utils;
import model.Deck;
import model.Gamer;
import model.User;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

public class DeckModifierBetweenGames {

    private final Deck deck;
    private final int initialMainDeckSize;
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

        String current = getCardOfIndexI(id, deck.getSideDeckCards());

        deck.getSideDeckCards().remove(current);
        deck.getMainDeckCards().add(current);

    }

    private void moveFromMainDeckToSideDeckCommand(int id) {
        if (id > deck.getMainDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return;
        }

        String current = getCardOfIndexI(id, deck.getMainDeckCards());

        deck.getMainDeckCards().remove(current);
        deck.getSideDeckCards().add(current);
    }

    private String getCardOfIndexI(int index, ArrayList<String> cards) {
        Iterator<String> it = cards.iterator();
        int i = 0;
        String current = null;
        while (it.hasNext()) {
            i++;
            current = it.next();
            if (index == i) {
                break;
            }
        }
        return current;
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
