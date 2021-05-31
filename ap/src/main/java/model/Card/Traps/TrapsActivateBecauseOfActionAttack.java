package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public abstract class TrapsActivateBecauseOfActionAttack extends Trigger {

    public TrapsActivateBecauseOfActionAttack
            (String name, String description, int price, Type type, TrapTypes trapType, Status status) {

        super(name, description, price, type, trapType, status);
    }


    public boolean canActivateBecauseOfAnAction(Action action) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(action.getGameData(), this));
        checkers.add(new CardOwnerIsNotActionDoerChecker(action, this));
        checkers.add(new mirageDragonChecker(action.getGameData(), this));

        if (!Checker.multipleCheck(checkers)) {
            return false;
        }

        return action instanceof Attack;
    }

}
