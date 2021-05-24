package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class TorrentialTribute extends Trap {

    public TorrentialTribute(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }


    @Override
    public ActivationData activate(GameData gameData) {

        destroyMonsters(gameData, gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCards());
        destroyMonsters(gameData, gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCards());
        handleCommonsForActivate(gameData);

        handleDestroy(gameData);

        return new TriggerActivationData
                (false,
                        "trap activated successfully\nAll monsters has destroyed",
                        this);

    }


    private void destroyMonsters(GameData gameData, ArrayList<Monster> monsters){

        for(Monster monster : monsters) {
            if(monster != null){
                monster.handleDestroy(gameData);
            }
        }
    }

    public boolean canActivateBecauseOfAnAction(Action action){


        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(action.getGameData(), this));
        checkers.add(new CardOwnerIsNotActionDoerChecker(action, this));
        checkers.add(new mirageDragonChecker(action.getGameData(), this));

        if(!Checker.multipleCheck(checkers)){
            return false;
        }

        if (!(action instanceof Summon)) {
            return false;
        }

        return true;
    }

}
