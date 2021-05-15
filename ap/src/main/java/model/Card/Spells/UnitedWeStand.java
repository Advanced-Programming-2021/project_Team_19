package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;

public class UnitedWeStand extends Spell implements Undo {
    @Override
    public ActivationData activate(GameData gameData)  {
        return null;
    }
    @Override
    public void undo() {

    }
}
