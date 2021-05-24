package model.Card.Monsters;

import model.Card.Monster;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

public abstract class EffectMonster extends Monster {
    public EffectMonster(String name, String description, int price, int attack, int defence,
                         int level, Attribute attribute, MonsterType monsterType,
                         MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
        setEffectSpeed(1);
    }

    public boolean shouldEffectRun(EffectLabel label){
        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label){
        return null;
    }
}
