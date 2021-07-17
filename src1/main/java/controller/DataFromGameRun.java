package controller;

import java.util.*;
import model.Card.*;
import model.*;
import controller.DuelControllers.*;

public class DataFromGameRun {
    public static ArrayList<DataFromGameRun> eventDataFromServer = new ArrayList<>();
    public ArrayList<Card> cardsForEvent = new ArrayList<>();
    public String event;
    public Gamer gamerOfAction;


    public DataFromGameRun(String event){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        eventDataFromServer.add(this);
    }

    public DataFromGameRun(String event, Card card){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        cardsForEvent.add(card);
        eventDataFromServer.add(this);
    }

    public static void reset() {
        eventDataFromServer = new ArrayList<>();
    }


}