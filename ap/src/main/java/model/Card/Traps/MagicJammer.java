package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;

public class MagicJammer extends Trap{

    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    public boolean canActivateBecauseOfAnAction(Action action){
        if(action.getActionName().equals("activate spell")){
            return true;
        }
        return false;
    }
}
