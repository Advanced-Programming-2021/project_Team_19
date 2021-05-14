package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.Spell;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Phase;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateTrapOnOtherPlayerTurn extends ActivateTrapBecauseOfAnAction {

    public ActivateTrapOnOtherPlayerTurn(Action action){
        super(action);
    }

    protected boolean checkInvalidMoves(String command){

        for(String str : getInvalidMoves()){
            if(command.matches(str)){
                Printer.print("it’s not your turn to play this kind of moves");
                return true;
            }
        }
        return false;
    }
}
