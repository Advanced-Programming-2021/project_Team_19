package model.Card.Monsters;

import controller.DuelControllers.Actoins.Activation;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Data.ActivationData;
import model.Enums.CardMod;
import model.Enums.GameEvent;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

import java.util.ArrayList;
import java.util.Collections;

public class ManEaterBug extends EffectMonster {

    @Override
    public boolean handleFlip(GameData gameData, CardMod cardMod) {

        setCardMod(cardMod);

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() == 0) {
            return false;
        }
        gameData.setEvent(GameEvent.MAN_EATER_BUG);

        new Activation(gameData, this).activate();
        return true;
    }

    @Override
    public ActivationData activate(GameData gameData) {
        ArrayList<Card> cards = new ArrayList<>(gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards());

        cards.removeAll(Collections.singleton(null));

        Card monsterToDestroy = Utils.askUserToSelectCard
                (cards, "select an enemy id to destroy:", null);
        gameData.setEvent(null);

        if (monsterToDestroy == null)
            return null;

        monsterToDestroy.handleDestroy(gameData);

        return null;
    }

    public ManEaterBug(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }


}
