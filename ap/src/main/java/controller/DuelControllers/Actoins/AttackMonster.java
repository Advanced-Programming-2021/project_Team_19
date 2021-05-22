package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class AttackMonster extends Attack {

    public AttackMonster(GameData gameData) {
        super(gameData, "attack monster");
    }

    public void run(Matcher matcher) {

        if(canAttack()){
            attackMonster(matcher);
        }

    }

    public void attackMonster(Matcher matcher) {

        Card selectedCard = gameData.getSelectedCard();
        matcher.find();
        int enemyId = Integer.parseInt(matcher.group(1));

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
