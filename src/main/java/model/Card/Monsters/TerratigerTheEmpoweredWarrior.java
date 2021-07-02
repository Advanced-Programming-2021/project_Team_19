package model.Card.Monsters;

import controller.DuelControllers.Actions.Activation;
import controller.DuelControllers.Actions.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.Printer.Printer;

import java.util.ArrayList;

public class TerratigerTheEmpoweredWarrior extends EffectMonster {
    private ArrayList<Card> level4OrLessMonstersInHand;

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();

        level4OrLessMonstersInHand = getLevel4OrLessMonstersInHand(hand);

        if (level4OrLessMonstersInHand.size() != 0 && !gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            new Activation(gameData, this).activate();
        } else {
            Printer.print("your monster zone is full and you cannot special summon a monster");
        }
    }

    @Override
    public ActivationData activate(GameData gameData) {
        Card selectedCard = Utils.askUserToSelectCard(level4OrLessMonstersInHand,
                "choose a monster to special summon:",
                null);

        if (selectedCard == null)
            return null;

        new SpecialSummon(gameData).run(selectedCard);
        ((Monster) selectedCard).setCardMod(CardMod.DEFENSIVE_OCCUPIED);
        return null;
    }


    private ArrayList<Card> getLevel4OrLessMonstersInHand(Hand hand) {
        ArrayList<Card> toReturn = new ArrayList<>();
        for (Card card : hand.getCardsInHand()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER) && ((Monster) card).getLevel() <= 4)
                toReturn.add(card);
        }
        return toReturn;
    }

    public TerratigerTheEmpoweredWarrior(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }

}
