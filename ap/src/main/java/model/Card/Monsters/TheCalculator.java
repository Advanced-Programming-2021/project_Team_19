package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Board.MonsterCardZone;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;

public class TheCalculator extends Monster {

    @Override
    public int getAttack(GameData gameData) {
        return getCurrentAttack(gameData);
    }

    private int getCurrentAttack(GameData gameData){
        Gamer gamer = gameData.getCurrentGamer();
        if (gameData.getSecondGamer().getGameBoard().getMonsterCardZone().containsCard(this)){
            gamer = gameData.getSecondGamer();
        }
        int attack = 0;
        MonsterCardZone attackerZone = gamer.getGameBoard().getMonsterCardZone();
        for (int i = 1; i < 6; i++) {
            if (attackerZone.getCardById(i) != null && !((Monster)attackerZone.getCardById(i)).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                attack += ((Monster)attackerZone.getCardById(i)).getLevel() * 300;
            }
        }
        return attack;
    }

    public TheCalculator(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
