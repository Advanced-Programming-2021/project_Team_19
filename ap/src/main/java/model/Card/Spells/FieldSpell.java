package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.MonsterType;

import java.util.HashMap;

public class FieldSpell extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    protected HashMap<MonsterType, Integer[]> typesAndAmountToChangeAttackAndDefence = new HashMap<>();

    public int attackDifference(MonsterType monsterType, GameData gameData){
        if (!typesAndAmountToChangeAttackAndDefence.containsKey(monsterType)){
            return 0;
        }
        return typesAndAmountToChangeAttackAndDefence.get(monsterType)[0];

    }

    public int defenceDifference(MonsterType monsterType){
        if (!typesAndAmountToChangeAttackAndDefence.containsKey(monsterType)){
            return 0;
        }
        return typesAndAmountToChangeAttackAndDefence.get(monsterType)[1];

    }
}
