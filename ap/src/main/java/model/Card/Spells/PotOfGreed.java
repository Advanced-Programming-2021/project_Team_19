package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class PotOfGreed extends Spell {
    @Override
    public ActivationData activate(GameData gameData){
        if(gameData.getCurrentGamer().getGameBoard().getDeckZone().getSize() < 2){
            return new ActivationData(this, "There are not enough cards in the deck");
        }
        else{
            Card firstCard = gameData.getCurrentGamer().getGameBoard().getDeckZone().getCard(1);
            Card secondCard = gameData.getCurrentGamer().getGameBoard().getDeckZone().getCard(2);
            gameData.moveCardFromOneZoneToAnother(firstCard, gameData.getCurrentGamer().getGameBoard().getDeckZone(),
                    gameData.getCurrentGamer().getGameBoard().getHand());
            return new ActivationData(this, "Successfully added two cards from the deck!");
        }
    }

    public PotOfGreed(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }
}
