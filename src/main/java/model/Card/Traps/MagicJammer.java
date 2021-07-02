package model.Card.Traps;

import controller.DuelControllers.Actions.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.TriggerActivationData;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public class MagicJammer extends Trap {

    public MagicJammer(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

    @Override
    public TriggerActivationData activate(GameData gameData) {

        return null;
    }

    public boolean canActivateBecauseOfAnAction(Action action) {

        if (!action.getGameData().getCurrentGamer().equals
                (action.getGameData().getCardController(this))) {
            return false;
        }

        return action.getActionName().equals("activate spell");
    }

}
