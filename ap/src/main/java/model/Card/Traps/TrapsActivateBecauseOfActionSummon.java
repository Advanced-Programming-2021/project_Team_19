package model.Card.Traps;

import model.Card.Trap;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public abstract class TrapsActivateBecauseOfActionSummon extends Trigger {
    public TrapsActivateBecauseOfActionSummon
            (String name, String description, int price, Type type, Icon icon, Status status) {
        super(name, description, price, type, icon, status);
    }

}
