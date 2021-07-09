package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.Utils;
import model.Card.Card;
import model.Deck;
import model.User;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Matcher;

public class DeckModifierBetweenGames {

    private final Deck deck;
    private int cardsToMove = 0;
    private final User user;


    public DeckModifierBetweenGames(User user) {
        deck = DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName());
        this.user = user;
    }

    @Deprecated
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

    public String runForGraphic(String command) {
        if (command.matches("finish")) {
            return canEndModificationGraphic();
        } else if (command.matches("--(main|side) \\d+")) {
            return moveCard(Utils.getMatcher(command, "--(.+) (\\d+)"));
        } else {
            Printer.printInvalidCommand();
        }
        DeckDataBaseController.changeDeck(user.getUsername(), deck);
        return null;
    }

    private String moveCard(Matcher matcher) {
        matcher.find();
        if (matcher.group(1).equals("main")) {
            return moveFromMainDeckToSideDeckCommand(Integer.parseInt(matcher.group(2)));
        }
        return moveFromSideDeckToMainDeckCommand(Integer.parseInt(matcher.group(2)));
    }

    private String moveFromSideDeckToMainDeckCommand(int id) {
        if (id > deck.getSideDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return "invalid id";
        }

        Card current = getCardOfIndexI(id, deck.getAllSideCardsSorted());
        String cardName = current.getName();

        cardsToMove += 1;

        deck.removeCardFromSideDeck(cardName);
        deck.addCardToMainDeck(cardName);
        Printer.print("successful");
        DeckDataBaseController.changeDeck(user.getUsername(), deck);
        return "success";
    }

    private String moveFromMainDeckToSideDeckCommand(int id) {
        if (id > deck.getMainDeckCards().size() || id <= 0) {
            Printer.print("invalid id");
            return "invalid id";
        }

        Card current = getCardOfIndexI(id, deck.getAllMainCardsSorted());
        String cardName = current.getName();

        cardsToMove -= 1;

        deck.removeCardFromMainDeck(cardName);
        deck.addCardToSideDeck(cardName);
        Printer.print("success");
        DeckDataBaseController.changeDeck(user.getUsername(), deck);
        return "success";
    }

    private Card getCardOfIndexI(int index, TreeSet<Card> cards) {
        Iterator<Card> it = cards.iterator();
        int i = 0;
        Card current = null;
        while (it.hasNext() && i < index){
            current = it.next();
            i++;
        }
        return current;
    }

    @Deprecated
    private boolean canEndModification() {
        if (cardsToMove == 0)
            return true;

        if (cardsToMove > 0) {
            Printer.print("you have to move " + cardsToMove + " more cards from your main deck to your side deck");
        } else {
            Printer.print("you have to move " + -cardsToMove + " more cards from your side deck to your main deck");
        }
        return false;
    }

    private String canEndModificationGraphic() {
        if (cardsToMove == 0)
            return null;

        if (cardsToMove > 0) {
            return "you have to move " + cardsToMove + " more cards from your main deck to your side deck";
        } else {
            return "you have to move " + -cardsToMove + " more cards from your side deck to your main deck";
        }
    }

}
