package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Phase;
import model.TriggerLabel;

public class FlipSummon extends Summon {

    public FlipSummon(GameData gameData) {
        super(gameData, "flip summon");
    }

    public String run() {
            return flip();
    }

    @Override
    public String actionIsValid() {

        if (summoningMonster == null) {
            return "no car is selected yet";
        }
        if (!gameData.getCurrentGamer().getGameBoard().getMonsterCardZone()
                .containsCard(summoningMonster)) {
            return "you can’t change this card position";
        }
        if (!gameData.getCurrentPhase().equals(Phase.MAIN1) &&
                !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            return "action not allowed in this phase";
        }
        if (!((Monster) summoningMonster).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
            return "you can’t flip summon this card";
        }
        if ((((Monster) summoningMonster).getTurnWasPutInMonsterZone() == gameData.getTurn())) {
            return "you can’t flip summon this card this turn";
        }
        return "flip summon";
    }

    public String flip() {

        if (!((Monster) summoningMonster).handleFlip(gameData, CardMod.OFFENSIVE_OCCUPIED)) {
            return "";
        }

        gameData.triggerLabel = new TriggerLabel(this);

        return "flip summoned successfully";

    }

}
