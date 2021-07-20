package controller;

import java.util.*;
import model.Card.*;
import model.*;
import controller.DuelControllers.*;
import model.Enums.CardFamily;

public class DataFromGameRun {
    public static ArrayList<DataFromGameRun> eventDataFromServer = new ArrayList<>();
    public Card card;
    ArrayList<String> cardNames = new ArrayList<>();
    ArrayList<Boolean> areMonsters = new ArrayList<>();
    public String event;
    public Gamer gamerOfAction;
    public int cardId;


    public DataFromGameRun(String event){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        eventDataFromServer.add(this);
    }

    public DataFromGameRun(String[] eventAndId){
        this.cardId = Integer.parseInt(eventAndId[1]);
        this.event = eventAndId[0];
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        eventDataFromServer.add(this);
    }

    public DataFromGameRun(String event, Card card){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        this.card = card;
        cardNames.add(card.getName());
        areMonsters.add(card.getCardFamily().equals(CardFamily.MONSTER));
        eventDataFromServer.add(this);
    }

    public static void reset() {
        eventDataFromServer = new ArrayList<>();
    }

    public int getCardId() {
        return cardId;
    }
}
