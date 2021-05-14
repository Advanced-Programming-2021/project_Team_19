package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.SpellAndTraps;
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
            if(spellOrTrap.canActivateBecauseOfAnAction(this)){
                return true;
            }
        }

        return false;
    }

    protected void handleActivateTrapOrSpeedSpellOnOtherPlayerTurn(){

        changeTurn();
        gameData.showBoard();
        Printer.print("do you want to activate your trap and spell?");
        if(GetInput.getString().equals("yes")){
            new ActivateTrapOnOtherPlayerTurn(this).run();
        }
        changeTurn();
    }

    protected boolean handleActivateTrapOnGamerTurnBecauseOfAnAction(){

        Printer.print(actionName + " has occurred just now");
        Printer.print("do you want to activate your trap and spell?");

        if(GetInput.getString().equals("yes")){
            return (new ActivateTrapOnGamerTurn(this).run());
        }

        return false;
    }

    private void changeTurn(){

        gameData.changeTurn();
        Printer.print("now it will be " + gameData.getCurrentGamer().getUsername() +"’s turn");
    }

    protected void handleTrap(){

        gameData.addActionToCurrentActions(this);

        if(canTurnOwnerActivateTrapBecauseOfAnAction()){
            if(!handleActivateTrapOnGamerTurnBecauseOfAnAction()){
                if(canOtherPlayerActivateAnyTrapOrSpeedSpellBecauseOfAnAction())
                    handleActivateTrapOrSpeedSpellOnOtherPlayerTurn();
            }
        }

        gameData.removeActionFromCurrentActions(this);

    }

}
