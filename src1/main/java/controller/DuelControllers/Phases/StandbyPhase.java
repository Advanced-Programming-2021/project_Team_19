package controller.DuelControllers.Phases;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.Monsters.ScannerMonster;

public class StandbyPhase {

    public void run(GameData gameData) {
        handleScannerCards(gameData);
        handleScannerCardsForOpponent(gameData);
    }

    public void handleScannerCards(GameData gameData){
        for(Monster monster : gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards()){
            if(monster instanceof ScannerMonster){
                ((ScannerMonster) monster).scanMonster(gameData);
            }
        }
    }

    public void handleScannerCardsForOpponent(GameData gameData){

        for(Monster monster : gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards()){
            if(monster instanceof ScannerMonster){
                if(((ScannerMonster) monster).getMonstersForScan(gameData).size() == 0){
                    return;
                } else{
                    Utils.changeTurn(gameData);
                    handleScannerCards(gameData);
                    Utils.changeTurn(gameData);
                }
            }
        }
    }

}
