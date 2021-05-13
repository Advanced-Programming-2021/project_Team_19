package model.Card.Monsters;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import view.Printer.Printer;

public class YomiShip extends Monster {

    @Override
    public void handleDestroy(GameData gameData) {
        super.handleDestroy(gameData);
        gameData.getSelectedCard().handleDestroy(gameData);
    }
}
