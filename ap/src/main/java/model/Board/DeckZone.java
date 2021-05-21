package model.Board;

import controller.Utils;
import model.Card.Card;
import model.Deck;

import java.util.ArrayList;

public class DeckZone extends Zones {

    ArrayList<Card> mainDeckCards=new ArrayList<>();
    ArrayList<Card> sideDeckCards=new ArrayList<>();

    public DeckZone(Deck deck){
        for(String cardName:deck.getMainDeckCards()){
            mainDeckCards.add(Utils.getCardByName(cardName));
        }
        for(String cardName:deck.getSideDeckCards()){
            sideDeckCards.add(Utils.getCardByName(cardName));
        }
        shuffleDeck();
    }

    public void shuffleDeck(){
        Utils.shuffle(this.mainDeckCards);
    }


    public Card getCard(int id) {
        return mainDeckCards.get(id);
    }


    public Card removeCard(int id) {
        Card temp=mainDeckCards.get(id);
        mainDeckCards.remove(temp);
        return temp;
    }

    public void addCard(Card card) {
        mainDeckCards.add(card);
    }

    public int getId(Card card){
        if(mainDeckCards.contains(card)){
            return mainDeckCards.indexOf(card);
        }
        else{
            return -1;
        }
    }

    public int getSize() {
        return mainDeckCards.size();
    }

    public ArrayList<Card> getMainDeckCards(){
        return mainDeckCards;
    }
}
