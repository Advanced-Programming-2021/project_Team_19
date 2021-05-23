package model.Board;

import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;

import java.util.ArrayList;
import java.util.Collections;

public class MonsterCardZone extends Zones {

    private ArrayList<Monster> cardsInMonsterZone = new ArrayList<>();

    public MonsterCardZone(){
        for (int i = 0; i < 5; i++) {
            cardsInMonsterZone.add(null);
        }
    }

    public ArrayList<Monster> getCards(){
        return cardsInMonsterZone;
    }

    public Card getCardById(int id) {
        return cardsInMonsterZone.get(hashNumber(id));
    }

    public Card removeCard(int id) {
        Card temp=cardsInMonsterZone.get(hashNumber(id));
        cardsInMonsterZone.set(hashNumber(id),null);
        return temp;
    }

    public void removeAllCards(){
        for(int i = 0; i < 5; i++){
            removeCard(i);
        }
    }

    public void addCard(Card card) {
        for(int i=1;i<=5;i++){
            if(cardsInMonsterZone.get(hashNumber(i))==null){
                cardsInMonsterZone.set(hashNumber(i),(Monster)card);
                break;
            }
        }
    }

    public boolean containsCard(Card card){
        return cardsInMonsterZone.contains(card);
    }


    public int getNumberOfCards(){
        int toReturn = 0;

        for (Monster monster : cardsInMonsterZone) {
            if (monster != null)
                toReturn++;
        }

        return toReturn;
    }

    public boolean isZoneFull(){
        for(Card card : cardsInMonsterZone){
            if(card == null){
                return false;
            }
        }
        return true;
    }

    public int getId(Card card){
        if(cardsInMonsterZone.contains(card)){
            int index = cardsInMonsterZone.indexOf(card);
            for(int i=1;i<=5;i++){
                if(hashNumber(i) == index){
                    return i;
                }
            }
        }
        return -1;
    }


    public String getStringForSelf(){

        StringBuilder answer= new StringBuilder();

        for(String appendingStr : getPrintingStringsForToStringMethod()){
            answer.append(appendingStr);
        }
        return answer.toString();
    }

    public String getStringForRival(){
        StringBuilder answer= new StringBuilder();

        ArrayList<String> printingStrings = getPrintingStringsForToStringMethod();
        Collections.reverse(printingStrings);

        for(String appendingStr : printingStrings){
            answer.append(appendingStr);
        }
        return answer.toString();
    }

    private ArrayList<String> getPrintingStringsForToStringMethod() {

        ArrayList<String> returnedArrayList = new ArrayList<>();

        returnedArrayList.add("\t");

        for (int i = 0; i < 5; i++) {

            if (cardsInMonsterZone.get(i) == null) {
                returnedArrayList.add("E ");

            } else if (cardsInMonsterZone.get(i).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                returnedArrayList.add("DH");

            } else if (cardsInMonsterZone.get(i).getCardMod().equals(CardMod.DEFENSIVE_OCCUPIED)) {
                returnedArrayList.add("DO");

            } else {
                returnedArrayList.add("OO");
            }

            returnedArrayList.add("\t");

        }

        return returnedArrayList;
    }


    public static MonsterCardZone getTestZone(){

        MonsterCardZone monsterCardZone = new MonsterCardZone();

        Monster tempMonster = (Monster) Utils.getCardByName("mirage dragon");
        tempMonster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        monsterCardZone.addCard(tempMonster);

        tempMonster = (Monster) Utils.getCardByName("battle OX");
        tempMonster.setCardMod(CardMod.DEFENSIVE_HIDDEN);
        monsterCardZone.addCard(tempMonster);

        tempMonster = (Monster) Utils.getCardByName("yomi ship");
        tempMonster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        monsterCardZone.addCard(tempMonster);

        tempMonster = (Monster) Utils.getCardByName("Silver Fang");
        tempMonster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        monsterCardZone.addCard(tempMonster);

//        tempMonster = (Monster) Utils.getCardByName("Battle OX");
//        tempMonster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
//        monsterCardZone.addCard(tempMonster);

        return monsterCardZone;
    }




}
