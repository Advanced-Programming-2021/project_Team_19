package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

import static controller.DuelControllers.Actoins.RitualSummon.getLevelSum;

public class RitualMonster extends Monster {

    private SpellAndTraps ritualSpell;

    {
        setEffectType(MonsterTypesForEffects.RITUAL);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        gameData.moveCardFromOneZoneToAnother(this,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        setTurnWasPutInMonsterZone(gameData.getTurn());
    }

    public RitualMonster(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
