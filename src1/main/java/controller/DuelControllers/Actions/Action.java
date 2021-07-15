package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Spell;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.CardFamily;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Gamer;
import view.Printer.Printer;

import java.util.ArrayList;

public abstract class Action {

    protected GameData gameData;
    protected String actionName;
    protected Gamer actionDoer;

    protected Action(GameData gameData, String actionName) {

        setGameData(gameData);
        setActionName(actionName);
        setActionDoer();
    }

    protected void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }

    protected void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

    private void setActionDoer() {
        actionDoer = gameData.getCurrentGamer();
    }

    public Gamer getActionDoer() {
        return actionDoer;
    }

    public boolean canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction() {

        for (SpellAndTraps spellOrTrap :
                gameData.getSecondGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {

            if (spellOrTrap == null) {
                continue;
            }
            if (spellOrTrap.canActivateBecauseOfAnAction(this)) {
                return true;
            }
        }

        return false;
    }

    public boolean canTurnOwnerActivateTrapBecauseOfAnAction() {

        for (SpellAndTraps spellOrTrap :
                gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if (spellOrTrap != null && spellOrTrap.canActivateBecauseOfAnAction(this)) {
                return true;
            }
        }

        return false;
    }

    public TriggerActivationData handleActivateTrapOrSpeedSpellOnOtherPlayerTurn() {

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        ActivationData tempData;

        Utils.changeTurn(gameData);
        gameData.showBoard();

        if (Utils.askForActivate(actionName + " has occurred just now")) {

            tempData = new ActivateTriggerEffectOnOtherPlayerTurn(this).run();

            if (tempData.activatedCard == null) {
                data = new TriggerActivationData
                        (false, tempData.message, null);
            } else {
                data = (TriggerActivationData) tempData;
            }

        }

        Utils.changeTurn(gameData);

        return data;

    }


    public TriggerActivationData handleActivateTrapOnGamerTurnBecauseOfAnAction() {


        if (Utils.askForActivate(actionName + " has occurred just now")) {

            ActivationData tempData = new ActivateTriggerEffectOnGamerTurn(this).run();

            if (tempData.activatedCard == null) {

                return new TriggerActivationData
                        (false, tempData.message, null);
            }

            return (TriggerActivationData) tempData;
        }

        return new TriggerActivationData(false, "", null);
    }


    protected TriggerActivationData handleTriggerEffects() {

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        gameData.addActionToCurrentActions(this);
        gameData.setActionIndexForTriggerActivation(gameData.getCurrentActions().indexOf(this));

        boolean hasTurnOwnerActivatedEffect = false;

        if (canTurnOwnerActivateTrapBecauseOfAnAction()) {

            data = handleActivateTrapOnGamerTurnBecauseOfAnAction();

            if (data.activatedCard != null) {

                hasTurnOwnerActivatedEffect = true;
            }
        }

        if (!hasTurnOwnerActivatedEffect) {

            if (canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction()) {

                data = handleActivateTrapOrSpeedSpellOnOtherPlayerTurn();
            }
        }

        gameData.removeActionFromCurrentActions(this);
        gameData.setActionIndexForTriggerActivation(-1);

        return data;

    }

    protected boolean canActionBeDone() {

        boolean canActionBeDone = true;

        gameData.addActionToCurrentActions(this);

        ArrayList<EffectLabel> tempArray = (ArrayList<EffectLabel>)
                gameData.getSecondGamer().getEffectLabels().clone();

        for (EffectLabel label : tempArray) {
            if (label.checkLabel()) {
                TriggerActivationData data = label.runEffect();
                if (!data.message.equals("")) {
                    Printer.print(data.message);
                }
                if (data.hasActionStopped) {
                    canActionBeDone = false;
                }
            }
        }

        gameData.removeActionFromCurrentActions(this);

        return canActionBeDone;
    }

    public void activateOrSetCheckFieldSpell(Card card, GameData gameData) {
        if (!card.getCardFamily().equals(CardFamily.SPELL) || !((Spell) card).getSpellType().equals(SpellTypes.FIELD)) {
            gameData.moveCardFromOneZoneToAnother(card,
                    gameData.getCurrentGamer().getGameBoard().getHand(),
                    gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone());
        } else {
            gameData.moveCardFromOneZoneToAnother(card,
                    gameData.getCurrentGamer().getGameBoard().getHand(),
                    gameData.getCurrentGamer().getGameBoard().getFieldZone());
        }
    }

    public String actionIsValid(){
        return "";
    }
}
