package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public abstract class ActivateTrapBecauseOfAnAction extends Action{

    Action action;

    public ActivateTrapBecauseOfAnAction(Action action){
        super(action.getGameData(), "activate");
        this.action = action;
    }

    public boolean run(){

        String command;

        while(true) {

            command = GetInput.getString();

            if (checkInvalidMoves(command)) {
                break;
            }
            else if (command.startsWith("select")) {
                new Select(gameData).select(command);
            } else if (command.matches("card show --selected")) {
                new Select(gameData).select(command);
            } else if (command.matches("activate effect")) {
                if(activateTrapOrSpell(action)){
                    return true;
                }
            } else if (command.matches("help")) {
                help();
            } else if (command.equals("show board")) {
                gameData.showBoard();
            } else if(command.equals("cancel")){
                break;
            }
            else {
                Printer.printInvalidCommand();
            }
        }

        return false;
    }

    private boolean activateTrapOrSpell(Action action) {

        Card card = gameData.getSelectedCard();

        if (Utils.IsSelectedCardIsNull(gameData)) {
            Printer.print("no card is selected yet");
            return false;
        }

        if(gameData.getSelectedCard().getCardFamily().equals(CardFamily.TRAP) ||
                gameData.getSelectedCard().getCardFamily().equals(CardFamily.SPELL)){
            Printer.print("activate effect is only for spell and trap cards");
            return false;
        }

        if(!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)){
            Printer.print("you can't activate this card");
            return false;
        }

        SpellAndTraps trap = (SpellAndTraps)card;

        if(trap.wasActivated){
            Printer.print("you have already activated this card");
            return false;
        }

        if(!trap.canActivateBecauseOfAnAction(action)){
            Printer.print("you can't activate this card");
            return false;
        }

        if(trap.canActivateBecauseOfAnAction(action)){
           trap.activate(gameData);
           gameData.setSelectedCard(null);
           Utils.deselectCard(gameData);
           return true;
        }

        return false;

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

    protected ArrayList<String> getInvalidMoves(){

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
