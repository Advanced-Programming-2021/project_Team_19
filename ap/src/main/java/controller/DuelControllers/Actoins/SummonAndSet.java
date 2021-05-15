package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Board.MonsterCardZone;
import model.Card.Monster;
import model.Gamer;
import view.GetInput;
import view.Printer.Printer;

public class SummonAndSet extends Action{

    protected SummonAndSet(GameData gameData, String actionName) {
        super(gameData, actionName);
    }

    public boolean sacrificeMonstersForSummonOrSet(GameData gameData, int numberOfSacrifices){
        Gamer gamer = gameData.getCurrentGamer();
        MonsterCardZone monsterCardZone = gamer.getGameBoard().getMonsterCardZone();
        String enterId = "enter the Id of the monster you want to sacrifice:";
        String doesNotContainMonster = "there are no monsters on this address";
        if (numberOfSacrifices > 1){
            enterId = "enter the Ids of the monsters you want to sacrifice:";
            doesNotContainMonster = "there is no monster on one of these addresses";
        }
        if (numberOfSacrifices == 0) {
            return true;
        }
        if (numberOfSacrifices == -1) {
            return false;
        }

        if (monsterCardZone.getNumberOfCards() < numberOfSacrifices) {
            Printer.print("there are not enough cards for tribute");
            return false;
        } else {
            String command;
            while (true) {
                Printer.print(enterId);
                command = GetInput.getString();
                if (hasNIds(numberOfSacrifices, command)) {
                    String[] ids = command.split(" ");
                    if (containsRecurringId(ids)){
                        Printer.print("please enter " + numberOfSacrifices + " distinct ids");
                    }
                    else if (!allIdsContainMonsters(ids, monsterCardZone)) {
                        Printer.print(doesNotContainMonster);
                    } else {
                        sacrificeMonsters(ids, monsterCardZone, gamer, gameData);
                        return true;
                    }
                } else if (command.matches("cancel")) {
                    return false;
                } else {
                    Printer.printInvalidCommand();
                }
            }
        }
    }


    private boolean hasNIds(int numberOfSacrifices, String command){
        return  (command.matches("[1-5] ".repeat(Math.max(0, numberOfSacrifices)).trim()));
    }

    private boolean allIdsContainMonsters(String[] ids, MonsterCardZone monsterCardZone){
        for (String id : ids) {
            if (monsterCardZone.getCardById(Integer.parseInt(id)) == null)
                return false;
        }
        return true;
    }

    private void sacrificeMonsters(String[] ids, MonsterCardZone monsterCardZone, Gamer gamer, GameData gameData){
        for (String id : ids) {
            ((Monster) monsterCardZone.getCardById(Integer.parseInt(id))).sacrifice(gameData, gamer);
        }
    }

    private boolean containsRecurringId(String[] ids){
        for (int i = 0; i < ids.length; i++) {
            for (int j = i + 1; j < ids.length; j++) {
                if (ids[i].equals(ids[j]))
                    return true;
            }
        }
        return false;
    }
}
