package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Trap;

public class TrapHole extends Trap {

    public void activate(GameData gameData) {

        Action action = gameData.getCurrentActions().get(
                (gameData.getCurrentActions().size() - 1));

        ((Summon)action).getSummoningMonster().handleDestroy(gameData);
    }

    public boolean canActivate(GameData gameData){

        for(Action action : gameData.getCurrentActions()){
            if(!Utils.isCurrentGamerActionDoer(gameData, action)){
                if(action instanceof FlipSummon || action instanceof NormalSummon){
                    if(((Summon) action).getSummoningMonster().getAttack(gameData) >= 1000){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
