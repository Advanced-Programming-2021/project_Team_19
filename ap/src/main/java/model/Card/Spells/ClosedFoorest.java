package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Field;
import model.Data.ActivationData;

public class ClosedFoorest extends Spell implements Field {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void activateField() {

    }
}
