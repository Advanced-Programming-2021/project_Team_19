package model.Card.Spells;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.SpellsAndTraps.SpellTypes;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class Terraforming extends Spell {
    @Override
    public ActivationData activate(GameData gameData)  {
        ArrayList<Card> deckCards = gameData.getCurrentGamer().getGameBoard().getDeckZone().getMainDeckCards();
        ArrayList<Card> fieldSpellsInDeck = getFieldSpells(deckCards);

        if (fieldSpellsInDeck.isEmpty()){
            return new ActivationData(this, "you have no field cards in your deck");
        }

        if (selectCardAndMoveToHand(fieldSpellsInDeck))
            return new ActivationData(this, "spell activated successfully");
        return new ActivationData(this, "you cancelled the activation");
    }

    private boolean selectCardAndMoveToHand(ArrayList<Card> fieldSpellsInDeck) {
        String command;
        while (true){
            showSelectCard(fieldSpellsInDeck);
            command = GetInput.getString();
            if (command.matches("cancel")) {
                return false;
            }else if (command.matches("\\d+")){
                int id = Integer.parseInt(command);
                if (id >= fieldSpellsInDeck.size()){
                    Printer.print("please enter a valid id");
                }
                else {
                    moveToHandAndFinishSpell(fieldSpellsInDeck.get(id - 1));
                }
            }
        }
    }

    private void moveToHandAndFinishSpell(Card card) {

    }

    private void showSelectCard(ArrayList<Card> fieldSpellsInDeck) {
        Printer.print("enter an id to move a field spell to your hand");
        Utils.printArrayListOfCards(fieldSpellsInDeck);
    }

    private ArrayList<Card> getFieldSpells(ArrayList<Card> deckCards) {
        ArrayList<Card> toReturn = new ArrayList<>();
        for (Card card : deckCards) {
            if (card.getCardFamily().equals(CardFamily.SPELL) && ((Spell) card).getSpellType().equals(SpellTypes.FIELD))
                toReturn.add(card);
        }
        return toReturn;
    }
}
