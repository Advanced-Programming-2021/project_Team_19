package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.Utils;
import model.Deck;
import model.User;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

public class DeckModifierBetweenGames {

    private final Deck deck;
    private final int initialMainDeckSize;
    private int cardsToMove = 0;
    private final User user;


    public DeckModifierBetweenGames(User user) {
        deck = DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName());
        this.user = user;
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

            if (command.matches("finish")) {
                if (canEndModification())
                    break;
            } else if (command.matches("--(main|side) \\d+")) {
                moveCard(Utils.getMatcher(command, "--(.+) (\\d+)"));
            } else {
                Printer.printInvalidCommand();
            }
        }

        DeckDataBaseController.changeDeck(user.getUsername(), deck);

    }

    private void moveCard(Matcher matcher) {
        matcher.find();
        if (matcher.group(1).equals("main")) {
            moveFromMainDeckToSideDeckCommand(Integer.parseInt(matcher.group(2)));
            return;
        }
        moveFromSideDeckToMainDeckCommand(Integer.parseInt(matcher.group(2)));
    }

    private void moveFromSideDeckToMainDeckCommand(int id) {
        if (id > deck.getSideDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return;
        }

        String current = getCardOfIndexI(id, deck.getSideDeckCards());

        cardsToMove += 1;

        deck.getSideDeckCards().remove(current);
        deck.getMainDeckCards().add(current);

    }

    private void moveFromMainDeckToSideDeckCommand(int id) {
        if (id > deck.getMainDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return;
        }

        String current = getCardOfIndexI(id, deck.getMainDeckCards());

        cardsToMove -= 1;

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

        if (cardsToMove > 0) {
            Printer.print("you have to move " + cardsToMove + " more cards from your main deck to your side deck");
        } else {
            Printer.print("you have to move " + -cardsToMove + " more cards from your side deck to your main deck");
        }
        return false;
    }
}
