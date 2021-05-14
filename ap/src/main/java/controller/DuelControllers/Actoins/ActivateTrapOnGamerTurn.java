package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateTrapOnGamerTurn extends ActivateTrapBecauseOfAnAction{


    public ActivateTrapOnGamerTurn(Action action){
        super(action);
    }

    protected boolean checkInvalidMoves(String command){

        for(String str : getInvalidMoves()){
            if(command.matches(str)){
                Printer.print("please activate one trap or spell");
                return true;
            }
        }
        return false;
    }
}
