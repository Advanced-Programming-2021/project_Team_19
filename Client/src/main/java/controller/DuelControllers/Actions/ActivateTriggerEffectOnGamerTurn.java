package controller.DuelControllers.Actions;

import controller.Utils;
import view.Printer.Printer;

public class ActivateTriggerEffectOnGamerTurn extends ActivateTriggerTrapEffect {


    public ActivateTriggerEffectOnGamerTurn(Action action) {
        super(action);
    }

    protected boolean checkInvalidMoves(String command) {

        for (String str : Utils.getCommandsExceptActivation()) {
            if (command.matches(str)) {
                Printer.print("please activate one trap or spell");
                return true;
            }
        }
        return false;
    }
}
