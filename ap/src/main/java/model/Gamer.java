package model;

import controller.DuelControllers.GameData;
import model.Card.Monster;

import javax.management.MalformedObjectNameException;
import java.util.ArrayList;

public class Gamer {

    AllBoards gameBoard ;
    private int lifePoint = 4000;

    private User user;

    private int lastTurnHasSummonedOrSet = 0;
    private int maxLifePointsInDuel = 0;
    private int currentScoreInDuel = 0;
    private ArrayList<EffectLabel> effectLabels = new ArrayList<>();

    public Gamer(User user) {
        this.user = user;
        gameBoard = new AllBoards(user);
    }

    public ArrayList<EffectLabel> getEffectLabels(){
        return effectLabels;
    }

    public int getLifePoint() {
        return lifePoint;
    }

    public int getLastTurnHasSummonedOrSet(){
        return lastTurnHasSummonedOrSet;
    }

    public void setLastTurnHasSummoned(int lastTurnHasSummoned){
        this.lastTurnHasSummonedOrSet = lastTurnHasSummoned;
    }

    public String getBoardForSelf() {
        return gameBoard.selfToString() +
                user.getNickname() + ":" + lifePoint;
    }

    public String getBoardForRival() {
        return user.getNickname() + ":" + lifePoint + "\n" +
                gameBoard.rivalToString();
    }

    public void addEffectLabel(EffectLabel effectLabel){
        effectLabels.add(effectLabel);
    }

    public void removeLabel(EffectLabel effectLabel){
        effectLabels.remove(effectLabel);
    }

    public void increaseLifePoint(int amount) {
        this.lifePoint += amount;
    }

    public void decreaseLifePoint(int amount) {
        if (amount == this.lifePoint + 1){
            this.lifePoint = -1;
        }
        else {
            this.lifePoint -= amount;
            if (this.lifePoint < 0)
                this.lifePoint = 0;
        }
    }

    public AllBoards getGameBoard(){
        return gameBoard;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public User getUser() {
        return user;
    }

    public void gameFinished(){
        maxLifePointsInDuel = Integer.max(lifePoint, maxLifePointsInDuel);
        lifePoint = 4000;
        gameBoard = new AllBoards(getUser());
    }

    public void increaseCredit(int amount){
        this.user.increaseCredit(amount);
    }

    public int getMaxLifePointsInDuel() {
        return maxLifePointsInDuel;
    }

    public void wonGame(){this.currentScoreInDuel += 1000;}

    public void increaseUserScore(int amount){user.increaseScore(amount);}

    public int getCurrentScoreInDuel() {
        return currentScoreInDuel;
    }

    public void destroyAllMonstersOnBoard(GameData gameData){
        for (Monster monster : getGameBoard().getMonsterCardZone().getCards()) {
            if (monster != null)
                monster.handleDestroy(gameData);
        }
    }


    private Gamer(){
    }

    public static Gamer getTestGamer(User user){

        Gamer gamer = new Gamer();
        gamer.user = user;
        gamer.gameBoard = AllBoards.getTestBoard(user);
        return gamer;
    }

    public static Gamer getAIGamer(User user){
        Gamer gamer = new Gamer();
        gamer.user = user;
        return gamer;
    }
}
