package model.Card.Spells;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class Terraforming extends Spell {
    @Override
    public ActivationData activate(GameData gameData)  {
        ArrayList<Card> deckCards = gameData.getCurrentGamer().getGameBoard().getDeckZone().getMainDeckCards();
        ArrayList<Card> fieldSpellsInDeck = getFieldSpells(deckCards);

        if (fieldSpellsInDeck.isEmpty()){
            return new ActivationData(this, "you have no field cards in your deck");
        }

        if (selectCardAndMoveToHand(fieldSpellsInDeck, gameData))
            return new ActivationData(this, "spell activated successfully");
        return new ActivationData(this, "you cancelled the activation");
    }

    private boolean selectCardAndMoveToHand(ArrayList<Card> fieldSpellsInDeck, GameData gameData) {
        Card selectedCard = Utils.askUserToSelectCard(
                fieldSpellsInDeck,
                        "enter an id to move a field spell to your hand",
                        null);

        if (selectedCard == null)
            return false;
        moveToHandAndFinishSpell(selectedCard, gameData);
        return true;
    }

    private void moveToHandAndFinishSpell(Card card, GameData gameData) {
        gameData.moveCardFromOneZoneToAnother(card,
                gameData.getCurrentGamer().getGameBoard().getDeckZone(),
                gameData.getCurrentGamer().getGameBoard().getHand());
        handleDestroy(gameData);
    }

    private ArrayList<Card> getFieldSpells(ArrayList<Card> deckCards) {
        ArrayList<Card> toReturn = new ArrayList<>();
        for (Card card : deckCards) {
            if (card != null && card.getCardFamily().equals(CardFamily.SPELL) && ((Spell) card).getSpellType().equals(SpellTypes.FIELD))
                toReturn.add(card);
        }
        return toReturn;
    }

    public Terraforming(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }
}
