package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.*;
import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

import java.util.ArrayList;

public class ActivateSpeedEffect extends ActivateTrapWithNotification {

    public ActivateSpeedEffect(GameData gameData){
        super(gameData);
    }

    public ActivationData handleActivate() {

        ActivationData data = new ActivationData(null, "");

        activatedCard = gameData.getSelectedCard();

        ArrayList<ActivationChecker> checkers = new ArrayList<>();

        checkers.add(new SelectedCardIsNotNullChecker(gameData, activatedCard));
        checkers.add(new CardIsNotOneMonsterChecker(gameData, activatedCard));
        checkers.add(new CardIsInCorrectZoneChecker(gameData, activatedCard));
        checkers.add(new CardHasNotBeenActivatedYetChecker(gameData, activatedCard));

        String checkersResult = ActivationChecker.multipleCheck(checkers);

        if(checkersResult != null){
            data.message = checkersResult;
            return data;
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if(!card.canActivate(gameData)){
            data.message = "you can't activate this card";
            return data;
        }

        return card.activate(gameData);

    }

    protected boolean checkInvalidMoves(String command){

        for(String str : getInvalidMoves()){
            if(command.matches(str)){
                Printer.print("please activate one trap or spell");
                return true;
            }
        }
        return false;
    }


}
