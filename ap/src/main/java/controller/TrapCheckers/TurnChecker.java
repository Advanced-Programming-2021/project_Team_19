package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;
import view.Printer.Printer;

public class TurnChecker extends Checker{

    public TurnChecker(GameData gameData, SpellAndTraps card){
        super(gameData, card);
    }

    public boolean check(){

        if (!card.canActivateThisTurn(gameData)) {
            return false;
        }
        if(card.getTurnActivated() != 0){
            return false;
        }
        return true;
    }

}
