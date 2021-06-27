package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Collections;

public class RitualSummon extends Summon {
    public Spell advancedRitualArt;

    public RitualSummon(GameData gameData, Card card) {
        super(gameData, "Ritual Summon");
        this.advancedRitualArt = (Spell) card;
    }

    public void run(Monster monster) {
        ArrayList<Card> monstersToTribute = new ArrayList<>(gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards());
        ArrayList<Card> selectedMonsters = new ArrayList<>();


        for (Card card : gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER) && !card.equals(gameData.getSelectedCard()))
                monstersToTribute.add(card);
        }

        monstersToTribute.removeAll(Collections.singleton(null));

        gameData.removeRitualSummoning();

        Printer.print("choose some monster cards who's levels add up to " + monster.getLevel() + " or more to discard from hand or monster card zone:");
        while (true) {
            String command;
            Utils.printArrayListOfCards(monstersToTribute);
            command = GetInput.getString();
            if (command.matches("cancel")) {
                Printer.print("you cancelled the ritual summon");
                break;
            } else if (command.matches("\\d+")) {
                int id = Integer.parseInt(command);
                if (id > monstersToTribute.size()) {
                    Printer.print("invalid id");
                } else {
                    selectedMonsters.add(monstersToTribute.get(id - 1));
                    monstersToTribute.remove(id - 1);
                }
            } else {
                Printer.printInvalidCommand();
            }
            if (getLevelSum(selectedMonsters) >= monster.getLevel()) {
                if (setOffenseDefence(monster)) {
                    summonAndDiscard(selectedMonsters, gameData, monster);
                    Printer.print("you successfully ritual summoned " + gameData.getSelectedCard().getName());
                }
                break;
            }
        }

    }

    private boolean setOffenseDefence(Monster monster) {
        Printer.print("do you want to summon defencive or offensive?");
        while (true) {
            String command;
            command = GetInput.getString();
            if (command.matches("cancel")) {
                Printer.print("you cancelled the ritual summon");

                return false;
            } else if (command.matches("offensive")) {
                monster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
                return true;
            } else if (command.matches("defencive")) {
                monster.setCardMod(CardMod.DEFENSIVE_OCCUPIED);
                return true;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    public static int getLevelSum(ArrayList<Card> monstersInDeck) {
        int toReturn = 0;
        for (Card card : monstersInDeck) {
            if (card != null)
                toReturn += ((Monster) card).getLevel();
        }
        return toReturn;
    }

    private void summonAndDiscard(ArrayList<Card> selectedMonsters, GameData gameData, Monster monster) {

        for (Card card : selectedMonsters) {
            gameData.moveCardFromOneZoneToAnother(card,
                    gameData.getCurrentGamer().getGameBoard().getZone(card),
                    gameData.getCurrentGamer().getGameBoard().getGraveYard());
        }

        gameData.moveCardFromOneZoneToAnother(advancedRitualArt,
                gameData.getCurrentGamer().getGameBoard().getZone(advancedRitualArt),
                gameData.getCurrentGamer().getGameBoard().getGraveYard());

        monster.handleSummon(gameData, 0);

    }


}
