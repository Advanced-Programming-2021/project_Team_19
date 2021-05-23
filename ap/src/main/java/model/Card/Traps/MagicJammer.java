package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

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

    public MagicJammer(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
