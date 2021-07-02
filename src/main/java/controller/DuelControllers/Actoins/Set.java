package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Phase;
import view.Printer.Printer;

import static view.Printer.Printer.print;

public class Set extends SummonAndSet {

    public Set(GameData gameData) {
        super(gameData, "set");
    }

    public String run() {
        return manageSetCard();
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
        } else if (card.getCardFamily().equals(CardFamily.TRAP) ||
                card.getCardFamily().equals(CardFamily.SPELL)) {
            if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
                return "spell card zone is full";
            }
        }
        return "set";
    }

    private String manageSetCard() {

        Card selectedCard = gameData.getSelectedCard();

//        String error = actionIsValid();
//
//        if(!error.equals("set")){
//            print(error);
//            return;
//        }

        if (selectedCard.getCardFamily().equals(CardFamily.MONSTER)) {
            return setMonster(selectedCard);
        } else {
            return setSpellOrTrap(selectedCard);
        }
    }

    private String setSpellOrTrap(Card card) {

        if (((SpellAndTraps) card).handleSet(gameData)) {

            activateOrSetCheckFieldSpell(card, gameData);

            return "set successfully";
        }

        return "set not successful";

    }


    private String setMonster(Card card) {

        Monster monster = (Monster) card;

        int numberOfSacrifices = monster.numberOfSacrifices(true, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData);

        if (numberOfSacrifices == 0) {
            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
            monster.handleSet(gameData);
            handleTriggerEffects();
            return "set successfully";
        }

//        sacrificeMonstersForSummonOrSet(gameData, monster.numberOfSacrifices(true, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData));

        return "sacrifice " + numberOfSacrifices + " monsters";

    }
}
