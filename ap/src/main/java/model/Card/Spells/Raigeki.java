package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;

public class Raigeki extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        for (int i = 1; i < 6; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i).handleDestroy(gameData);
        }
        handleDestroy(gameData);
        return new ActivationData(this, "all your enemy cards were destroyed");
    }

}
