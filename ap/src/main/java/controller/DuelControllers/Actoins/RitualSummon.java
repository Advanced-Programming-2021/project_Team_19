package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Monster;

public class RitualSummon extends Summon{
    public RitualSummon(GameData gameData) {
        super(gameData, "Ritual Summon");
    }

    public void run(Monster monster){
        gameData.moveCardFromOneZoneToAnother(monster,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());

    }


}
