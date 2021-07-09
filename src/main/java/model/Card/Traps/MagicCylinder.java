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

        int trapIndex = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(this);

        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Attack.class);

        int attackOfMonster = ((Monster) attack.getAttackingMonster()).getAttack(gameData);

        gameData.getCardController(attack.getAttackingMonster()).decreaseLifePoint(attackOfMonster);

        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData
                (true, "activate trap " +
                        trapIndex
                        + ":change turn:magic cylinder:" +
                        "activate spell -1 " + "destroy spell and rival loses " + attackOfMonster, this);
    }

}
