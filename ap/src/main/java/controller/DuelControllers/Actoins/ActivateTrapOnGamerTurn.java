package controller.DuelControllers.Actoins;

import view.Printer.Printer;

public class ActivateTrapOnGamerTurn extends ActivateTriggerTrapEffect {


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
