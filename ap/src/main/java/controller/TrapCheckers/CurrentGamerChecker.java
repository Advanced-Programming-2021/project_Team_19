package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;

public class CurrentGamerChecker extends Checker{

    protected CurrentGamerChecker(GameData gameData, SpellAndTraps card) {
        super(gameData, card);
    }

    @Override
    public boolean check() {
        if(!gameData.getCurrentGamer().equals(gameData.getCardController(card))){
            return false;
        }
        return true;
    }
}
