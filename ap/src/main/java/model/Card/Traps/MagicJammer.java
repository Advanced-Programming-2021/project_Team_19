package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;

public class MagicJammer extends Trap{

    @Override
    public TriggerActivationData activate(GameData gameData) {


        return null;
    }

    public boolean canActivateBecauseOfAnAction(Action action){

        if(!action.getGameData().getCurrentGamer().equals
                (action.getGameData().getCardController(this))){
            return false;
        }

        if(action.getActionName().equals("activate spell")){
            return true;
        }
        return false;
    }
}
