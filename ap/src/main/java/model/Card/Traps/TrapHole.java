package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.FlipSummon;
import controller.DuelControllers.Actoins.NormalSummon;
import controller.DuelControllers.Actoins.Summon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Trap;

public class TrapHole extends Trap {
    @Override
    public void activate() {

    }

    public boolean canActivate(GameData gameData){

        for(Action action : gameData.getCurrentActions()){
            if(!Utils.isCurrentGamerActionDoer(gameData, action)){
                if(action instanceof FlipSummon || action instanceof NormalSummon){
                    if(((Summon) action).getSummoningMonster().getAttack() >= 1000){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
