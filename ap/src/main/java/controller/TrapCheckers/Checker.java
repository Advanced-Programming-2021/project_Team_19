package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.SpellAndTraps;

public abstract class Checker {

    GameData gameData;
    SpellAndTraps card;

    protected Checker(GameData gameData, SpellAndTraps card){
        this.gameData = gameData;
        this.card = card;
    }

    public abstract boolean check();
}
