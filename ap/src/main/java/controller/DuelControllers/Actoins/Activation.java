package controller.DuelControllers.Actoins;

import controller.ActivateCheckers.SelectedCardIsNotNullChecker;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.Monsters.ShouldAskForActivateEffectMonster;
import model.Card.SpellAndTraps;
import model.Card.Traps.SpeedEffectTrap;
import model.Card.Traps.Trigger;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class Activation extends Action {

    protected Card activatedCard;

    public Activation(GameData gameData) {
        super(gameData, "Activate");
    }

    public Card getActivatedCard() {
        return activatedCard;
    }

    protected void setActivatedCard(Card card) {
        activatedCard = card;
    }

    public ActivationData activate() {

        ActivationData data = null;

        try{
            activatedCard.getName();
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }

        gameData.addActionToCurrentActions(this);

        if(handleChain() != null){
            data =  handleChain();
        }else{
            data = runActivation();
        }

        gameData.removeActionFromCurrentActions(this);

        return data;
    }

    public ActivationData runActivation(){

        if(activatedCard.hasActivationEffectCanceledInChain){
            return null;
        }

        if(activatedCard.getCardFamily().equals(CardFamily.MONSTER)){
            return ((ShouldAskForActivateEffectMonster)activatedCard).activate(gameData);
        }
        else{
            return ((SpellAndTraps)activatedCard).activate(gameData);
        }

    }

    public ActivationData handleChain(){

        if(canThisCardsChainOnActivatedCard
                (gameData.getSecondGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards())){

            if(handleChainForOtherPlayer()){
                selectCardForChain();
                return null;
            }
        }

        if(canThisCardsChainOnActivatedCard
                (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards())){

            if(handleChainForTurnPlayer()){
                selectCardForChain();
                return null;
            }
        }

        return ((SpellAndTraps)activatedCard).activate(gameData);
    }

    private boolean handleChainForTurnPlayer() {

        if(Utils.askForConfirmation("do want to Chain ?")){
            selectCardForChain();
            return true;
        }

        return false;
    }

    private boolean handleChainForOtherPlayer() {

        boolean doChain = false;

        Utils.changeTurn(gameData);

        if(Utils.askForConfirmation("do want to Chain")){
            selectCardForChain();
            doChain = true;
        }

        Utils.changeTurn(gameData);
        return doChain;
    }

    public boolean canThisCardsChainOnActivatedCard(ArrayList<SpellAndTraps>cardsForChain){

        for(SpellAndTraps card : cardsForChain){

            if(canThisCardChainOnActivatedCard(card)){
                return true;
            }
        }

        return false;
    }

    public boolean canThisCardChainOnActivatedCard(SpellAndTraps card){

        if(card == null){
            return false;
        }

        if(activatedCard.getEffectSpeed() > card.getEffectSpeed())
            return false;

        if(card instanceof SpeedEffectTrap){
            if(card.canActivate(gameData)){
                return true;
            }
        } else if (card instanceof Trigger){
            for(Action action : gameData.getCurrentActions()){
                if(((Trigger)card).canActivateBecauseOfAnAction(action)){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean selectCardForChain(){
        Printer.print("so please do that");

        String command;

        while(true){
            command = GetInput.getString();

            if (checkInvalidMoves(command)) {

            }
            else if (command.equals("chain")){
                if(chain())
                    return true;
            }
            else if(Utils.handleSelect(gameData, command)){

            }
            else if (command.matches("help")) {
                help();
            } else if (command.equals("cancel")) {
                return false;
            } else {
                Printer.printInvalidCommand();
            }
        }

    }


    private boolean chain(){

        if(new SelectedCardIsNotNullChecker(gameData, gameData.getSelectedCard()).check() != null){
            Printer.print("no card selected");
            return false;
        }

        Card selectedCard = gameData.getSelectedCard();

        if(!(gameData.getCurrentGamer().getGameBoard().getZone(selectedCard)
                instanceof SpellAndTrapCardZone)){

            Printer.print("you can't chain this card");
            return false;
        }

        if(!canThisCardChainOnActivatedCard((SpellAndTraps) selectedCard)){
            Printer.print("you can't chain this card");
            return false;
        }

        Printer.print("card added to chain");
        Activation activate = new Activation(gameData);
        activate.setActivatedCard(selectedCard);
        activate.activate();

        return true;
    }

    private boolean checkInvalidMoves(String command){

        for(String str : Utils.getCommandsExceptActivation()){
            if(command.matches(str)){
                Printer.print("please chain");
                return true;
            }
        }
        return false;
    }

    private void help() {

        Printer.print("chain");

        Select.help();

        System.out.println("""
                card show --selected
                help
                show board
                cancel""");

    }

}
