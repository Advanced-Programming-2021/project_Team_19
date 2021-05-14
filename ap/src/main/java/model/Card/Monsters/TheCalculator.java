package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Board.MonsterCardZone;
import model.Card.Monster;
import model.Enums.CardMod;

public class TheCalculator extends Monster {

    @Override
    public int getAttack(GameData gameData) {
        return getCurrentAttack(gameData);
    }

    private int getCurrentAttack(GameData gameData){
        int attack = 0;
        MonsterCardZone attackerZone = gameData.getCurrentGamer().getGameBoard().getMonsterCardZone();
        for (int i = 0; i < 5; i++) {
            if (attackerZone.getCardById(i) != null && !((Monster)attackerZone.getCardById(i)).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                attack += ((Monster)attackerZone.getCardById(i)).getLevel() * 300;
            }
        }
        return attack;
    }
}
