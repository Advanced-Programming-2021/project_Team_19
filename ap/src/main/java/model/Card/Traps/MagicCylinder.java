package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.Utils;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class MagicCylinder extends TrapsActivateBecauseOfActionAttack {

    public MagicCylinder(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
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
