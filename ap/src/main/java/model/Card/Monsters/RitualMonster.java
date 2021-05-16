package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.MonsterEnums.MonsterTypesForEffects;

public class RitualMonster extends Monster {

    {
        setEffectType(MonsterTypesForEffects.RITUAL);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {

    }
}
