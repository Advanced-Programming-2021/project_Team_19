package model.Card.TrapAndSpellTypes;

import model.Card.Trap;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public abstract class Normal extends Trap {
    public Normal(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }
}
