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
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class MirrorForce extends Trap {

    @Override
    public ActivationData activate(GameData gameData) {

        handleEffect(gameData);

        handleDestroy(gameData);
        turnActivated = gameData.getTurn();

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

        if (Utils.isCareOwnerActionDoer(action.getGameData(), action, this)) {
            return false;
        }

        if (!(action instanceof Attack)) {
            return false;
        }

        return true;
    }
    public MirrorForce(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
