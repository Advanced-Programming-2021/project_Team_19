package model.Card.Traps;

import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardControllerIsCurrentGamerChecker;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Phase;

import java.util.ArrayList;

public class TimeSeal extends Trap {
    @Override
    public ActivationData activate(GameData gameData)  {

        handleCommonsForActivate(gameData);

        handleDestroy(gameData);

        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));

        return new ActivationData(this, "trap activated successfully");
    }

    public boolean canActivate(GameData gameData) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(gameData, this));

        if(!Checker.multipleCheck(checkers)){
            return false;
        }

        return true;
    }


    public static boolean shouldEffectRun(EffectLabel label){

        if(((SpellAndTraps)label.card).getTurnActivated() + 1 == label.gameData.getTurn()){
            if(label.gameData.getCurrentPhase().equals(Phase.DRAW)){
                return true;
            }
        }

        return false;
    }

    public static TriggerActivationData runEffect(EffectLabel label){

        Game.goToNextPhase(label.gameData);

        label.gamer.removeLabel(label);

        return new TriggerActivationData
                (true, "you can't draw this turn", label.card);
    }



}
