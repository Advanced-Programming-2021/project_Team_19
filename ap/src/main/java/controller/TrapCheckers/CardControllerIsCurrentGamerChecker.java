package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;

public class CardControllerIsCurrentGamerChecker extends Checker {

    protected CardControllerIsCurrentGamerChecker(GameData gameData, SpellAndTraps card) {
        super(gameData, card);
    }

    @Override
    public boolean check() {
        if (!gameData.getCurrentGamer().equals(gameData.getCardController(card))) {
            return false;
        }
        return true;
    }
}
