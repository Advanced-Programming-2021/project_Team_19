package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardMod;
import model.Phase;
import view.Printer.Printer;

public class FlipSummon extends Summon {

    public FlipSummon(GameData gameData) {
        super(gameData, "flip summon");
    }

    public void flipByCommand() {
        if (checkFlipSummonErrors())
            flip();
    }

    private boolean checkFlipSummonErrors() {

        if (summoningMonster == null) {
            Printer.print("no card is selected yet");
            return false;
        }
        if (!gameData.getCurrentGamer().getGameBoard().getMonsterCardZone()
                .containsCard(summoningMonster)) {
            Printer.print("you can’t change this card position");
            return false;
        }
        if (!gameData.getCurrentPhase().equals(Phase.MAIN1) &&
                !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            Printer.print("action not allowed in this phase");
            return false;
        }
        if (!((Monster) summoningMonster).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
            Printer.print("you can’t flip summon this card");
            return false;
        }
        if ((((Monster) summoningMonster).getTurnWasPutInMonsterZone() == gameData.getTurn())) {
            Printer.print("you can’t flip summon this card this turn");
            return false;
        }
        return true;
    }

    public void flip() {

        if (!((Monster) summoningMonster).handleFlip(gameData, CardMod.OFFENSIVE_OCCUPIED)) {
            return;
        }

        Printer.print("flip summoned successfully");

        handleTriggerEffects();
    }

}