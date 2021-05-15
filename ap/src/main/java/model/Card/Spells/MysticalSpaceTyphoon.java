package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;

public class MysticalSpaceTyphoon extends Spell implements Destroyer {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void destroy(Card card) {

    }
}
