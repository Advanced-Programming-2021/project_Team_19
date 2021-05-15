package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.MonsterCardZone;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardMod;

import java.util.ArrayList;

public class MirrorForce extends Trap {

    @Override
    public ActivationData activate(GameData gameData) {

        handleEffect(gameData);

        handleDestroy(gameData);
        wasActivated = true;

        return new TriggerActivationData
                (true, "spell activated successfully", this);
    }

    private void handleEffect(GameData gameData){

        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Attack.class);

        ArrayList<Monster> monsters = gameData.getCardController(attack.getAttackingMonster())//gamer
                .getGameBoard().getMonsterCardZone().getCards();

        for(Monster monster : monsters){
            if(monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)){
                monster.handleDestroy(gameData);
            }
        }
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
