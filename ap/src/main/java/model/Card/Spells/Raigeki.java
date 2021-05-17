package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;

public class Raigeki extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
       gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);

        handleDestroy(gameData);
        return new ActivationData(this, "all enemy monsters were destroyed");
    }

}
