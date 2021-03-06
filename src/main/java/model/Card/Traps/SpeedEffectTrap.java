package model.Card.Traps;

import controller.DuelControllers.GameData;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import model.Card.Trap;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public abstract class SpeedEffectTrap extends Trap {

    public SpeedEffectTrap(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

    public boolean canActivate(GameData gameData) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(gameData, this));
        checkers.add(new mirageDragonChecker(gameData, this));

        return Checker.multipleCheck(checkers);
    }
}
