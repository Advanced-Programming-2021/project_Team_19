package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.ContinuousEffect;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class MessangerOfPeace extends Spell implements ContinuousEffect {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void checkActivation() {

    }

    public MessangerOfPeace(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }
}
