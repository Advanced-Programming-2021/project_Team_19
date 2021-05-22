package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Data.ActivationData;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public abstract class ActivateTrapWithNotification extends Activate {

    public ActivateTrapWithNotification(GameData gameData) {
        super(gameData);
    }

    public ActivationData run() {

        if(!canActionBeDone()){
            return null;
        }

        String command;

        ActivationData data = new ActivationData(null, "");

        while (true) {

            command = GetInput.getString();

            if (checkInvalidMoves(command)) {

            } else if (command.startsWith("select")) {
                new Select(gameData).select(command);
            } else if (command.matches("card show --selected")) {
                new Select(gameData).select(command);
            } else if (command.matches("activate effect")) {
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
            } else if (command.equals("show board")) {
                gameData.showBoard();
            } else if (command.equals("cancel")) {
                return data;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }


    protected ArrayList<String> getInvalidMoves() {

        ArrayList<String> answer = new ArrayList<>();

        answer.add("attack ([1-5])");
        answer.add("attack direct");
        answer.add("summon");
        answer.add("set");
        answer.add("set --position (attack|defence)");
        answer.add("flip summon");
        answer.add("next phase");

        return answer;
    }

    private void help() {

        Printer.print("activate effect");

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
