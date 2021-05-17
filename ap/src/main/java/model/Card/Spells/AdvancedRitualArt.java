package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;

public class AdvancedRitualArt extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        return new ActivationData(this, "");
    }

}
