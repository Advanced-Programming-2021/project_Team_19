package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public abstract class Trigger extends Trap {


    public Trigger(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }

    public abstract boolean canActivateBecauseOfAnAction(Action action);

}
