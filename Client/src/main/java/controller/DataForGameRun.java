package controller;

import model.Data.DataForServerFromClient;
import view.Menu.Menu;
import view.graphic.CardView;
import view.graphic.GameView;

public class DataForGameRun extends DataForServerFromClient {

    int id;
    String zoneName;

    public DataForGameRun(String command){
        super(command, Menu.token, "duel");
    }

    public int getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void findIdAndZoneName(GameView gameView, CardView cardView){
        gameView.setIdAndZoneForData(this, cardView);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

}
