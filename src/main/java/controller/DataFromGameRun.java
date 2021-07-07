package controller;

import java.util.*;
import model.Card.*;

public class DataFromGameRun {
    public static ArrayList<DataFromGameRun> eventDataFromServer = new ArrayList<>();
    public ArrayList<Card> cardsForEvent = new ArrayList<>();
    public String event;


    public DataFromGameRun(String event){
        this.event = event;
        eventDataFromServer.add(this);
    }

    public DataFromGameRun(String event, Card card){
        this.event = event;
        cardsForEvent.add(card);
        eventDataFromServer.add(this);
    }

    public static void reset() {
        eventDataFromServer = new ArrayList<>();
    }

    public void addEvents(ArrayList<String> eventsToAdd){
        for (String event : eventsToAdd) {
            new DataFromGameRun(event);
        }
    }


}
