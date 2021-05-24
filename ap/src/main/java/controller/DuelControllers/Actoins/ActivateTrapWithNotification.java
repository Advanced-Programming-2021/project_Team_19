package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Data.ActivationData;
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

            command = GetInput.getString();

            if (checkInvalidMoves(command)) {

            }else if(Utils.handleSelect(gameData, command)){

            } else if (command.matches("activate")) {
                ActivationData data1 = handleActivate();
                Printer.print(data1.message);
                if (data1.activatedCard != null) {
                    if (this instanceof ActivateSpeedEffect) {
                        if (Utils.askForConfirmation("do you want to activate another effect ?")) {
                            continue;
                        }
                    }
                    return data1;
                }
            }
            else if (command.matches("help")) {
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
