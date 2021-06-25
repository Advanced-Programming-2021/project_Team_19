package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Card.Monsters.ShouldAskForActivateEffectMonster;

import java.util.ArrayList;

import static view.Printer.Printer.print;

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

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if (checkersResult != null) {
            print(checkersResult);
            return;
        }

        ShouldAskForActivateEffectMonster card = (ShouldAskForActivateEffectMonster) activatedCard;

        if (!card.canActivate(gameData)) {
            print("you can't activate this card");
            return;
        }
        print(super.activate().message);

    }
}
