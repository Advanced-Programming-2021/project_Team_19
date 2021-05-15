package controller.DuelControllers.Actoins;

import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.CardFamily;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public abstract class ActivateTriggerTrapEffect extends Activate {

    Action activatorAction;

    public ActivateTriggerTrapEffect(Action action) {
        super(action.getGameData());
        activatorAction = action;
    }

    public Action getActivatorAction() {
        return activatorAction;
    }

    public TriggerActivationData run() {

        String command;

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        while (true) {

            command = GetInput.getString();

            if (checkInvalidMoves(command)) {

            } else if (command.startsWith("select")) {
                new Select(gameData).select(command);
            } else if (command.matches("card show --selected")) {
                new Select(gameData).select(command);
            } else if (command.matches("activate effect")) {

                TriggerActivationData data1 = activateTrapOrSpell(activatorAction);

                Printer.print(data1.message);

                if (data.activatedCard != null) {
                    return data1;
                }

            } else if (command.matches("help")) {
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

    private TriggerActivationData activateTrapOrSpell(Action action) {

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        Card card = gameData.getSelectedCard();

        if (Utils.IsSelectedCardIsNull(gameData)) {
            data.message = "no card is selected yet";
            return data;
        }

        if (gameData.getSelectedCard().getCardFamily().equals(CardFamily.TRAP) ||
                gameData.getSelectedCard().getCardFamily().equals(CardFamily.SPELL)) {
            Printer.print("activate effect is only for spell and trap cards");
            data.message = "activate effect is only for spell and trap cards";
            return data;
        }

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)) {
            Printer.print("you can't activate this card");
            data.message = "you can't activate this card";
            return data;
        }

        activatedCard = (SpellAndTraps) card;

        if (activatedCard.wasActivated) {
            Printer.print("you have already activated this card");
            data.message = "you have already activated this card";
            return data;
        }

        if (!activatedCard.canActivateBecauseOfAnAction(action)) {
            Printer.print("you can't activate this card");
            data.message = "you can't activate this card";
            return data;
        }

        if (activatedCard.canActivateBecauseOfAnAction(action)) {

            Utils.deselectCard(gameData);
            return (TriggerActivationData) activatedCard.activate(gameData);

        }

        return data;

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

}
