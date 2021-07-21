package controller;

import controller.DuelControllers.GameData;
import model.Gamer;

import java.util.ArrayList;

public class DataFromGameRun {
    public ArrayList<String> cardNames = new ArrayList<>();
    public String event;
    public Gamer gamerOfAction;
    public int cardId;


    public DataFromGameRun(GameData gameData, String event){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        gameData.addData(this);
    }

    public DataFromGameRun(GameData gameData, String[] eventAndId){
        this.cardId = Integer.parseInt(eventAndId[1]);
        this.event = eventAndId[0];
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        gameData.addData(this);
    }

    public DataFromGameRun(GameData gameData, String event, String cardName){
        this.event = event;
        gamerOfAction = GameData.getGameData().getCurrentGamer();
        cardNames.add(cardName);
        gameData.addData(this);
    }

    public int getCardId() {
        return cardId;
    }
}