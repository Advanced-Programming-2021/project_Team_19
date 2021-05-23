package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.Printer.Printer;

public class GateGuardian extends Monster {

    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData) {
        if (isForSetting){
            Printer.print("you cannot set this monster");
            return -1;
        }
        return 3;
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn() - 1);
    }

    public GateGuardian(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
