package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

public class DirectAttack extends Attack{

    public DirectAttack(GameData gameData){
        super(gameData, "direct attack");
    }

    public void run(){

        if(canActionBeDone()){
            directAttack();
        }

    }


    private void directAttack() {



        Card selectedCard = gameData.getSelectedCard();

        if (checkMutualAttackErrors(selectedCard, gameData)) {
            if (currentPlayerCannotDirectAttack(gameData)) {
                Printer.print("you can’t attack the opponent directly");
            } else {

                TriggerActivationData activationData = handleTriggerEffects();

                if(activationData.hasActionStopped){
                    return;
                }

                ((Monster)attackingMonster).handleDirectAttack(gameData);
            }
        }
    }


    private static boolean currentPlayerCannotDirectAttack(GameData gameData) {
        for (int i = 0; i < 5; i++) {
            if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                return true;
        }
        return false;
    }
}
