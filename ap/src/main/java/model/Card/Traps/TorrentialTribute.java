package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;

public class TorrentialTribute extends Trap {
    @Override
    public ActivationData activate(GameData gameData) {

        gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().removeAllCards();
        gameData.getSecondGamer().getGameBoard().getMonsterCardZone().removeAllCards();

        handleDestroy(gameData);
        turnActivated = gameData.getTurn();
        return new TriggerActivationData
                (false, "trap activated successfully", this);
    }

    public boolean canActivateBecauseOfAnAction(Action action){

        if(!action.getGameData().getCurrentGamer().equals
                (action.getGameData().getCardController(this))){
            return false;
        }

        if(!canActivateThisTurn(action.getGameData())){
            return false;
        }

        if (!(action instanceof Summon)) {
            return false;
        }

        return true;
    }
}
