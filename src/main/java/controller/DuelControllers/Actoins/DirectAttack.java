package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

public class DirectAttack extends Attack {

    public DirectAttack(GameData gameData) {
        super(gameData, "direct attack");
    }

    public void run() {

        if (canActionBeDone()) {
            if (checkErrors().equals("")) {
                directAttack();
            }
        }

    }

    public String checkErrors() {

        String attackErrors = checkMutualAttackErrors();

        if (!attackErrors.equals("")) {
            return attackErrors;
        }

        if (currentPlayerCannotDirectAttack()) {
            return "you can’t attack the opponent directly";
        }

        return "";
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
