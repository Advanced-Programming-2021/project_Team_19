package controller.DuelControllers.Actions;

import controller.Utils;
import view.Printer.Printer;


public class ActivateTriggerEffectOnOtherPlayerTurn extends ActivateTriggerTrapEffect {

    public ActivateTriggerEffectOnOtherPlayerTurn(Action action) {
        super(action);
    }

    protected boolean checkInvalidMoves(String command) {

        for (String str : Utils.getCommandsExceptActivation()) {
            if (command.matches(str)) {
                Printer.print("it’s not your turn to play this kind of moves");
                return true;
            }
        }
        return false;
    }
}
