package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import view.GetInput;
import view.Printer.Printer;

public class ManEaterBug extends Monster {

    public boolean handleFlip(GameData gameData) {

        setCardMod(CardMod.OFFENSIVE_OCCUPIED);

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() != 0) {
            String command;
            Printer.print("you have activated Man-Eater Bug\n" +
                    "select an enemy monster id to destroy: ");
            while (true) {
                command = GetInput.getString();

                if (command.matches("[1-5]")) {
                    if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(Integer.parseInt(command)) == null){
                        Printer.print("there is no monster here\n" +
                                "enter an id that contains a monster");
                    }else{
                        new Destroy(gameData).run(gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(Integer.parseInt(command)), false);
                        return true;
                    }
                } else if (command.matches("//d+")) {
                    Printer.print("please enter a valid id");
                } else {
                    Printer.printInvalidCommand();
                }
            }
        }
        return true;
    }

}
