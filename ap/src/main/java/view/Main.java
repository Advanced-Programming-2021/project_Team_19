package view;

import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.Utils;
import jdk.jshell.execution.Util;

import static view.Printer.Printer.print;


public class Main {

    /**
     * Start point of the game!
     * The main function makes the directories required for the game.
     * Then creates a start point for the game.
     * @param arg
     */

    public static void main(String[] arg) {

        new Game(GameData.getTestGameData()).DeprecatedRun(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();
//
//        WelcomeMenu.getInstance().run();
    }

}
