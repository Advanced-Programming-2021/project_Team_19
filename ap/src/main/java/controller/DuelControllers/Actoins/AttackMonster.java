package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

import java.util.regex.Matcher;

public class AttackMonster extends Attack {

    private int enemyId;

    public AttackMonster(GameData gameData) {
        super(gameData, "attack monster");
    }

    public int getEnemyId(){
        return enemyId;
    }

    public void run(Matcher matcher) {

        if(canActionBeDone()){
            attackMonster(matcher);
        }

    }

    public void attackMonster(Matcher matcher) {

        Card selectedCard = gameData.getSelectedCard();
        matcher.find();
        enemyId = Integer.parseInt(matcher.group(1));

        if (checkMutualAttackErrors(selectedCard, gameData)) {

            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(enemyId) == null)
                Printer.print("there is no card to attack here");
            else {

                if (((Monster) gameData.getSecondGamer().getGameBoard().getMonsterCardZone()
                        .getCardById(enemyId)).attackIsNormal(gameData)) {

                    TriggerActivationData activationData = handleTriggerEffects();

                    if(activationData.hasActionStopped){
                        return;
                    }

                    ((Monster)attackingMonster).handleAttack(gameData, enemyId);

                }
            }
        }

    }

}
