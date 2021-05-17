package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.Actoins.RitualSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Spells.AdvancedRitualArt;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class RitualMonster extends Monster {

    private SpellAndTraps ritualSpell;

    {
        setEffectType(MonsterTypesForEffects.RITUAL);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        ArrayList<Card> cardsInDeck = gameData.getCurrentGamer().getGameBoard().getDeckZone().getMainDeckCards();
        ArrayList<Monster> monstersInDeck = new ArrayList<>();
        ArrayList<Monster> selectedMonsters = new ArrayList<>();

        for (Card card : cardsInDeck) {
            if (card.getCardFamily().equals(CardFamily.MONSTER))
                monstersInDeck.add((Monster) card);
        }

        if (!canRitualSummon(monstersInDeck, gameData)) {
            return;
        }

        Printer.print("choose some monster cards who's levels add up to " + getLevel() + " or more to discard from deck:");
        while (true) {
            String command;
            Utils.printArrayListOfMonsters(monstersInDeck);
            command = GetInput.getString();
            if (command.matches("camcel")) {
                break;
            } else if (command.matches("\\d+")) {
                int id = Integer.parseInt(command);
                if (id > monstersInDeck.size()) {
                    Printer.print("invalid id");
                } else {
                    selectedMonsters.add(monstersInDeck.get(id - 1));
                    monstersInDeck.remove(id - 1);
                }
            } else {
                Printer.printInvalidCommand();
            }
            if (getLevelSum(selectedMonsters) >= getLevel()) {
                summonAndDiscard(selectedMonsters, gameData);
            }
        }
    }

    private void summonAndDiscard(ArrayList<Monster> selectedMonsters, GameData gameData) {
        for (Monster selectedMonster : selectedMonsters) {
            new Destroy(gameData).run(selectedMonster, true);
        }
        new Destroy(gameData).run(ritualSpell, true);
        new RitualSummon(gameData).run(this);

    }

    private boolean canRitualSummon(ArrayList<Monster> monstersInDeck, GameData gameData) {
        if (getLevel() > getLevelSum(monstersInDeck)) {
            Printer.print("you do not have enough monsters in your deck to summon this card");
            return false;
        }
        for (SpellAndTraps spellOrTrap : gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if (spellOrTrap instanceof AdvancedRitualArt) {
                ritualSpell = spellOrTrap;
                return true;
            }
        }
        return false;
    }

    private int getLevelSum(ArrayList<Monster> monstersInDeck) {
        int toReturn = 0;
        for (Monster monster : monstersInDeck) {
            toReturn += monster.getLevel();
        }
        return toReturn;
    }
}
