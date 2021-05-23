package model.Board;

import controller.Utils;
import model.Card.Card;
import view.Printer.Printer;

import java.util.ArrayList;

public class GraveYard extends Zones {
    private ArrayList<Card> cardsInGraveYard = new ArrayList<>();

    public Card getCard(int id) {
        return cardsInGraveYard.get(id - 1);
    }

    public Card removeCard(int id) {
        return cardsInGraveYard.remove(id - 1);
    }

    public int getId(Card card) {
        if (cardsInGraveYard.contains(card)) {
            return cardsInGraveYard.indexOf(card)+1;
        } else {
            return -1;
        }
    }

    public void addCard(Card card) {
        cardsInGraveYard.add(card);
    }

    public int getSize() {
        return cardsInGraveYard.size();
    }

    public ArrayList<Card> getCardsInGraveYard() {
        return cardsInGraveYard;
    }


    public void printGraveYard() {
        if (cardsInGraveYard.isEmpty()) {
            Printer.print("graveyard empty");
        } else {
            Utils.printArrayListOfCards(cardsInGraveYard);
        }
    }

    //test

    public static GraveYard getTestGraveYard(){
        GraveYard graveYard = new GraveYard();
        graveYard.addCard(Utils.getCardByName("baby dragon"));
        return graveYard;
    }

}
