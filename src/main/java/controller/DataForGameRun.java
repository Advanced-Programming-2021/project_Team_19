package controller;

import model.Data.DataForServerFromClient;

public class DataForGameRun extends DataForServerFromClient {

    int id;
    String zoneName;

    public DataForGameRun(String message, String token, String menuName) {
        super(message, token, menuName);
    }


    public int getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

}
