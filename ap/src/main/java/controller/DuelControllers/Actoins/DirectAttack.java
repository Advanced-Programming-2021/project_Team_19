package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.ActionData;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

public class DirectAttack extends Attack{

    public DirectAttack(GameData gameData){
        super(gameData, "direct attack");
    }

    public ActionData run(){
        return directAttack();
    }


    private ActionData directAttack() {

        ActionData returnData = new ActionData();

        Card selectedCard = gameData.getSelectedCard();

        if (checkMutualAttackErrors(selectedCard, gameData)) {
            if (currentPlayerCannotDirectAttack(gameData)) {
                Printer.print("you can’t attack the opponent directly");
            } else {

                TriggerActivationData activationData = handleTriggerEffects();

                returnData.effectLabels.addAll(activationData.labels);

                if(activationData.hasActionStopped){
                    return returnData;
                }

                ((Monster)attackingMonster).handleDirectAttack(gameData);
            }
        }
        return returnData;
    }


    private static boolean currentPlayerCannotDirectAttack(GameData gameData) {
        for (int i = 0; i < 5; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                return true;
        }
        return false;
    }
}
