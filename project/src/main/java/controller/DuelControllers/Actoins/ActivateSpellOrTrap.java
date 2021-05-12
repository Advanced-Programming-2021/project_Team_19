package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Board.Zones;
import model.Card.SpellAndTraps;
import model.Enums.CardFamily;
import model.Enums.ZonesEnum;
import model.Phase;
import view.Printer.Printer;

public class ActivateSpellOrTrap extends Action {

    public ActivateSpellOrTrap(GameData gameData){
        super(gameData, "activate");
    }

    public void run(){

        if(!checkSomeErrors()){
            return;
        }

        SpellAndTraps card = (SpellAndTraps) gameData.getSelectedCard();

        if(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand){
            if(activateFromHand()) return;
        }

        else if(gameData.getCurrentGamer().getGameBoard().getZone(card)
                instanceof SpellAndTrapCardZone){
            if(!activateFromSpellZone()) return;
        }

        else{
            Printer.print("invalid Zone");
            return;
        }

        Printer.print("spell/trap activated");

    }

    private boolean activateFromSpellZone() {
        SpellAndTraps card = (SpellAndTraps) gameData.getSelectedCard();
        card.activate();
        return true;
    }

    private boolean activateFromHand(){

        SpellAndTraps card = (SpellAndTraps) gameData.getSelectedCard();

        if(gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()){
            Printer.print("spell card zone is full");
            return false;
        }
        if(!card.canActivate(gameData)){
            Printer.print("preparations of this spell are not done yet");
            return false;
        }

        card.activate();

        gameData.moveCardFromOneZoneToAnother(card,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone());

        return true;

    }

    private boolean checkSomeErrors(){

        if (gameData.getSelectedCard() == null) {
            Printer.print("no card is selected yet");
            return false;
        }

        if(gameData.getSelectedCard().getCardFamily().equals(CardFamily.TRAP) ||
                gameData.getSelectedCard().getCardFamily().equals(CardFamily.SPELL)){
            Printer.print("activate effect is only for spell and trap cards");
            return false;
        }

        if(gameData.getCurrentPhase().equals(Phase.MAIN1) ||
                gameData.getCurrentPhase().equals(Phase.MAIN2)){
            Printer.print("you can’t activate an effect on this phase");
            return false;
        }

        SpellAndTraps card = (SpellAndTraps) gameData.getSelectedCard();

        if(card.wasActivated){
            Printer.print("you have already activated this card");
            return false;
        }

        return true;
    }


}
