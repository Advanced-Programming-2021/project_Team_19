package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Tribute;
import model.Data.ActivationData;

public class AdvancedRitualArt extends Spell implements Tribute {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void Tribute() {

    }
}
