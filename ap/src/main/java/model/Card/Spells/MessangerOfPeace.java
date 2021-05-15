package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.ContinuousEffect;
import model.Data.ActivationData;

public class MessangerOfPeace extends Spell implements ContinuousEffect {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void checkActivation() {

    }
}
