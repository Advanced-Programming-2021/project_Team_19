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

    @Override
    public String actionIsValid(){

        ArrayList<ActivationChecker> checkers = new ArrayList<>();
        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectMonsterZoneChecker(gameData, activatedCard));
        checkers.add(new SelectedCardIsOneEffectMonsterForActivateEffectChecker(gameData, activatedCard));
        checkers.add(new CurrentPhaseIsMainPhaseChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if (checkersResult != null) {
            return checkersResult;
        }

        ShouldAskForActivateEffectMonster card = (ShouldAskForActivateEffectMonster) activatedCard;

        if (!card.canActivate(gameData)) {
            return "you can't activate this card";
        }

        return "activate effect monster";
    }

    public void run() {

        String error = actionIsValid();

        if(error.equals("activate effect monster")){
            super.activate();
        } else {
            print(error);
        }


    }
}
