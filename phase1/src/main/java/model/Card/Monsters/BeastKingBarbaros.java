package model.Card.Monsters;

import controller.DuelControllers.Actoins.Activation;
import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.GetInput;
import view.Printer.Printer;

public class BeastKingBarbaros extends EffectMonster {
    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData) {
        if (isForSetting)
            return sacrificesForSetting(cardsThatCanBeSacrificed);
        return sacrificesForSummoning(cardsThatCanBeSacrificed);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        if (numberOfSacrifices == 3) {
            new Activation(gameData).activate();
        }
    }

    @Override
    public ActivationData activate(GameData gameData) {
        gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);
        return null;
    }

    private int sacrificesForSummoning(int cardsThatCanBeSacrificed) {
        Printer.print("""
                how do you want to set this card:
                1- with 1900 ATK without sacrificing
                2- with 3000 ATK with 2 sacrifices
                3- with 3000 ATK with 3 sacrifices and destroying all enemy monsters""");
        while (true) {
            String command = GetInput.getString();
            switch (command) {
                case "1":
                    setAttack(1900);
                    return 0;
                case "2":
                    if (cardsThatCanBeSacrificed >= 2) {
                        setAttack(3000);
                        return 2;
                    } else {
                        printNotEnoughCards();
                    }
                    break;
                case "3":
                    if (cardsThatCanBeSacrificed >= 3) {
                        setAttack(3000);
                        return 3;
                    } else {
                        printNotEnoughCards();
                    }
                    break;
                case "cancel":
                    return -1;
                default:
                    Printer.printInvalidCommand();
                    break;
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
            switch (command) {
                case "1":
                    setAttack(1900);
                    return 0;
                case "2":
                    if (cardsThatCanBeSacrificed >= 2) {
                        setAttack(3000);
                        return 2;
                    } else {
                        printNotEnoughCards();
                    }
                    break;
                case "cancel":
                    return -1;
                default:
                    Printer.printInvalidCommand();
                    break;
            }
        }
    }

    private void printNotEnoughCards() {
        Printer.print("you do not have enough cards to tribute for this type of set/summon\n" +
                "enter another number: ");
    }

    public BeastKingBarbaros(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }


}
