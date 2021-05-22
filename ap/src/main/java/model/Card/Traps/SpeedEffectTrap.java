package model.Card.Traps;

import controller.DuelControllers.GameData;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public abstract class SpeedEffectTrap extends Trap {

    public SpeedEffectTrap(String name, String description, int price, Type type, Icon icon, Status status) {
        super(name, description, price, type, icon, status);
    }

    public boolean canActivate(GameData gameData) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(gameData, this));
        checkers.add(new mirageDragonChecker(gameData, this));

        if(!Checker.multipleCheck(checkers)){
            return false;
        }

        return true;
    }
}
