package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.ContinuousEffect;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;

public class SwordsOfRevealingLight extends Spell implements ContinuousEffect, Undo {
    @Override
    public ActivationData activate(GameData gameData)  {
        return null;
    }

    @Override
    public void undo() {

    }

    @Override
    public void checkActivation() {

    }
}
