package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Data.ActivationData;
import model.Enums.GameEvent;
import view.GetInput;
import view.Printer.Printer;

public abstract class ActivateTrapWithNotification extends Activation {

    public ActivateTrapWithNotification(GameData gameData) {
        super(gameData);
    }

    public ActivationData run() {

        String command;

        ActivationData data = new ActivationData(null, "");

        while (true) {

            gameData.setEvent(GameEvent.ACTIVATE_TRAP);
//            command = GetInput.getString();
            command = "GetInput.getString()";
            gameData.setEvent(null);

            if (checkInvalidMoves(command)) {

            } else if (Utils.handleSelect(gameData, command)) {

            } else if (command.matches("activate")) {
                ActivationData data1 = handleActivate();
                Printer.print(data1.message);
                if (data1.activatedCard != null) {
                    if (this instanceof ActivateSpeedEffect) {
                        gameData.setEvent(GameEvent.ASK_FOR_CONFIRMATION_FOR_ACTIVATE_ANOTHER_SPELL);
                        if (Utils.askForConfirmation("do you want to activate another effect ?")) {
                            gameData.setEvent(null);
                            continue;
                        }
                        gameData.setEvent(null);
                    }
                    return data1;
                }
            } else if (command.matches("help")) {
                help();
            } else if (command.equals("cancel")) {
                return data;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    private void help() {

        Printer.print("activate");

        Select.help();

        System.out.println("""
                card show --selected
                help
                show board
                cancel""");

    }

    protected abstract boolean checkInvalidMoves(String command);

    public abstract ActivationData handleActivate();

}
