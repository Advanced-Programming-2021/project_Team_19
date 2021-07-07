package controller;

import java.util.*;

public class DataFromGameRun {
    public static ArrayList<DataFromGameRun> eventDataFromServer = new ArrayList<>();
    private ArrayList<String> events = new ArrayList<>();

    public DataFromGameRun(ArrayList<String> events){
        for (String event : events) {
            new DataFromGameRun(event);
        }
    }

    public DataFromGameRun(String event){
        this.events.add(event);
        eventDataFromServer.add(this);
    }

    public ArrayList<DataFromGameRun> getEvents() {
        return eventDataFromServer;
    }

    public void addEvents(ArrayList<String> eventsToAdd){
        for (String event : events) {
            new DataFromGameRun(event);
        }
    }


}
