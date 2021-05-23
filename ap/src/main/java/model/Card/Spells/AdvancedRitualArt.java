package model.Card.Spells;

import controller.DuelControllers.Actoins.RitualSummon;
import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.Icon;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Enums.Status;
import model.Enums.Type;
import model.Gamer;

import java.util.ArrayList;

import static controller.DuelControllers.Actoins.RitualSummon.getLevelSum;

public class AdvancedRitualArt extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        ArrayList<Card> monstersToTribute = new ArrayList<>(gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards());

        for (Card card : gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER))
                monstersToTribute.add(card);
        }


        if (!canRitualSummon(monstersToTribute, gameData)){
            return new ActivationData(null,"there is no way you could ritual summon a monster");
        }

        gameData.addActionToCurrentActions(new RitualSummon(gameData, this));
        handleCommonsForActivate(gameData);

        return new ActivationData(this, "you activated advanced ritual art, you should summon a ritual monster now");
    }


    private int getLowestRitualMonsterLevel(Gamer gamer) {
        int toReturn = 100;
        for (Card card : gamer.getGameBoard().getHand().getCardsInHand()) {
            if (((Monster) card).getEffectType().equals(MonsterTypesForEffects.RITUAL)) {
                toReturn = Integer.min(((Monster) card).getLevel(), toReturn);
            }
        }
        return toReturn;
    }

    private boolean canRitualSummon(ArrayList<Card> monstersInDeck, GameData gameData) {

        int minimumLevelOfRitualMonsters = getLowestRitualMonsterLevel(gameData.getCurrentGamer());

        return minimumLevelOfRitualMonsters <= getLevelSum(monstersInDeck) && minimumLevelOfRitualMonsters < 100;
    }



    public AdvancedRitualArt(String name, String description, int price, Type type, Icon icon, Status status) {
        super(name, description, price, type, icon, status);
    }
}
