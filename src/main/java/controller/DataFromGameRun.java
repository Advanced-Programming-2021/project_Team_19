package controller;

import controller.DuelControllers.GameData;

import java.util.ArrayList;

public class DataFromGameRun {
    public ArrayList<String> cardNames = new ArrayList<>();
    public String event;
    public String gamerOfActionName;
    public int cardId;


    public DataFromGameRun(GameData gameData, String event){
        this.event = event;
        gamerOfActionName = gameData.getCurrentGamer().getUsername();
        gameData.addData(this);
    }

    public DataFromGameRun(GameData gameData, String[] eventAndId){
        this.cardId = Integer.parseInt(eventAndId[1]);
        this.event = eventAndId[0];
        gamerOfActionName = gameData.getCurrentGamer().getUsername();
        gameData.addData(this);
    }

    public DataFromGameRun(GameData gameData, String event, String cardName){
        this.event = event;
        gamerOfActionName = gameData.getCurrentGamer().getUsername();
        cardNames.add(cardName);
        gameData.addData(this);
    }

    public int getCardId() {
        return cardId;
    }
}
