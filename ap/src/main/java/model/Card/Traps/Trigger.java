package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public abstract class Trigger extends Trap {


    public Trigger(String name, String description, int price, Type type, Icon icon, Status status) {
        super(name, description, price, type, icon, status);
    }

    public abstract boolean canActivateBecauseOfAnAction(Action action);

}
