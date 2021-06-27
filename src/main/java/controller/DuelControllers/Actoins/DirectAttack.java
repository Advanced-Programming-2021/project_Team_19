package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Data.TriggerActivationData;

public class DirectAttack extends Attack {

    public DirectAttack(GameData gameData) {
        super(gameData, "direct attack");
    }

    public void run() {

        if (canActionBeDone()) {
            if (actionIsValid().equals("direct attack")) {
                directAttack();
            }
        }

    }

    @Override
    public String actionIsValid() {

        String attackErrors = checkMutualAttackErrors();

        if (!attackErrors.equals("")) {
            return attackErrors;
        }

        if (currentPlayerCannotDirectAttack()) {
            return "you can’t attack the opponent directly";
        }

        return "direct attack";
    }


    private void directAttack() {

        TriggerActivationData activationData = handleTriggerEffects();

        if (activationData.hasActionStopped) {
            return;
        }

        ((Monster) attackingMonster).handleDirectAttack(gameData);
    }


    private boolean currentPlayerCannotDirectAttack() {
        for (int i = 0; i < 5; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                return true;
        }
        return false;
    }
}
