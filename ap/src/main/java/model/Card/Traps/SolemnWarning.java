package model.Card.Traps;

import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public class SolemnWarning extends Trap{

    public SolemnWarning(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }



}
