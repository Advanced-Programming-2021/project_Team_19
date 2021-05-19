package model.Card.TrapAndSpellTypes;

import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public abstract class Normal extends Trap {
    public Normal(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
