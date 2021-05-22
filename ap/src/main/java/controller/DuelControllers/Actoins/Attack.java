package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.CardMod;
import model.Phase;
import view.Printer.Printer;

import java.util.ArrayList;

public abstract class Attack extends Action{

    protected Card attackingMonster;

    public Attack(GameData gameData, String actionName){
        super(gameData, actionName);
        attackingMonster = gameData.getSelectedCard();
    }

    public Card getAttackingMonster(){
        return attackingMonster;
    }

    public boolean checkMutualAttackErrors(Card selectedCard, GameData gameData) {

        if (selectedCard == null) {
            Printer.print("no card is selected yet");
            return false;
        } else if (!gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(selectedCard)) {
            Printer.print("you can’t attack with this card");
            return false;
        } else if(!((Monster) selectedCard).getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)){
            Printer.print("you cannot attack with a defensive monster");
            return false;
        }
        else if (!gameData.getCurrentPhase().equals(Phase.BATTLE)) {
            Printer.print("action not allowed in this phase");
            return false;
        } else if (gameData.getTurn() == ((Monster) selectedCard).getLastTurnAttacked()) {
            Printer.print("this card already attacked");
            return false;
        }
        return true;
    }

    protected boolean canAttack(){

        boolean canAttack = true;

        gameData.addActionToCurrentActions(this);

        ArrayList<EffectLabel> tempArray = (ArrayList<EffectLabel>)
                gameData.getSecondGamer().getEffectLabels().clone();

        for (EffectLabel label : tempArray) {
            if (label.checkLabel()) {
                TriggerActivationData data = label.runEffect();
                if (!data.message.equals("")) {
                    Printer.print(data.message);
                }
                if(data.hasActionStopped){
                    canAttack = false;
                }
            }
        }

        gameData.removeActionFromCurrentActions(this);

        return canAttack;
    }

}
