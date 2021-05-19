package model.Card.Traps;

import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class SolemnWarning extends Trap{
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    public SolemnWarning(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
