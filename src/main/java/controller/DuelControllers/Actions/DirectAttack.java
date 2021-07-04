package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Data.TriggerActivationData;

public class DirectAttack extends Attack {

    public DirectAttack(GameData gameData) {
        super(gameData, "direct attack");
        attackingMonster = gameData.getSelectedCard();
    }

    public String run() {

        return directAttack();

    }

    @Override
    public String actionIsValid() {

        String attackErrors = checkMutualAttackErrors();

        if (!canActionBeDone()){
            return "action cannot be done";
        }

        if (!attackErrors.equals("")) {
            return attackErrors;
        }

        if (currentPlayerCannotDirectAttack()) {
            return "you can’t attack the opponent directly";
        }

        return "direct attack";
    }


    private String directAttack() {

        TriggerActivationData activationData = handleTriggerEffects();

        if (activationData.hasActionStopped) {
            return "direct attack not successful";
        }

        ((Monster) attackingMonster).handleDirectAttack(gameData);
        return "direct attack successful";
    }


    private boolean currentPlayerCannotDirectAttack() {
        for (int i = 0; i < 5; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                return true;
        }
        return false;
    }
}
