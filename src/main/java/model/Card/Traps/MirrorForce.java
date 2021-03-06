package model.Card.Traps;

import controller.DuelControllers.Actions.Attack;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardMod;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class MirrorForce extends TrapsActivateBecauseOfActionAttack {

    public MirrorForce(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {

        int trapIndex = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(this);

        String ids = handleEffect(gameData);

        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData(
                true,
                "activate trap " +
                        trapIndex
                        + ":change turn:trap hole:" +
                        "activate spell " +
                        "-1 destroy rival monsters" + ids,
                this);
    }

    private String handleEffect(GameData gameData) {

        StringBuilder ids = new StringBuilder();
        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Attack.class);

        ArrayList<Monster> monsters = gameData.getCardController(attack.getAttackingMonster())//gamer
                .getGameBoard().getMonsterCardZone().getCards();

        for (Monster monster : monsters) {
            if (monster != null && monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
                ids.append(" ").append(gameData.getCardController(attack.getAttackingMonster())//gamer
                        .getGameBoard().getMonsterCardZone().getId(monster));

                monster.handleDestroy(gameData);
            }
        }

        return ids.toString();
    }

}
