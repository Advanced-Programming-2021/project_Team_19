package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class MysticalSpaceTyphoon extends Spell implements Destroyer {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void destroy(Card card) {

    }

    public MysticalSpaceTyphoon(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }
}
