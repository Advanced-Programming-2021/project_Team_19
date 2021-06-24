package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

public class ShouldAskForActivateEffectMonster extends EffectMonster {

    protected int turnActivated = 0;

    public ShouldAskForActivateEffectMonster(String name, String description, int price, int attack,
                                             int defence, int level, Attribute attribute, MonsterType monsterType,
                                             MonsterTypesForEffects monsterTypesForEffects) {

        super(name, description, price, attack, defence, level, attribute, monsterType,
                monsterTypesForEffects);
    }

    public int getTurnActivated() {
        return turnActivated;
    }

    public void setTurnActivated(int turnActivated) {
        this.turnActivated = turnActivated;
    }

    public boolean canActivate(GameData gameData) {
        return false;
    }

    public ActivationData activate(GameData gameData) {
        return null;
    }
}
