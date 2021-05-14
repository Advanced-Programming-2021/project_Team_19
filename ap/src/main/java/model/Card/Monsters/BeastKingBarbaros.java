package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import view.GetInput;
import view.Printer.Printer;

public class BeastKingBarbaros extends Monster {
    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed) {
        if (isForSetting)
            return sacrificesForSetting(cardsThatCanBeSacrificed);
        return sacrificesForSummoning(cardsThatCanBeSacrificed);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        if (numberOfSacrifices == 3) {
            for (int i = 1; i < 6; i++) {
                if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i) != null)
                    gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(i).handleDestroy(gameData);
            }
        }
        super.handleSummon(gameData, numberOfSacrifices);
    }

    private int sacrificesForSummoning(int cardsThatCanBeSacrificed) {
        Printer.print("""
                how do you want to set this card:
                1- with 1900 ATK without sacrificing
                2- with 3000 ATK with 2 sacrifices
                3- with 3000 ATK with 3 sacrifices and destroying all enemy monsters""");
        while (true) {
            String command = GetInput.getString();
            if (command.equals("1")) {
                setAttack(1900);
                return 0;
            } else if (command.equals("2")) {
                if (cardsThatCanBeSacrificed >= 2) {
                    setAttack(3000);
                    return 2;
                } else {
                    printNotEnoughCards();
                }
            } else if (command.equals("3")) {
                if (cardsThatCanBeSacrificed >= 3) {
                    setAttack(3000);
                    return 3;
                } else {
                    printNotEnoughCards();
                }
            } else if (command.equals("cancel")) {
                return -1;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    private int sacrificesForSetting(int cardsThatCanBeSacrificed) {
        Printer.print("""
                how do you want to set this card:
                1- with 1900 ATK without sacrificing
                2- with 3000 ATK with 2 sacrifices""");
        while (true) {
            String command = GetInput.getString();
            if (command.equals("1")) {
                setAttack(1900);
                return 0;
            } else if (command.equals("2")) {
                if (cardsThatCanBeSacrificed >= 2) {
                    setAttack(3000);
                    return 2;
                } else {
                    printNotEnoughCards();
                }
            } else if (command.equals("cancel")) {
                return -1;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    private void printNotEnoughCards() {
        Printer.print("you do not have enough cards to tribute for this type of set/summon\n" +
                "enter another number: ");
    }
}
