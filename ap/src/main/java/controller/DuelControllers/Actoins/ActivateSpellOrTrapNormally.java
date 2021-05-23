package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Enums.CardFamily;
import model.Enums.SpellCardMods;
import model.Phase;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateSpellOrTrapNormally extends Activate {


    public ActivateSpellOrTrapNormally(GameData gameData){
        super(gameData);
        setActivatedCard(gameData.getSelectedCard());
    }

    public void run(){

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CurrentPhaseIsMainPhaseChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);
        if(checkersResult != null){
            Printer.print(checkersResult);
            return;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if(!card.canActivate(gameData)){
            Printer.print("you can't activate this card");
            return;
        }

        if(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand){
            if(!activateFromHand(card)) return;
        }

        else if(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone){
            if(!activateFromSpellZone(card)) return;
        }

        else{
            Printer.print("invalid Zone");
            return;
        }

    }

    private boolean activateFromSpellZone(SpellAndTraps card) {

        Printer.print(card.activate(gameData).message);
        return true;

    }

    private boolean activateFromHand(SpellAndTraps card){

        if(card instanceof Trap){
            Printer.print("you should set trap card first");
            return false;
        }

        if(gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()){
            Printer.print("spell card zone is full");
            return false;
        }
        if(!card.canActivate(gameData)){
            Printer.print("preparations of this spell are not done yet");
            return false;
        }

        gameData.moveCardFromOneZoneToAnother(card,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone());
        card.setSpellCardMod(SpellCardMods.OFFENSIVE);

        Printer.print(card.activate(gameData).message);

        return true;

    }

}
