package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Gamer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MagicCylinder extends Trap {
    @Override
    public ActivationData activate(GameData gameData) {

        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Attack.class);

        gameData.getCardController(attack.getAttackingMonster()).decreaseLifePoint
                (((Monster) attack.getAttackingMonster()).getAttack(gameData));

        attack.getAttackingMonster().handleDestroy(gameData);

        handleDestroy(gameData);
        wasActivated = true;

        return new TriggerActivationData
                (true, "spell activated successfully", this);
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
