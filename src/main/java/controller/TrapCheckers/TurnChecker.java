package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;

public class TurnChecker extends Checker {

    public TurnChecker(GameData gameData, SpellAndTraps card) {
        super(gameData, card);
    }

    public boolean check() {

        if (!card.canActivateThisTurn(gameData)) {
            return false;
        }
        return card.getTurnActivated() == 0;
    }

}
