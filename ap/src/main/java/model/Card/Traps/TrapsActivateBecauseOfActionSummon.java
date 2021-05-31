package model.Card.Traps;

import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public abstract class TrapsActivateBecauseOfActionSummon extends Trigger {
    public TrapsActivateBecauseOfActionSummon
            (String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

}
