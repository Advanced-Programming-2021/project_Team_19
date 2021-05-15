package model.Card.Monsters;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import view.Printer.Printer;

public class GateGuardian extends Monster {

    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed) {
        if (isForSetting){
            Printer.print("you cannot set this monster");
            return -1;
        }
        return 3;
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        gameData.getCurrentGamer().setLastTurnHasSummoned(gameData.getTurn() - 1);
    }
}
