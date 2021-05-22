package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardFamily;

import java.util.ArrayList;

public abstract class ActivateTriggerTrapEffect extends ActivateTrapWithNotification {

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


    private TriggerActivationData activateTrapOrSpell(Action action) {

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);


        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectZoneChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if(checkersResult != null){
            data.message = checkersResult;
            return data;
        }

        if (!((SpellAndTraps)activatedCard).canActivateBecauseOfAnAction(action)) {

            data.message = "you can't activate this card";
            return data;
        }

        if (((SpellAndTraps)activatedCard).canActivateBecauseOfAnAction(action)) {

            return (TriggerActivationData) ((SpellAndTraps)activatedCard).activate(gameData);

        }

        return data;

    }

    protected abstract boolean checkInvalidMoves(String command);

}
