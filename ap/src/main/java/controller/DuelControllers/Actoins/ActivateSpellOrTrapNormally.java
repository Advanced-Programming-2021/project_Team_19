package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Board.SpellAndTrapCardZone;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Enums.SpellCardMods;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateSpellOrTrapNormally extends Activation {


    public ActivateSpellOrTrapNormally(GameData gameData) {
        super(gameData);
        setActivatedCard(gameData.getSelectedCard());
    }

    public void run() {

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CurrentPhaseIsMainPhaseChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);
        if (checkersResult != null) {
            Printer.print(checkersResult);
            return;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if (!card.canActivate(gameData)) {
            Printer.print("you can't activate this card");
            return;
        }

        if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand) {
            activateFromHand(card);
        } else if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone) {
            activateSpellOrTrap();
        } else {
            Printer.print("invalid Zone");
        }

    }

    private void activateSpellOrTrap() {

        ActivationData data = super.activate();

        if (!data.message.equals("")) {
            Printer.print(data.message);
        }
    }


    private void activateFromHand(SpellAndTraps card) {

        if (card instanceof Trap) {
            Printer.print("you should set trap card first");
            return;
        }

        if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
            Printer.print("spell card zone is full");
            return;
        }
        if (!card.canActivate(gameData)) {
            Printer.print("preparations of this spell are not done yet");
            return;
        }

        activateOrSetCheckFieldSpell(card, gameData);

        card.setSpellCardMod(SpellCardMods.OFFENSIVE);

        activateSpellOrTrap();
    }

}
