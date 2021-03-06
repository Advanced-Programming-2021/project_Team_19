package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Phase;
import model.TriggerLabel;

public class NormalSummon extends Summon {

    public NormalSummon(GameData gameData) {
        super(gameData, "normal summon");
    }

    public String[] run(String ids) {
        return new String[]{summonMonster(ids), String.valueOf(currentId)};
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
        }

        int numberOfSacrifices = ((Monster) summoningMonster).numberOfSacrifices
                (false, gameData.getCurrentGamer().
                        getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);


        if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            return "action not allowed in this phase";
        } else if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull() &&
                numberOfSacrifices != 0) {
            return "monster card zone is full";
        } else if (gameData.getCurrentGamer().getLastTurnHasSummonedOrSet() == gameData.getTurn() &&
                !summoningMonster.getName().equals("Gate Guardian")) {
            return "you already summoned/set on this turn";
        }


        if (numberOfSacrifices == 0) {
            return "normal summon";
        } else if (numberOfSacrifices <= gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards()) {
            return "get " + numberOfSacrifices + " monsters";
        }
        return "not enough cards for tribute";
    }

    private String summonMonster(String ids) {

        gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
        currentId = gameData.getCurrentGamer().getGameBoard().getHand().getId(summoningMonster);

        if (ids == null) {
            ((Monster) summoningMonster).handleSummon(gameData, 0);
            gameData.triggerLabel = new TriggerLabel(this);
            return "summon " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(summoningMonster);
        } else {
            sacrificeByIds(ids);
            ((Monster) summoningMonster).handleSummon(gameData, (ids.length() + 1) / 2);
            gameData.triggerLabel = new TriggerLabel(this);
            return "summon " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(summoningMonster) +
                    " sacrifice " + ids;
        }
    }

    public static boolean canSacrifice(GameData gameData) {
        return gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(gameData.getSelectedCard());
    }
}
