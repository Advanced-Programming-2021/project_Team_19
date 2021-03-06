package controller.DuelControllers.Actions;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateSpeedEffect extends ActivateTrapWithNotification {

    public ActivateSpeedEffect(GameData gameData) {
        super(gameData);
    }

    public ActivationData handleActivate() {

        ActivationData data = new ActivationData(null, "");
        setActivatedCard(gameData.getSelectedCard());

        ArrayList<ActivationChecker> checkers = new ArrayList<>();

        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectSpellZoneChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if (checkersResult != null) {
            data.message = checkersResult;
            return data;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if (!card.canActivate(gameData)) {
            data.message = "you can't activate this card";
            return data;
        }

        return super.activate();

    }

    protected boolean checkInvalidMoves(String command) {

        for (String str : Utils.getCommandsExceptActivation()) {
            if (command.matches(str)) {
                Printer.print("please activate one trap or spell");
                return true;
            }
        }
        return false;
    }


}
