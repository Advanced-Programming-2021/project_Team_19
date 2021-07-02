package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Phase;

import java.util.regex.Matcher;

import static view.Printer.Printer.print;

public class SetPosition extends Action {


    public SetPosition(GameData gameData) {
        super(gameData, "set position");
    }


    public String actionIsValid(boolean toDefensiveMode){

        Card card =  gameData.getSelectedCard();

        if (card == null) {
            return "no card is selected yet";
        }

        if(!(card.getCardFamily().equals(CardFamily.MONSTER))){
            return "you can’t change this card position";
        }

        Monster monster = (Monster) card;

        if (!gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().containsCard(monster)) {
            return "you can’t change this card position";
        }

        if (!gameData.getCurrentPhase().equals(Phase.MAIN1) && !gameData.getCurrentPhase().equals(Phase.MAIN2)) {
            return "action not allowed in this phase";
        }

        if (toDefensiveMode) {
            if (!monster.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
                return "invalid change position";
            }
        } else {
            if (!monster.getCardMod().equals(CardMod.DEFENSIVE_OCCUPIED)) {
                return "you can’t change this card position";
            }
        }

        if (monster.getLastTurnHasChangedPosition() == gameData.getTurn()) {
            return "you already changed this card position in this turn";
        }

        String mode = toDefensiveMode ? "defense" : "attack";
        return "set position " + mode;
    }

    public void run(Matcher matcher) {
        setPosition(matcher);
    }

    private void setPosition(Matcher matcher) {

        Card selectedCard =  gameData.getSelectedCard();
        String newModeStr = Utils.getFirstGroupInMatcher(matcher);
        boolean toDefensiveMode = newModeStr.equals("defense");
        String result = actionIsValid(toDefensiveMode);

        if(!result.startsWith("set position")){
            print(result);
            return;
        }

        CardMod newCardMode = toDefensiveMode ? CardMod.DEFENSIVE_OCCUPIED : CardMod.OFFENSIVE_OCCUPIED;

        ((Monster)selectedCard).handleChangePosition(gameData, newCardMode);
        print("monster card position changed successfully");
    }

}
