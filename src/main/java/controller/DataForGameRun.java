package controller;

import model.Gamer;

public class DataForGameRun {
    String command;
    Gamer gamer;
    int id;
    String zoneName;


    public DataForGameRun(String command, Gamer gamer){
        this.command = command;
        this.gamer = gamer;
    }

    public int getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

//    public void findIdAndZoneName(GameView gameView, CardView cardView){
//        gameView.setIdAndZoneForData(this, cardView);
//    }

    public String getCommand() {
        return command;
    }

    public Gamer getGamer() {
        return gamer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

}
