package controller.DuelControllers.Actions;

import controller.ActivateCheckers.SelectedCardIsNotNullChecker;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Card.Monsters.EffectMonster;
import model.Card.SpellAndTraps;
import model.Card.Traps.SpeedEffectTrap;
import model.Card.Traps.Trigger;
import model.Data.ActivationData;
import model.Enums.CardFamily;
import model.Enums.GameEvent;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class Activation extends Action {

    protected Card activatedCard;

    public Activation(GameData gameData) {
        super(gameData, "Activate");
    }

    public Activation(GameData gameData, Card card){
        super(gameData, "Activate");
        setActivatedCard(card);
    }

    public Card getActivatedCard() {
        return activatedCard;
    }

    protected void setActivatedCard(Card card) {
        activatedCard = card;
    }

    public ActivationData activate() {

        ActivationData data;

        try {
            activatedCard.getName();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        gameData.addActionToCurrentActions(this);

        data = handleChain();

        gameData.removeActionFromCurrentActions(this);

        return data;
    }

    public ActivationData runActivation() {

        if (activatedCard.hasActivationEffectCanceledInChain) {
            return null;
        }

        if (activatedCard.getCardFamily().equals(CardFamily.MONSTER)) {
            return ((EffectMonster) activatedCard).activate(gameData);
        } else {
            return ((SpellAndTraps) activatedCard).activate(gameData);
        }

    }

    public ActivationData handleChain() {

//        if (canThisCardsChainOnActivatedCard
//                (gameData.getSecondGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards())) {
//            if (!handleChainForOtherPlayer()) {
//                if (canThisCardsChainOnActivatedCard
//                        (gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getAllCards())) {
//                    handleChainForTurnPlayer();
//                }
//            }
//        }

        return runActivation();
    }

    private boolean handleChainForTurnPlayer() {

        gameData.setEvent(GameEvent.ASK_FOR_CONFIRMATION_FOR_CHAIN);
        if (Utils.askForConfirmation("do want to Chain ?")) {
            selectCardForChain();
            return true;
        }
        gameData.setEvent(null);

        return false;
    }

    private boolean handleChainForOtherPlayer() {

        boolean doChain = false;

        Utils.changeTurn(gameData);

        if (Utils.askForConfirmation("do want to Chain")) {
            selectCardForChain();
            doChain = true;
        }

        Utils.changeTurn(gameData);
        return doChain;
    }

    public boolean canThisCardsChainOnActivatedCard(ArrayList<SpellAndTraps> cardsForChain) {

        for (SpellAndTraps card : cardsForChain) {

            if (canThisCardChainOnActivatedCard(card) && !cardIsAlreadyInChain(card)) {
                return true;
            }
        }

        return false;
    }

    public boolean cardIsAlreadyInChain(Card card) {
        for (Action action : gameData.getCurrentActions()) {
            if (action instanceof Activation && ((Activation) action).getActivatedCard().equals(card)) {
                return true;
            }
        }
        return false;
    }

    public boolean canThisCardChainOnActivatedCard(SpellAndTraps card) {

        if (card == null) {
            return false;
        }

        if (activatedCard.getEffectSpeed() > card.getEffectSpeed())
            return false;

        if (card instanceof SpeedEffectTrap) {
            if (card.canActivate(gameData)) {
                return true;
            }
        } else if (card instanceof Trigger) {
            for (Action action : gameData.getCurrentActions()) {
                if ((card).canActivateBecauseOfAnAction(action)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean selectCardForChain() {

        Printer.print("so please do that");

        String command;

        while (true) {

            gameData.setEvent(GameEvent.CHAIN);
            command = GetInput.getString();
            gameData.setEvent(null);

            if (checkInvalidMoves(command)) {

            } else if (command.equals("chain")) {
                if (chain())
                    return true;
            } else if (Utils.handleSelect(gameData, command)) {

            } else if (command.matches("help")) {
                help();
            } else if (command.equals("cancel")) {
                return false;
            } else {
                Printer.printInvalidCommand();
            }
        }

    }


    private boolean chain() {

        if (new SelectedCardIsNotNullChecker(gameData, gameData.getSelectedCard()).check() != null) {
            Printer.print("no card selected");
            return false;
        }

        Card selectedCard = gameData.getSelectedCard();

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(selectedCard)
                instanceof SpellAndTrapCardZone)) {

            Printer.print("you can't chain this card");
            return false;
        }

        if (!canThisCardChainOnActivatedCard((SpellAndTraps) selectedCard)) {
            Printer.print("you can't chain this card");
            return false;
        }

        Printer.print("card added to chain");
        Activation activate = new Activation(gameData);
        activate.setActivatedCard(selectedCard);
        activate.activate();

        return true;
    }

    private boolean checkInvalidMoves(String command) {

        for (String str : Utils.getCommandsExceptActivation()) {
            if (command.matches(str)) {
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
