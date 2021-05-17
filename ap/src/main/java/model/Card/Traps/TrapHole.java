package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.TrapAndSpellTypes.Normal;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;

public class TrapHole extends Normal {

    public ActivationData activate(GameData gameData) {

        Action action = gameData.getCurrentActions().get(
                (gameData.getCurrentActions().size() - 1));

        ((Summon) action).getSummoningMonster().handleDestroy(gameData);

        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData
                (false, "trap activate successfully", this);
    }

    public boolean canActivateBecauseOfAnAction(Action action) {

        if(action.getGameData().getTurnOwner().equals
                (action.getGameData().getCardController(this))){

            return false;
        }

        if (!canActivateThisTurn(action.getGameData())) {
            return false;
        }

        if (Utils.isCareOwnerActionDoer(action.getGameData(), action, this)) {
            return false;
        }

        if (!(action instanceof FlipSummon || action instanceof NormalSummon)) {
           return false;
        }

        if (((Monster)((Summon) action).getSummoningMonster()).getAttack(action.getGameData()) >= 1000) {
            return false;
        }

        return true;
    }

}