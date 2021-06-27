package view;

import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import view.Menu.WelcomeMenu;


public class Main {

    /**
     * Start point of the game!
     * The main function makes the directories required for the game.
     * Then creates a start point for the game.
     * @param arg
     */

    public static void main(String[] arg) {

//        new Game(GameData.getTestGameData()).DeprecatedRun();

        DataBaseController.makeResourceDirectory();

        WelcomeMenu.getInstance().run();
    }

}
