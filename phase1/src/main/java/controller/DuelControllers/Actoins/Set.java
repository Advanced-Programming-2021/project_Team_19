package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Spell;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Phase;
import view.Printer.Printer;

public class Set extends SummonAndSet {

    public Set(GameData gameData) {
        super(gameData, "set");
    }

    public void run() {
        manageSetCard();
    }

    private void manageSetCard() {

        Card selectedCard = gameData.getSelectedCard();

        if (selectedCard == null) {
            Printer.print("no card is selected yet");
        } else if (!gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().contains(selectedCard) ||
                (selectedCard.getCardFamily().equals(CardFamily.SPELL) && ((Spell) selectedCard).getSpellType().equals(SpellTypes.FIELD))) {
            Printer.print("you can’t set this card");
        } else if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            Printer.print("action not allowed in this phase");
        } else if (selectedCard.getCardFamily().equals(CardFamily.MONSTER)) {
            setMonster(selectedCard);
        } else if (selectedCard.getCardFamily().equals(CardFamily.TRAP) ||
                selectedCard.getCardFamily().equals(CardFamily.SPELL)) {
            setSpellOrTrap(selectedCard);
        }

    }

    private void setSpellOrTrap(Card card) {

        if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
            Printer.print("spell card zone is full");
            return;
        }

        if (((SpellAndTraps) card).handleSet(gameData)) {

            activateOrSetCheckFieldSpell(card, gameData);

            Printer.print("set successfully");
        }

    }


    private void setMonster(Card card) {

        if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            Printer.print("monster card zone is full");
            return;
        } else if (gameData.getCurrentGamer().getLastTurnHasSummonedOrSet() == gameData.getTurn()) {
            Printer.print("you already summoned/set on this turn");
            return;
        }

        Monster monster = (Monster) card;

        if (sacrificeMonstersForSummonOrSet(gameData, monster.numberOfSacrifices(true, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getNumberOfCards(), gameData))) {
            gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn());
            monster.handleSet(gameData);
            Printer.print("set successfully");
            handleTriggerEffects();
        }

    }
}
