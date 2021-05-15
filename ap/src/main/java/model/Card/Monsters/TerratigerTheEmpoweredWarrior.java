package model.Card.Monsters;

import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class TerratigerTheEmpoweredWarrior extends Monster {
    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();
        if (getLevel4OrLessMonstersInHand(hand).size() != 0) {
            specialSummonLevel4OrLessCard(gameData, getLevel4OrLessMonstersInHand(hand));
        }
    }

    private void specialSummonLevel4OrLessCard(GameData gameData, ArrayList<Monster> level4OrLessMonstersInHand) {
        showCardsToChooseFrom(level4OrLessMonstersInHand);
        boolean effectNotDone = true;
        String command;

        while (effectNotDone) {
            command = GetInput.getString();
            if (command.matches("\\d+")) {
                if (Integer.parseInt(command) > level4OrLessMonstersInHand.size()){
                    Printer.print("invalid id");
                    showCardsToChooseFrom(level4OrLessMonstersInHand);
                } else {
                    Monster monsterToSpecialSummon = level4OrLessMonstersInHand.get(Integer.parseInt(command));
                    new SpecialSummon(gameData).run(monsterToSpecialSummon);
                    monsterToSpecialSummon.setCardMod(CardMod.DEFENSIVE_OCCUPIED);
                }
            } else if (command.matches("cancel")) {
                effectNotDone = false;
            } else {
                Printer.printInvalidCommand();
            }

        }

    }

    private ArrayList<Monster> getLevel4OrLessMonstersInHand(Hand hand) {
        ArrayList<Monster> toReturn = new ArrayList<>();
        for (Card card : hand.getCardsInHand()) {
            if (card instanceof Monster && ((Monster) card).getLevel() <= 4)
                toReturn.add(((Monster) card));
        }
        return toReturn;
    }

    private void showCardsToChooseFrom(ArrayList<Monster> level4OrLessMonstersInHand) {
        Printer.print("choose a monster to special summon:");
        for (int i = 1; i <= level4OrLessMonstersInHand.size(); i++) {
            Printer.print(i + "- " + level4OrLessMonstersInHand.get(i - 1).toString());
        }
    }
}
