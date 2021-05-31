package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;

public class SpecialSummon extends Summon {

    public SpecialSummon(GameData gameData) {

        super(gameData, "special summon");
    }

    public void run(Card card) {

        Monster monster = (Monster) card;
        monster.setCardMod(CardMod.OFFENSIVE_OCCUPIED);

        gameData.moveCardFromOneZoneToAnother(monster,
                gameData.getCurrentGamer().getGameBoard().getZone(monster),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());

        handleTriggerEffects();

    }

}
