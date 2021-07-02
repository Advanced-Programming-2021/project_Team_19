package model.Card.Traps;

import controller.DuelControllers.Actions.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

public class MagicCylinder extends TrapsActivateBecauseOfActionAttack {

    public MagicCylinder(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {

        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Attack.class);

        gameData.getCardController(attack.getAttackingMonster()).decreaseLifePoint
                (((Monster) attack.getAttackingMonster()).getAttack(gameData));

        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData
                (true, "trap activated successfully\nrival LP decrease by ard attack", this);
    }

}
