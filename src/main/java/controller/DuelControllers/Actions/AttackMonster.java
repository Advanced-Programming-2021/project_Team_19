package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Data.TriggerActivationData;

import java.util.regex.Matcher;

public class AttackMonster extends Attack {

    private int enemyId;

    public AttackMonster(GameData gameData) {
        super(gameData, "attack monster");
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void run(Matcher matcher) {

        if (canActionBeDone()) {
            attackMonster(matcher);
        }

    }

    @Override
    public String actionIsValid() {

        String attackErrors = checkMutualAttackErrors();

        if (!attackErrors.equals("")) {
            return attackErrors;
        }

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() == 0) {
            return "there is no card to attack here";
        }

        return "attack monster";
    }

    public void attackMonster(Matcher matcher) {

        matcher.find();
        enemyId = Integer.parseInt(matcher.group(1));


        if (!actionIsValid().equals("attack monster")) {
            return;
        }

        if (((Monster) gameData.getSecondGamer().getGameBoard().getMonsterCardZone()
                .getCardById(enemyId)).attackIsNormal(gameData)) {

            TriggerActivationData activationData = handleTriggerEffects();

            if (activationData.hasActionStopped) {
                return;
            }

            ((Monster) attackingMonster).handleAttack(gameData, enemyId);

        }

    }

}
