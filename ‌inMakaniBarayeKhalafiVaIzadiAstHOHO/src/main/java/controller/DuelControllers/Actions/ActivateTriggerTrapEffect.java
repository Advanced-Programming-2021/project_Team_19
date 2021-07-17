package controller.DuelControllers.Actions;

import controller.ActivateCheckers.*;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;

import java.util.ArrayList;

public class ActivateTriggerTrapEffect extends ActivateTrapWithNotification {

    Action activatorAction;

    public ActivateTriggerTrapEffect(Action action) {
        super(action.getGameData());
        activatorAction = action;
    }

    public Action getActivatorAction() {
        return activatorAction;
    }


    public ActivationData handleActivate() {
        return activateTrapOrSpell(activatorAction);
    }


    public boolean canDoThisActionBecauseOfAnAction(){

        setActivatedCard(gameData.getSelectedCard());

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectSpellZoneChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if (checkersResult != null) {
            return false;
        }

        if (!((SpellAndTraps) activatedCard).canActivateBecauseOfAnAction(activatorAction)) {
            return false;
        }
        return true;
    }

    public TriggerActivationData activateTrapOrSpell(Action action) {

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        setActivatedCard(gameData.getSelectedCard());

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectSpellZoneChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);


        if (checkersResult != null) {
            data.message = checkersResult;
            return data;
        }

        if (!((SpellAndTraps) activatedCard).canActivateBecauseOfAnAction(action)) {

            data.message = "you can't activate this card";
            return data;
        }


        if (((SpellAndTraps) activatedCard).canActivateBecauseOfAnAction(action)) {
            return (TriggerActivationData) super.activate();
        }

        return data;

    }

    protected boolean checkInvalidMoves(String command){
        return false;
    }

}
