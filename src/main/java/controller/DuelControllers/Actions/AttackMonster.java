package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;

public class AttackMonster extends Attack {

    private int enemyId;

    public AttackMonster(GameData gameData, int enemyId) {
        super(gameData, "attack monster");
        this.enemyId = enemyId;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public String[] run(boolean checkTriggerOrRun) {
        if(checkTriggerOrRun){
            checkTrigger();
            return new String[]{"", ""};
        } else{
            return new String[]{attackMonster(), String.valueOf(currentId)};
        }
    }

    @Override
    public String actionIsValid() {

        String attackErrors = checkMutualAttackErrors();

        if (!canActionBeDone()) {
            return "action cannot be done";
        }

        if (!attackErrors.equals("")) {
            return attackErrors;
        }

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() == 0) {
            return "there is no card to attack here";
        }

        return "attack monster";
    }

    public String attackMonster() {

        currentId = gameData.getCurrentGamer().getGameBoard().getHand().getId(attackingMonster);

        ((Monster) gameData.getSecondGamer().getGameBoard().getMonsterCardZone()
                .getCardById(enemyId)).attackIsNormal(gameData);

        return ((Monster) gameData.getSelectedCard()).handleAttack(gameData, enemyId);

    }

    public static boolean canAttack(GameData gameData) {
        return gameData.getSecondGamer().getGameBoard().getMonsterCardZone().containsCard(gameData.getSelectedCard());
    }

}
