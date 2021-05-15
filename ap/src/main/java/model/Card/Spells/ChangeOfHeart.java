package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.ContinuousEffect;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;

public class ChangeOfHeart extends Spell implements ContinuousEffect, Undo {
    Monster monster;

    @Override
    public void undo() {

    }

    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void checkActivation() {

    }
}
