package controller.DuelControllers;

import controller.DataFromGameRun;
import controller.DuelControllers.Actions.Action;
import controller.Utils;
import model.Board.GraveYard;
import model.Board.Zones;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Enums.GameEvent;
import model.Gamer;
import model.Phase;
import model.TriggerLabel;
import model.User;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Collections;

public class GameData {

    private int gameStarterId = 0;
    private final ArrayList<Gamer> gamers = new ArrayList<>();
    private final ArrayList<Action> currentActions = new ArrayList<>();
    private Card selectedCard;
    private int turn = 1;
    private Phase currentPhase = Phase.DRAW;
    private Gamer turnOwner;
    public boolean hasAskedForSpellsThisPhase = true;
    private GameEvent event = null;
    private int actionIndexForTriggerActivation = -1;
    public TriggerLabel triggerLabel = null;
    public ArrayList<DataFromGameRun> dataFromGameRuns = new ArrayList<>();

    public GameData(Gamer firstGamer, Gamer secondGamer) {
        gamers.add(firstGamer);
        gamers.add(secondGamer);
        turnOwner = firstGamer;
    }

    public boolean isGameOver() {
        return (gamers.get(0).getLifePoint() == 0 || gamers.get(1).getLifePoint() == 0);

    }

    public int getActionIndexForTriggerActivation() {
        return actionIndexForTriggerActivation;
    }

    public void setActionIndexForTriggerActivation(int index) {
        actionIndexForTriggerActivation = index;
    }

    public GameEvent getEvent() {
        return event;
    }

    public void setEvent(GameEvent event) {
        this.event = event;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public Gamer getCurrentGamer() {
        return gamers.get(0);
    }

    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Gamer getSecondGamer() {
        return gamers.get(1);
    }

    public Gamer getOtherGamer(Gamer gamer) {
        if (!gamers.contains(gamer)) {
            return null;
        }

        return gamers.get(1 - gamers.indexOf(gamer));

    }

    public int getTurn() {
        return turn;
    }

    public void turnFinished() {
        changeTurn();
        Printer.print(getCurrentGamer().getUsername() + "'s turn");
        turn++;
    }

    public void changeTurn() {
        Collections.swap(gamers, 0, 1);
        gameStarterId = 1 - gameStarterId;
    }

    public void surrender() {
        changeTurn();
        getCurrentGamer().decreaseLifePoint(getCurrentGamer().getLifePoint());
        getSecondGamer().decreaseLifePoint(getSecondGamer().getLifePoint() + 1);
    }

    public void addActionToCurrentActions(Action action) {
        currentActions.add(action);
    }

    public boolean isRitualSummoning() {
        for (Action action : currentActions) {
            if (action.getActionName().equals("Ritual Summon")) {
                return true;
            }
        }
        return false;
    }

    public void removeRitualSummoning() {
        removeActionFromCurrentActions(getRitualSummoning());
    }

    public Action getRitualSummoning() {
        for (Action action : currentActions) {
            if (action.getActionName().equals("Ritual Summon")) {
                return action;
            }
        }
        return null;
    }

    public void removeActionFromCurrentActions(Action action) {
        currentActions.remove(action);
    }


    public Card moveCardFromOneZoneToAnother(Card card, Zones sourceZone, Zones destinationZone) {
        if (sourceZone instanceof GraveYard) {
            sourceZone.removeCard(sourceZone.getId(card));
            Card newCard = getCardToMoveFromGraveYardToAnotherZone(card);
            destinationZone.addCard(newCard);
            return newCard;
        } else {
            destinationZone.addCard(sourceZone.removeCard(sourceZone.getId(card)));
            return null;
        }
    }

    private Card getCardToMoveFromGraveYardToAnotherZone(Card card) {
        Card newCard = Utils.getCardByName(card.getName());

        if (card instanceof SpellAndTraps) {
            ((SpellAndTraps) newCard).setSpellCardMod(((SpellAndTraps) card).getSpellCardMod());
        }
        if (card instanceof Monster) {
            ((Monster) newCard).setCardMod(((Monster) card).getCardMod());
        }
        return newCard;
    }

    public int getRole(Card card) {

        if (gamers.get(0).getGameBoard().getZone(card) != null) {
            return 0;
        } else if (gamers.get(1).getGameBoard().getZone(card) != null) {
            return 1;
        } else {
            return -1;
        }
    }

    ArrayList<Phase> phases = new ArrayList<>();

    {
        phases.add(Phase.DRAW);
        phases.add(Phase.STANDBY);
        phases.add(Phase.MAIN1);
        phases.add(Phase.BATTLE);
        phases.add(Phase.MAIN2);
        phases.add(Phase.END);
    }

    public void goToNextPhase() {

        int nextPhaseIndex = (phases.indexOf(currentPhase) + 1) % 6;
        currentPhase = phases.get(nextPhaseIndex);
        setSelectedCard(null);

        if (currentPhase.equals(Phase.DRAW)) {
            turnOwner = getCurrentGamer();
        }
    }

    public void goToEndPhase() {
        currentPhase = Phase.END;
    }

    public void showBoard() {

        Printer.print(getSecondGamer().getBoardForRival());
        Printer.print("--------------------------");
        Printer.print(getCurrentGamer().getBoardForSelf());

    }

    public void finishGame() {
        for (Gamer gamer : gamers) {
            gamer.gameFinished();
        }
    }

    public Gamer getTurnOwner() {
        return turnOwner;
    }

    public Gamer getNextTurnOwner() {
        int index = gamers.indexOf(getTurnOwner());
        return gamers.get(1 - index);
    }

    public Gamer getGameStarter() {
        return gamers.get(gameStarterId);
    }

    public Gamer getInvitedGamer() {
        return gamers.get(1 - gameStarterId);
    }

    public ArrayList<Action> getCurrentActions() {
        return currentActions;
    }

    //test

    public static GameData getTestGameData() {

        User user1 = new User("mohammad", "mohammad", "123");

        User user2 = new User("taha", "taha", "123");

        user1.setActiveDeckName("deck");
        user2.setActiveDeckName("deck");

        GameData gameData = new GameData(Gamer.getTestGamer(user1), Gamer.getTestGamer(user2));

        gameData.turn = 5;
        return gameData;
    }

    public Gamer getCardController(Card card) {

        if (getCurrentGamer().getGameBoard().getZone(card) != null)
            return getCurrentGamer();
        else
            return getSecondGamer();

    }

    public void resetDataFromGameRuns(){
        dataFromGameRuns.clear();
    }

    public void addData(DataFromGameRun data){
        dataFromGameRuns.add(data);
    }
}
