package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;

public class DarkHole extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);
        gameData.getCurrentGamer().destroyAllMonstersOnBoard(gameData);

        handleDestroy(gameData);
        return new ActivationData(this, "all monsters were destroyed");
    }

}
