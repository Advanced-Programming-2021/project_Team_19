package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

public class ActivateSpeedEffect extends ActivateTrapWithNotification {

    public ActivateSpeedEffect(GameData gameData){
        super(gameData);
    }

    public ActivationData handleActivate() {



        return null;
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
