package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Card.Monsters.ShouldAskForActivateEffectMonster;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateEffectMonster extends Activation {

    public ActivateEffectMonster(GameData gameData) {
        super(gameData);
        setActivatedCard(gameData.getSelectedCard());
    }

    public void run() {

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectMonsterZoneChecker(gameData, activatedCard));
        checkers.add(new SelectedCardIsOneEffectMonsterForActivateEffectChecker(gameData, activatedCard));
        checkers.add(new CurrentPhaseIsMainPhaseChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if (checkersResult != null) {
            Printer.print(checkersResult);
            return;
        }

        ShouldAskForActivateEffectMonster card = (ShouldAskForActivateEffectMonster) activatedCard;

        if (!card.canActivate(gameData)) {
            Printer.print("you can't activate this card");
            return;
        }

        super.activate();
    }
}
