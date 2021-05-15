package model.Card.Traps;

import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import model.Board.GraveYard;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import view.GetInput;
import view.Printer.Printer;

public class CallOfTheHaunted extends Trap {

    Monster summonedMonster;

    public ActivationData activate(GameData gameData) {

        gameData.getCurrentGamer().getGameBoard().getGraveYard().printGraveYard();

        Printer.print("enter monster id to special summon it");
        String input;

        while (true) {

            input = GetInput.getString();
            int id = 0;

            GraveYard graveYard = gameData.getCurrentGamer().getGameBoard().
                    getGraveYard();

            if (input.equals("cancel")) {
                return new ActivationData(true);
            }

            if (!input.matches("\\d+")) {
                Printer.print("invalid input");
                continue;
            } else {
                id = Integer.parseInt(input);
            }

            if (!(getSelectedCard(graveYard, id) instanceof Monster)) {
                Printer.print("enter monster id please");
                continue;
            }

            if (graveYard.getCardsInGraveYard().size() < id || id < 1) {
                Printer.print("please enter valid number");
                continue;
            }

            Monster selectedCard = (Monster) getSelectedCard(graveYard,id);
            putMonsterToMonsterZone(gameData, selectedCard);
            selectedCard.setCallOfTheHauntedTrap(this);
            wasActivated = true;
            return new ActivationData(true);
        }
    }

    private void putMonsterToMonsterZone(GameData gameData, Card card){

        new SpecialSummon(gameData).run(card);
    }

    private Card getSelectedCard(GraveYard graveYard, int id) {
        return graveYard.getCard(id);
    }

    public boolean canActivate(GameData gameData) {

        if (!canActivateThisTurn(gameData)) {
            return false;
        }

        if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            return false;
        }

        for (Card card : gameData.getCurrentGamer().getGameBoard().getGraveYard().getCardsInGraveYard()) {
            if (card instanceof Monster) {
                return true;
            }
        }

        return true;
    }

    public void handleDestroy(GameData gameData){

        if(!gameData.getCardController(summonedMonster).getGameBoard().getGraveYard().
                getCardsInGraveYard().contains(summonedMonster)){
            summonedMonster.handleDestroy(gameData);
        }

        super.handleDestroy(gameData);
    }

}
