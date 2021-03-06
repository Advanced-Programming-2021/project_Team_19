package model.Board;

import model.Card.Card;
import view.Printer.Printer;

import java.util.ArrayList;

public class Hand extends Zones {
    ArrayList<Card> cardsInHand = new ArrayList<>();

    public ArrayList<Card> getCardsInHand() {
        return cardsInHand;
    }

    public Card getCard(int id) {
        return cardsInHand.get(id - 1);
    }

    public Card removeCard(int id) {
        Card temp = getCard(id);
        cardsInHand.remove(id - 1);
        return temp;
    }

    public void addCard(Card card) {
        cardsInHand.add(card);
    }

    public int getId(Card card) {
        if (cardsInHand.contains(card)) {
            return cardsInHand.indexOf(card) + 1;
        } else {
            return -1;
        }
    }

    public String selfToString() {
        return "c\t".repeat(cardsInHand.size());
    }

    public String rivalToString() {
        return "\tc".repeat(cardsInHand.size());
    }

    public int getSize() {
        return cardsInHand.size();
    }

    public void showHand() {
        for (Card card : cardsInHand) {
            Printer.print(getId(card) + "- " + card.toString());
        }
    }


}
