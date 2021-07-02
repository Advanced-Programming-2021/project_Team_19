package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Phase;
import view.Printer.Printer;

public class NormalSummon extends Summon {

    public NormalSummon(GameData gameData) {
        super(gameData, "normal summon");
    }

    public String run() {
//        if (actionIsValid().equals("normal summon"))
        return summonMonster();

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
        return "normal summon";
    }

    private String summonMonster() {

//        if (summoningMonster.getName().equals("Gate Guardian")) {
//            if (sacrificeMonstersForSummonOrSet(gameData, 3)) {
//                new SpecialSummon(gameData).run(gameData.getSelectedCard());
//                Printer.print("summoned successfully");
//            }
//            return "summoned successfully";
//        }

//        if (((Monster) summoningMonster).getEffectType().equals(MonsterTypesForEffects.RITUAL)) {
//            ((RitualSummon) gameData.getRitualSummoning()).run((Monster) summoningMonster);
//            handleTriggerEffects();
//            return "summoned successfully";
//        }

        int numberOfSacrifices = ((Monster) summoningMonster).numberOfSacrifices
                (false, gameData.getCurrentGamer().
                        getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);

        if (numberOfSacrifices == 0){
            ((Monster) summoningMonster).handleSummon(gameData, numberOfSacrifices);
            handleTriggerEffects();
            return "summoned successfully";
        }

        return "sacrifice " + numberOfSacrifices + " monsters";

//        if (sacrificeMonstersForSummonOrSet(gameData, numberOfSacrifices)) {
//            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
//
//            ((Monster) summoningMonster).handleSummon(gameData, numberOfSacrifices);
//
//            handleTriggerEffects();
//            return "summoned successfully";
//        }

    }
}
