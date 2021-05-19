package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class PotOfGreed extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    public PotOfGreed(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
