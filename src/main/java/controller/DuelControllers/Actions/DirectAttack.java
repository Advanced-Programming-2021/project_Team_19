package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;

public class DirectAttack extends Attack {

    public DirectAttack(GameData gameData) {
        super(gameData, "direct attack");
        attackingMonster = gameData.getSelectedCard();
    }

    public String run(boolean checkTriggerOrRun) {
        if(checkTriggerOrRun){
            checkTrigger();
            return "";
        } else {
            return directAttack();
        }
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


        if (gameData.getTurn() == 1){
            return "cannot direct attack on first turn";
        }

        return "attack direct";
    }


    private String directAttack() {

        String lpLoss = ((Monster) attackingMonster).handleDirectAttack(gameData);
        return "rival loses " + lpLoss;
    }


    private boolean currentPlayerCannotDirectAttack() {
        for (int i = 0; i < 5; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                return true;
        }
        return false;
    }
}
