package controller.DuelControllers.Actoins;

import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardFamily;
import view.GetInput;
import view.Printer.Printer;

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

        Card card = gameData.getSelectedCard();

        if (Utils.IsSelectedCardIsNull(gameData)) {
            data.message = "no card is selected yet";
            return data;
        }

        if (gameData.getSelectedCard().getCardFamily().equals(CardFamily.TRAP) ||
                gameData.getSelectedCard().getCardFamily().equals(CardFamily.SPELL)) {

            data.message = "activate effect is only for spell and trap cards";
            return data;
        }

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)) {

            data.message = "you can't activate this card";
            return data;
        }

        activatedCard = (SpellAndTraps) card;

        if (activatedCard.getTurnActivated() != 0) {

            data.message = "you have already activated this card";
            return data;
        }

        if (!activatedCard.canActivateBecauseOfAnAction(action)) {

            data.message = "you can't activate this card";
            return data;
        }

        if (activatedCard.canActivateBecauseOfAnAction(action)) {

            return (TriggerActivationData) activatedCard.activate(gameData);

        }

        return data;

    }

    protected abstract boolean checkInvalidMoves(String command);

}
