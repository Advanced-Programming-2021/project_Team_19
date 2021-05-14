package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Trap;

public class TrapHole extends Trap {

    public void activate(GameData gameData) {

        Action action = gameData.getCurrentActions().get(
                (gameData.getCurrentActions().size() - 1));

        ((Summon) action).getSummoningMonster().handleDestroy(gameData);

        handleDestroy(gameData);
    }

    public boolean canActivateBecauseOfAnAction(Action action) {

        if (!canActivateThisTurn(action.getGameData())) {
            return false;
        }

        if (Utils.isCurrentGamerActionDoer(action.getGameData(), action)) {
            return false;
        }

        if (!(action instanceof FlipSummon || action instanceof NormalSummon)) {
           return false;
        }

        if (((Summon) action).getSummoningMonster().getAttack(action.getGameData()) >= 1000) {
            return false;
        }

        return true;
    }

}