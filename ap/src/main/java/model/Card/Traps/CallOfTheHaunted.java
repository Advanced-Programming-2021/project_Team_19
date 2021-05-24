package model.Card.Traps;

import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Enums.*;
import model.Enums.SpellsAndTraps.TrapTypes;

import java.util.ArrayList;


public class CallOfTheHaunted extends SpeedEffectTrap {

    Monster summonedMonster;

    public CallOfTheHaunted(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }

    public ActivationData activate(GameData gameData) {

        Card card = Utils.askUserToSelectCard(
                gameData.getCurrentGamer().getGameBoard().getGraveYard().getCardsInGraveYard(),
                "enter monster id to special summon it", CardFamily.MONSTER);

        if(card == null){
            return new ActivationData
                    (null, "activation canceled");
        }

        Monster selectedCard = (Monster) card;
        putMonsterToMonsterZone(gameData, selectedCard);
        selectedCard.setCallOfTheHauntedTrap(this);

        handleCommonsForActivate(gameData);

        return new ActivationData(this, "trap has activated successfully");
    }

    private void putMonsterToMonsterZone(GameData gameData, Monster card) {

        card.setCardMod(CardMod.OFFENSIVE_OCCUPIED);
        new SpecialSummon(gameData).run(card);

    }

    public boolean canActivate(GameData gameData) {

        ArrayList<Checker> checkers = new ArrayList<>();
        checkers.add(new TurnChecker(gameData, this));
        checkers.add(new mirageDragonChecker(gameData, this));

        if(!Checker.multipleCheck(checkers)){
           return false;
        }

        if (gameData.getCardController(this).getGameBoard().getMonsterCardZone().isZoneFull()) {
            return false;
        }

        for (Card card : gameData.getCardController(this).getGameBoard().getGraveYard().getCardsInGraveYard()){
            if (card instanceof Monster) {
                return true;
            }
        }

        return true;
    }

    public void handleDestroy(GameData gameData) {

        if (!gameData.getCardController(summonedMonster).getGameBoard().getGraveYard().
                getCardsInGraveYard().contains(summonedMonster)) {
            summonedMonster.handleDestroy(gameData);
        }

        super.handleDestroy(gameData);
    }

}
