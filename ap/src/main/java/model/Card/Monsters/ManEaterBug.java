package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ManEaterBug extends Monster {

    public boolean handleFlip(GameData gameData) {

        setCardMod(CardMod.OFFENSIVE_OCCUPIED);

        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getNumberOfCards() == 0) {
            return false;
        }
        ArrayList<Card> cards = new ArrayList<>(gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards());

        cards.removeAll(Collections.singleton(null));

        Card monsterToDestroy = Utils.askUserToSelectCard
                (cards, "select an enemy id to destroy:", null);

        if (monsterToDestroy == null)
            return false;

        monsterToDestroy.handleDestroy(gameData);

        return true;
    }

    public ManEaterBug(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }

}
