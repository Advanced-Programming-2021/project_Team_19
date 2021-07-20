package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Phase;

public class Set extends SummonAndSet {

    public Set(GameData gameData) {
        super(gameData, "set");
    }

    public String[] run(String ids) {
        return new String[]{manageSetCard(ids), String.valueOf(currentId)};
    }

    @Override
    public String actionIsValid() {

        Card card = gameData.getSelectedCard();

        if (card == null) {
            return "no card is selected yet";
        } else if (!gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().contains(card) ||
                (card.getCardFamily().equals(CardFamily.SPELL) && ((Spell) card).getSpellType().equals(SpellTypes.FIELD))) {
            return "you can’t set this card";
        } else if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            return "action not allowed in this phase";
        } else if (card.getCardFamily().equals(CardFamily.MONSTER)) {
            if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
                return "monster card zone is full";
            } else if (card.getName().equals("Gate Guardian")) {
                return "you cannot set gate guardian";
            } else if (gameData.getCurrentGamer().getLastTurnHasSummonedOrSet() == gameData.getTurn()) {
                return "you already summoned/set on this turn";
            }

            int numberOfSacrifices = ((Monster) card).numberOfSacrifices(true, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);

            if (numberOfSacrifices == 0) {
                return "set";
            } else if (numberOfSacrifices <= gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards()) {
                return "get " + numberOfSacrifices + " monsters";
            }
            return "not enough cards for tribute";


        } else if (card.getCardFamily().equals(CardFamily.TRAP) ||
                card.getCardFamily().equals(CardFamily.SPELL)) {
            if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
                return "spell card zone is full";
            }
        }
        return "set";
    }

    private String manageSetCard(String ids) {

        Card selectedCard = gameData.getSelectedCard();

        if (selectedCard.getCardFamily().equals(CardFamily.MONSTER)) {
            return setMonster(selectedCard, ids);
        } else {
            return setSpellOrTrap(selectedCard);
        }
    }

    private String setSpellOrTrap(Card card) {

        if (((SpellAndTraps) card).handleSet(gameData)) {

            currentId = gameData.getCurrentGamer().getGameBoard().getHand().getId(card);

            activateOrSetCheckFieldSpell(card, gameData);

            return "set spell " + gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(card);
        }

        return "set not successful";

    }


    private String setMonster(Card card, String ids) {

        Monster monster = (Monster) card;

        gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
        currentId = gameData.getCurrentGamer().getGameBoard().getHand().getId(card);

        if (ids == null) {
            monster.handleSet(gameData);
            handleTriggerEffects();
            return "set monster " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(card);
        } else {
            sacrificeByIds(ids);
            monster.handleSet(gameData);
            handleTriggerEffects();
            return "set monster " + gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(monster) +
                    " sacrifice " + ids;
        }
    }
}
