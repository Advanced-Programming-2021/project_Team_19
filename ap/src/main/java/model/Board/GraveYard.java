package model.Board;

import model.Card.Card;
import model.Card.Monster;

import java.util.ArrayList;

public class GraveYard extends Zones {
    private ArrayList<Card> cardsInGraveYard = new ArrayList<>();

    public Card getCard(int id) {
        return cardsInGraveYard.get(id);
    }

    public Card removeCard(int id) {
        return cardsInGraveYard.remove(id);
    }

    public int getId(Card card){
        if(cardsInGraveYard.contains(card)){
            return cardsInGraveYard.indexOf(card);
        }
        else{
            return -1;
        }
    }

    public void addCard(Card card) {

    }

    public int getSize() {
        return cardsInGraveYard.size();
    }

    public ArrayList<Card> getCardsInGraveYard(){
        return cardsInGraveYard;
    }


    public String printGraveYard(){
        if(cardsInGraveYard.isEmpty()){
            return "graveyard empty";
        }
        else{
            StringBuilder stringBuilder=new StringBuilder();
            for(Card card:cardsInGraveYard){
                stringBuilder.append(card.toString()).append("\n");
            }
            return stringBuilder.toString().trim();
        }
    }
}
