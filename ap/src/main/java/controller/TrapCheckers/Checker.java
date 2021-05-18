package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.SpellAndTraps;

import java.util.ArrayList;

public abstract class Checker {

    GameData gameData;
    SpellAndTraps card;

    protected Checker(GameData gameData, SpellAndTraps card){
        this.gameData = gameData;
        this.card = card;
    }

    public abstract boolean check();

    public static boolean multipleCheck(ArrayList<Checker> checkers){
        for(Checker checker : checkers){
            if(!checker.check()){
                return false;
            }
        }
        return true;
    }
}
