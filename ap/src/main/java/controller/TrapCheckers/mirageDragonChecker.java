package controller.TrapCheckers;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Enums.CardMod;

public class mirageDragonChecker extends Checker {

    public mirageDragonChecker(GameData gameData, SpellAndTraps card) {
        super(gameData, card);
    }

    public boolean check() {
        for (Monster monster : gameData.getOtherGamer
                (gameData.getCardController(card)).getGameBoard().getMonsterCardZone().getCards()) {
            if (monster != null && monster.getName().equals("Mirage Dragon")) {
                if (!monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                    return false;
                }
            }
        }
        return true;
    }
}
