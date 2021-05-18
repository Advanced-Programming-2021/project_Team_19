package model.Card.Monsters;
import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Phase;
import view.Printer.Printer;

public class YomiShip extends Monster {



    @Override
    public void handleDestroy(GameData gameData) {
        super.handleDestroy(gameData);
        if (gameData.getCurrentPhase().equals(Phase.BATTLE) && !gameData.getSelectedCard().equals(this)) {
            gameData.getSelectedCard().handleDestroy(gameData);
            Printer.print("you destroyed Yomi ship and your card was destroyed");
        }
    }
}
