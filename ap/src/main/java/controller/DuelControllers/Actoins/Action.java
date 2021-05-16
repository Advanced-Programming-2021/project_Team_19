package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Gamer;
import view.GetInput;
import view.Printer.Printer;

public abstract class Action {

    protected GameData gameData;
    protected String actionName;
    protected Gamer actionDoer;

    protected Action(GameData gameData, String actionName){

        setGameData(gameData);
        setActionName(actionName);
        setActionDoer();
    }

    protected void setGameData(GameData gameData){
        this.gameData = gameData;
    }

    public GameData getGameData(){
        return gameData;
    }

    protected void setActionName(String actionName){
        this.actionName = actionName;
    }

    public String getActionName(){
        return actionName;
    }

    private void setActionDoer(){actionDoer = gameData.getCurrentGamer();}

    public Gamer getActionDoer(){return actionDoer;}

    protected boolean canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction(){

        for(SpellAndTraps spellOrTrap :
                gameData.getSecondGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if(spellOrTrap.canActivateBecauseOfAnAction(this)){
                return true;
            }
        }

        return false;
    }

    protected boolean canTurnOwnerActivateTrapBecauseOfAnAction(){

        for(SpellAndTraps spellOrTrap :
                gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards()) {
            if(spellOrTrap != null && spellOrTrap.canActivateBecauseOfAnAction(this)){
                return true;
            }
        }

        return false;
    }

    protected TriggerActivationData handleActivateTrapOrSpeedSpellOnOtherPlayerTurn(){

        TriggerActivationData data =  new TriggerActivationData
                (false, "", null);

        ActivationData tempData;

        changeTurn();

        gameData.showBoard();

        Printer.print("do you want to activate your trap and spell?");

        if(GetInput.getString().equals("yes")){

            tempData = new ActivateTriggerEffectOnOtherPlayerTurn(this).run();
            if(tempData.activatedCard == null){
                data = new TriggerActivationData
                        (false, tempData.message, null);
            }
        }

        changeTurn();

        return data;

    }

    protected TriggerActivationData handleActivateTrapOnGamerTurnBecauseOfAnAction(){

        Printer.print(actionName + " has occurred just now");
        Printer.print("do you want to activate your trap and spell?");

        if(GetInput.getString().equals("yes")){

            ActivationData tempData = new ActivateTriggerEffectOnGamerTurn(this).run();

            if(tempData.activatedCard == null){
                return new TriggerActivationData
                        (false, tempData.message, null);
            }

            return (TriggerActivationData) tempData;
        }

        return new TriggerActivationData(false, "", null);
    }

    private void changeTurn(){

        gameData.changeTurn();
        Printer.print("now it will be " + gameData.getCurrentGamer().getUsername() +"’s turn");
    }

    protected TriggerActivationData handleTriggerEffects(){

        TriggerActivationData data = new TriggerActivationData
                (false, "", null);

        gameData.addActionToCurrentActions(this);

        if(canTurnOwnerActivateTrapBecauseOfAnAction()){

            data = handleActivateTrapOnGamerTurnBecauseOfAnAction();

            if(data.activatedCard == null){

                if(canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction()){

                    data = handleActivateTrapOrSpeedSpellOnOtherPlayerTurn();

                }

            }
        }

        gameData.removeActionFromCurrentActions(this);

        return data;

    }

}
