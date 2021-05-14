package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Phase;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateTrapOnOtherPlayerTurn extends Action {

    public ActivateTrapOnOtherPlayerTurn(GameData gameData){
        super(gameData, "activeTrap");
    }

    public void run(){

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
                if(activateTrapOrSpell()){
                    return;
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
    }

    private boolean activateTrapOrSpell() {

        Card card = gameData.getSelectedCard();

        if (gameData.getSelectedCard() == null) {
            Printer.print("no card is selected yet");
            return false;
        }

        if(gameData.getSelectedCard().getCardFamily().equals(CardFamily.TRAP) ||
                gameData.getSelectedCard().getCardFamily().equals(CardFamily.SPELL)){
            Printer.print("activate effect is only for spell and trap cards");
            return false;
        }

        if(((SpellAndTraps)card).wasActivated){
            Printer.print("you have already activated this card");
            return false;
        }

        if(!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)){
            Printer.print("you can't activate this card");
            return false;
        }

        if(!((SpellAndTraps)card).canActivate(gameData)){
            Printer.print("you can't activate this card");
            return false;
        }

        ((SpellAndTraps)card).activate(gameData);

        return true;

    }

    private void help() {

        Printer.print("activate effect");

        Select.help();

        System.out.println("""
                card show --selected
                help
                show board
                finish turn
                cancel""");

    }

    private static boolean checkInvalidMoves(String command){

        for(String str : getInvalidMoves()){
            if(command.matches(str)){
                Printer.print("it’s not your turn to play this kind of moves");
                return true;
            }
        }
        return false;
    }

    private static ArrayList<String> getInvalidMoves(){
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
