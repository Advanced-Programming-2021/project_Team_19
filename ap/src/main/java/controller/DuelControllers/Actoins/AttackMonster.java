package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.ActionData;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

import java.util.regex.Matcher;

public class AttackMonster extends Attack {

    public AttackMonster(GameData gameData) {
        super(gameData, "attack monster");
    }


    public ActionData run(Matcher matcher) {
        return attackMonster(matcher);
    }

    public ActionData attackMonster(Matcher matcher) {

        ActionData returnData = new ActionData();

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

                    returnData.effectLabels.addAll(activationData.labels);

                    if(activationData.hasActionStopped){
                        return returnData;
                    }

                    ((Monster)attackingMonster).handleAttack(gameData, enemyId);


                }
            }
        }

        return returnData;

    }

}
