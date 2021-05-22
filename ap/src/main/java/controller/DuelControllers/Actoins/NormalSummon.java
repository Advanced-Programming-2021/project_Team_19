package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Phase;
import view.Printer.Printer;

public class NormalSummon extends Summon {

    public NormalSummon(GameData gameData) {
        super(gameData, "normal summon");
    }

    public void run() {
        if (checkSummonErrors())
            summonMonster();
    }

    private boolean checkSummonErrors() {

        if (summoningMonster == null) {
            Printer.print("no card is selected yet");
            return false;
        }

        if (!gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand()
                .contains(summoningMonster) ||
                !summoningMonster.getCardFamily().equals(CardFamily.MONSTER) ||
                ((Monster) summoningMonster).getEffectType().equals(MonsterTypesForEffects.RITUAL) ^
                        gameData.isRitualSummoning()) {
            Printer.print("you can’t summon this card");
            return false;
        }

        if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.
                getCurrentPhase().equals(Phase.MAIN2)) {
            Printer.print("action not allowed in this phase");
            return false;
        }
        if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            Printer.print("monster card zone is full");
            return false;
        }
        if (gameData.getCurrentGamer().getLastTurnHasSummonedOrSet() == gameData.getTurn() &&
                !summoningMonster.getName().equals("Gate Guardian")) {
            Printer.print("you already summoned/set on this turn");
            return false;
        }
        return true;
    }

    private void summonMonster() {

        if (summoningMonster.getName().equals("Gate Guardian")) {
            if (sacrificeMonstersForSummonOrSet(gameData, 3)) {
                new SpecialSummon(gameData).run(gameData.getSelectedCard());
                Printer.print("summoned successfully");
            }
            return;
        }

        if (((Monster) summoningMonster).getEffectType().equals(MonsterTypesForEffects.RITUAL)) {
            ((RitualSummon) gameData.getRitualSummoning()).run((Monster) summoningMonster);
            handleTriggerEffects();
            return;
        }

        int numberOfSacrifices = ((Monster) summoningMonster).numberOfSacrifices
                (false, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards());

        if (sacrificeMonstersForSummonOrSet(gameData, numberOfSacrifices)) {

            if (!gameData.getSelectedCard().getName().equals("Gate Guardian"))
                gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());

            ((Monster) summoningMonster).handleSummon(gameData, numberOfSacrifices);
            Printer.print("summoned successfully");

            handleTriggerEffects();
        }

    }
}
