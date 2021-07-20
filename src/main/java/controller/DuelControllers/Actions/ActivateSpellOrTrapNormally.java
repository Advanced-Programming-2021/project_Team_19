package controller.DuelControllers.Actions;

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
    int currentId;

    public ActivateSpellOrTrapNormally(GameData gameData) {
        super(gameData);
        setActivatedCard(gameData.getSelectedCard());
    }

    @Override
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

        if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand){
            if (card instanceof Trap) {
                return "you should set trap card first";
            }

            if (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().isZoneFull()) {
                return "spell card zone is full";
            }
            if (!card.canActivate(gameData)) {
                return "preparations of this spell are not done yet";
            }


        }

        return "activate spell normally";
    }

    public String[] run() {

        String result = actionIsValid();

        if(!result.equals("activate spell normally")){
            print(result);
        }

        SpellAndTraps card = (SpellAndTraps) activatedCard;

        if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof Hand) {
            currentId = gameData.getCurrentGamer().getGameBoard().getHand().getId(card);
             return new String[]{"activate spell " + activateFromHand(card), String.valueOf(currentId)};
        } else {
            currentId = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(card);
            return new String[]{"activate spell -1 " + activateSpellOrTrap(), String.valueOf(currentId)};
        }
    }

    private String activateSpellOrTrap() {

        ActivationData data = super.activate();

        if (!data.message.equals("")) {
            print(data.message);
        }

        return data.message;
    }


    private String activateFromHand(SpellAndTraps card) {

        activateOrSetCheckFieldSpell(card, gameData);

        card.setSpellCardMod(SpellCardMods.OFFENSIVE);

        return  gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(card) +
                " " + activateSpellOrTrap();
    }

}
