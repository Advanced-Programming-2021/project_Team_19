package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import controller.DuelControllers.CardActionManager;

import java.util.regex.Matcher;

public class AttackMonster extends Attack {

    private int enemyId;

    public AttackMonster(GameData gameData) {
        super(gameData, "attack monster");
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void run() {

        if (canActionBeDone()) {
            attackMonster();
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

    public void attackMonster() {

//        ((Monster) gameData.getSecondGamer().getGameBoard().getMonsterCardZone()
//                .getCardById(enemyId)).attackIsNormal(gameData)

        TriggerActivationData activationData = handleTriggerEffects();

        if (activationData.hasActionStopped) {
            return;
        }

//        ((Monster) CardActionManager.getInstance(null).card).handleAttack(gameData,
//                (Monster) CardActionManager.getInstance(null).getSelectedCardsForMultiCardAction().get(0));


    }

}
