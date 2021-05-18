package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Phase;
import view.Printer.Printer;

public class NegateAttack extends Trap {

    public ActivationData activate(GameData gameData) {

        handleDestroy(gameData);

        turnActivated = gameData.getTurn();

        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));

        return new TriggerActivationData
                (true, "spell activated successfully", this);

    }


    public boolean canActivateBecauseOfAnAction(Action action) {

        if (!canActivateThisTurn(action.getGameData())) {
            return false;
        }

        if (Utils.isCareOwnerActionDoer(action.getGameData(), action, this)) {
            return false;
        }

        if (!(action instanceof Attack)) {
            return false;
        }

        return true;
    }

    public boolean shouldEffectRun(EffectLabel label){

        if(label.gameData.getCurrentPhase().equals(Phase.BATTLE)){
            if(label.gameData.getCurrentActions().size() == 0){
                return true;
            }
        }
        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label){

        label.gamer.removeLabel(label);

        label.label = 1;

        return new TriggerActivationData(true,"Spell Activated Successfully",
                label.card);
    }
}
