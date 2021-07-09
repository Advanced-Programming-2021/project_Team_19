package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Phase;

public abstract class Attack extends Action {

    protected Card attackingMonster;

    public Attack(GameData gameData, String actionName) {
        super(gameData, actionName);
        attackingMonster = gameData.getSelectedCard();
    }

    public abstract String run(boolean checkTriggerOrRun);

    public Card getAttackingMonster() {
        return attackingMonster;
    }

    public String checkMutualAttackErrors() {

        Card selectedCard = gameData.getSelectedCard();

        if (selectedCard == null) {
            return "no card is selected yet";
        } else if (!gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(selectedCard)) {
            return "you can’t attack with this card";
        } else if (!((Monster) selectedCard).getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
            return "you cannot attack with a defensive monster";
        } else if (!gameData.getCurrentPhase().equals(Phase.BATTLE)) {
            return "action not allowed in this phase";
        } else if (gameData.getTurn() == ((Monster) selectedCard).getLastTurnAttacked()) {
            return "this card already attacked";
        }
        return "";
    }

}
