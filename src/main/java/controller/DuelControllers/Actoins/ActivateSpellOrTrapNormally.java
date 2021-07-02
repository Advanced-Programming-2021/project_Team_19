package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.SpellCardMods;

import java.util.ArrayList;

import static view.Printer.Printer.print;

public class ActivateSpellOrTrapNormally extends Activation {


    public ActivateSpellOrTrapNormally(GameData gameData) {
        super(gameData);
        setActivatedCard(gameData.getSelectedCard());
    }

    public String actionIsValid(){

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CurrentPhaseIsMainPhaseChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);
        if (checkersResult != null) {
            return checkersResult;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if (!card.canActivate(gameData)) {
            return "you can't activate this card";
        }

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand) &&
                !(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)) {
            return "invalid zone";
        }

        return "activate normally";
    }

    public void run() {

        String error = actionIsValid();

        if(!error.equals("activate normally")){
            print(error);
            return;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand) {
            activateFromHand(card);
        } else if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone) {
            activateSpellOrTrap();
        }
    }

    private void activateSpellOrTrap() {

        ActivationData data = super.activate();

        if (!data.message.equals("")) {
            print(data.message);
        }
    }


    private void activateFromHand(SpellAndTraps card) {

        if (card instanceof Trap) {
            print("you should set trap card first");
            return;
        }

        if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
            print("spell card zone is full");
            return;
        }
        if (!card.canActivate(gameData)) {
            print("preparations of this spell are not done yet");
            return;
        }

        activateOrSetCheckFieldSpell(card, gameData);

        card.setSpellCardMod(SpellCardMods.OFFENSIVE);

        activateSpellOrTrap();
    }

}
