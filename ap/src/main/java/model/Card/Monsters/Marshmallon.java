package model.Card.Monsters;

import model.Card.EffectTypes.Defend;
import model.Card.Monster;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

public class Marshmallon extends Monster implements Defend {
    @Override
    public void handleDefend() {

    }

    public Marshmallon(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
