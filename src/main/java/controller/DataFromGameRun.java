package controller;

import java.util.*;

public class DataFromGameRun {
    private ArrayList<String> events = new ArrayList<>();

    public DataFromGameRun(ArrayList<String> events){
        this.events = events;
    }

    public DataFromGameRun(String event){
        events.add(event);
    }

    public ArrayList<String> getEvents() {
        return events;
    }


}
