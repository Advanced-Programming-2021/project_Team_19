package model;


import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;

import java.util.ArrayList;
import java.util.TreeSet;

public class Deck {
    private final String name;
    private final ArrayList<String> mainDeckCards = new ArrayList<>();
    private final ArrayList<String> sideDeckCards = new ArrayList<>();

    public Deck(String name) {
        this.name = name;
    }

    public static Deck gsonToDeck(String gson) {
        return null;
    }

    public void addCardToMainDeck(String cardName) {
        mainDeckCards.add(cardName);
    }

    public void addCardToSideDeck(String cardName) {
        sideDeckCards.add(cardName);
    }

    public ArrayList<String> getMainDeckCards() {
        return mainDeckCards;
    }

    public ArrayList<String> getSideDeckCards() {
        return sideDeckCards;
    }

    public boolean isDeckValid() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void removeCardFromSideDeck(String cardName) {
        sideDeckCards.remove(cardName);
    }

    public void removeCardFromMainDeck(String cardName) {
        mainDeckCards.remove(cardName);
    }


    public boolean isSideDeckFull() {
        return sideDeckCards.size() >= 15;
    }

    public boolean isMainDeckFull() {
        return mainDeckCards.size() >= 60;
    }

    public ArrayList<String> getAllCard() {
        ArrayList<String> allCardNames = new ArrayList<>();
        allCardNames.addAll(mainDeckCards);
        allCardNames.addAll(sideDeckCards);
        return allCardNames;
    }

    public TreeSet<Card> getAllCardsSorted() {
        TreeSet<Card> allCards = new TreeSet<>(new Card.CardComp());
        for (String cardName : mainDeckCards) {
            allCards.add(Utils.getCardByName(cardName));
        }
        for (String cardName : sideDeckCards) {
            allCards.add(Utils.getCardByName(cardName));
        }
        return allCards;
    }

    public TreeSet<Card> getAllMainCardsSorted() {
        TreeSet<Card> allCards = new TreeSet<>(new Card.CardComp());
        for (String cardName : mainDeckCards) {
            allCards.add(Utils.getCardByName(cardName));
        }
        return allCards;
    }

    public TreeSet<Card> getAllSideCardsSorted() {
        TreeSet<Card> allCards = new TreeSet<>(new Card.CardComp());
        for (String cardName : sideDeckCards) {
            allCards.add(Utils.getCardByName(cardName));
        }
        return allCards;
    }

    private boolean canAddThisCard(Card card) {
        return true;
    }

    public boolean isThereThreeCardsInDeck(String CardName) {
        int cnt = 0;
        for (String cardName : getAllCard()) {
            if (cardName.equals(CardName)) {
                cnt++;
            }
        }
        return cnt >= 3;
    }

    @Override
    public String toString() {
        String temp = name + ": main deck " + mainDeckCards.size() + ", side deck " +
                sideDeckCards.size() + ", ";
        if (isDeckValid()) {
            return temp + "valid\n";
        } else {
            return temp + "invalid\n";
        }
    }

    public String detailedToStringMain() {
        StringBuilder detailedToString = new StringBuilder();
        detailedToString.append("Deck: ").append(name).append("\n");
        detailedToString.append("Main deck:\n");
        detailedToString.append("Monsters:\n");
        for (Card card : getAllMainCardsSorted()) {
            if (card instanceof Monster) {
                detailedToString.append(card).append("\n");
            }
        }
        detailedToString.append("Spell And Traps:\n");
        for (Card card : getAllMainCardsSorted()) {
            if (card instanceof SpellAndTraps) {
                detailedToString.append(card).append("\n");
            }
        }
        return detailedToString.toString();
    }

    public String detailedToStringSide() {
        StringBuilder detailedToString = new StringBuilder();
        detailedToString.append("Deck: ").append(name).append("\n");
        detailedToString.append("Side deck:\n");
        detailedToString.append("Monsters:\n");
        for (Card card : getAllSideCardsSorted()) {
            if (card instanceof Monster) {
                detailedToString.append(card).append("\n");
            }
        }
        detailedToString.append("Spell And Traps:\n");
        for (Card card : getAllSideCardsSorted()) {
            if (card instanceof SpellAndTraps) {
                detailedToString.append(card).append("\n");
            }
        }
        return detailedToString.toString();
    }

    public void showDeckForModifying() {
        TreeSet<Card> cards;
        for (int j = 0; j < 2; j++) {
            int i = 1;
            if (j == 0) {
                System.out.println("main deck:");
                cards = getAllMainCardsSorted();
            } else {
                System.out.println("side deck:");
                cards = getAllSideCardsSorted();
            }

            for (Card card : cards) {
                System.out.print(i + "." + card.getCardFamily() + " " + card.getName());
                if ((i++) % 3 == 0) {
                    System.out.println();
                } else {
                    System.out.print(" ".repeat(Math.max(0, 50 - (i + "." + card.getCardFamily() + " " + card.getName()).length())));
                }
            }
            System.out.println();
        }
    }


}



