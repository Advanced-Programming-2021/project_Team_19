package controller.TrapCheckers;

import controller.DuelControllers.Actoins.Action;
import model.Card.Card;
import model.Card.SpellAndTraps;

public class CardOwnerIsNotActionDoerChecker extends Checker {

    Action action;

    public CardOwnerIsNotActionDoerChecker(Action action, SpellAndTraps card) {
        super(action.getGameData(), card);
        this.action = action;
    }

    public boolean check() {

        return !isCardOwnerActionDoer(action, card);
    }

    public boolean isCardOwnerActionDoer(Action action, Card card) {
        return action.getGameData().getCardController(card).equals(action.getActionDoer());
    }
}
