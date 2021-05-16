package model.Card.Monsters;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.Printer.Printer;

import java.util.ArrayList;

public class RitualMonster extends Monster {

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

        if (getLevel() > getLevelSum(monstersInDeck)){
            Printer.print("you do not have enough monsters in your deck to summon this card");
            return;
        }
        Printer.print("choose some monster cards who's levels add up to " + getLevel() + " or more to discard from deck:");
        while (true){
            String command;
//            Utils.printArrayListOfCards(monstersInDeck);
        }
    }

    private int getLevelSum(ArrayList<Monster> monstersInDeck) {
        int toReturn = 0;
        for (Monster monster : monstersInDeck) {
            toReturn += monster.getLevel();
        }
        return toReturn;
    }
}
