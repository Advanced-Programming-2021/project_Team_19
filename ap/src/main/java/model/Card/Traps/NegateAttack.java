package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardMod;
import model.Enums.EffectLabels;

import java.util.ArrayList;

public class NegateAttack extends Trap {

    public ActivationData activate(GameData gameData) {

        handleDestroy(gameData);
        wasActivated = true;

        TriggerActivationData data =  new TriggerActivationData
                (true, "spell activated successfully", this);



        return data;
    }


    public boolean canActivateBecauseOfAnAction(Action action) {

        if (!canActivateThisTurn(action.getGameData())) {
            return false;
        }

        if (Utils.isCurrentGamerActionDoer(action.getGameData(), action)) {
            return false;
        }

        if (!(action instanceof Attack)) {
            return false;
        }

        return true;
    }
}
