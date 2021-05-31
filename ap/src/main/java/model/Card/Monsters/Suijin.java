package model.Card.Monsters;

import model.Card.Monster;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

public class Suijin extends Monster {

    public Suijin(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }
}
