package controller.TrapCheckers;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.SpellAndTraps;

public class ActionDoerChecker extends Checker{

    Action action;

    public ActionDoerChecker(Action action, SpellAndTraps card){
        super(action.getGameData(), card);
        this.action = action;
    }

    public boolean check(){

        if (isCardOwnerActionDoer(action, card)) {
            return false;
        }
        return true;
    }

    public  boolean isCardOwnerActionDoer(Action action, Card card) {
        return action.getGameData().getCardController(card).equals(action.getActionDoer());
    }
}
