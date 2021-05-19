package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class TwinTwisters extends Spell implements Destroyer {
    @Override
    public ActivationData activate(GameData gameData)  {
        return null;
    }

    @Override
    public void destroy(Card card) {

    }

    public TwinTwisters(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
