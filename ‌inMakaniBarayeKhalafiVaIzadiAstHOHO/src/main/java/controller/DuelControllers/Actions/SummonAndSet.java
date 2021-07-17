package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Board.MonsterCardZone;
import model.Card.Monster;
import model.Gamer;

public class SummonAndSet extends Action {

    protected SummonAndSet(GameData gameData, String actionName) {
        super(gameData, actionName);
    }

    public void sacrificeByIds(String ids){
        Gamer gamer = gameData.getCurrentGamer();
        MonsterCardZone monsterCardZone = gamer.getGameBoard().getMonsterCardZone();
        sacrificeMonsters(ids.split(" "), monsterCardZone, gamer, gameData);
    }

    private void sacrificeMonsters(String[] ids, MonsterCardZone monsterCardZone, Gamer gamer, GameData gameData) {
        for (String id : ids) {
            ((Monster) monsterCardZone.getCardById(Integer.parseInt(id))).sacrifice(gameData, gamer);
        }
    }

}
