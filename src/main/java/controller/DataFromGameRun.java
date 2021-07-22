package controller;

import controller.DuelControllers.GameData;
import model.Gamer;

import java.util.ArrayList;

public class DataFromGameRun {
    public ArrayList<String> cardNames = new ArrayList<>();
    public String event;
    public String gamerOfActionName;
    public int cardId;


    public DataFromGameRun(Gamer gamer, String event){
        this.event = event;
        gamerOfActionName = gamer.getUsername();
        gamer.dataForSend.add(this);
    }

    public DataFromGameRun(GameData gameData, String event){
        this.event = event;
        gamerOfActionName = gameData.dataSender.getUsername();
        gameData.addData(this);
        gameData.getOtherGamer(gameData.dataSender).dataForSend.add(this);
    }

    public DataFromGameRun(GameData gameData, String[] eventAndId){
        this.cardId = Integer.parseInt(eventAndId[1]);
        this.event = eventAndId[0];
        gamerOfActionName = gameData.dataSender.getUsername();
        gameData.addData(this);
        gameData.getOtherGamer(gameData.dataSender).dataForSend.add(this);
    }

    public DataFromGameRun(GameData gameData, String event, String cardName){
        this.event = event;
        gamerOfActionName = gameData.dataSender.getUsername();
        cardNames.add(cardName);
        gameData.addData(this);
        gameData.getOtherGamer(gameData.dataSender).dataForSend.add(this);
    }

    public DataFromGameRun(GameData gameData, String event, ArrayList<String> validActions){
        this.event = event;
        gamerOfActionName = gameData.dataSender.getUsername();
        cardNames = validActions;
        gameData.addData(this);
        gameData.getOtherGamer(gameData.dataSender).dataForSend.add(this);
    }

    public int getCardId() {
        return cardId;
    }
}
