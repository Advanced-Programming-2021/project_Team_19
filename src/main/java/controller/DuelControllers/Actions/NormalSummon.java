package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Phase;

public class NormalSummon extends Summon {

    public NormalSummon(GameData gameData) {
        super(gameData, "normal summon");
    }

    public String run(String ids) {

        return summonMonster(ids);

    }

    @Override
    public String actionIsValid() {

        Card card = gameData.getSelectedCard();

        if (card == null) {
            return "no card is selected yet";
        } else if (!gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand()
                .contains(summoningMonster) ||
                !summoningMonster.getCardFamily().equals(CardFamily.MONSTER) ||
                ((Monster) summoningMonster).getEffectType().equals(MonsterTypesForEffects.RITUAL) ^
                        gameData.isRitualSummoning()) {
            return "you can’t summon this card";
        } else if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            return "action not allowed in this phase";
        } else if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            return "monster card zone is full";
        } else if (gameData.getCurrentGamer().getLastTurnHasSummonedOrSet() == gameData.getTurn() &&
                !summoningMonster.getName().equals("Gate Guardian")) {
            return "you already summoned/set on this turn";
        }

        int numberOfSacrifices = ((Monster) summoningMonster).numberOfSacrifices
                (false, gameData.getCurrentGamer().
                        getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);

        if (numberOfSacrifices == 0) {
            return "normal summon";
        } else if (numberOfSacrifices <= gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards()) {
            return "get " + numberOfSacrifices + " monsters";
        }
        return "not enough cards for tribute";
    }

    private String summonMonster(String ids) {

//        if (summoningMonster.getName().equals("Gate Guardian")) {
//            if (sacrificeMonstersForSummonOrSet(gameData, 3)) {
//                new SpecialSummon(gameData).testRun(gameData.getSelectedCard());
//                Printer.print("summoned successfully");
//            }
//            return "summoned successfully";
//        }

//        if (((Monster) summoningMonster).getEffectType().equals(MonsterTypesForEffects.RITUAL)) {
//            ((RitualSummon) gameData.getRitualSummoning()).testRun((Monster) summoningMonster);
//            handleTriggerEffects();
//            return "summoned successfully";
//        }

        if (ids == null) {
            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
            ((Monster) summoningMonster).handleSummon(gameData, 0);
            handleTriggerEffects();
            return "summon " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(summoningMonster);
        } else {
            sacrificeByIds(ids);
            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
            ((Monster) summoningMonster).handleSummon(gameData, (ids.length() + 1) / 2);
            return "summon " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(summoningMonster) +
                    " sacrifice " + ids;
        }


//        if (sacrificeMonstersForSummonOrSet(gameData, (ids.length() + 1) / 2)) {
//            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
//
//            ((Monster) summoningMonster).handleSummon(gameData, numberOfSacrifices);
//
//            handleTriggerEffects();
//            return "summoned successfully";
//        }

    }

    public boolean canSacrifice() {
        return gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(gameData.getSelectedCard());
    }
}
