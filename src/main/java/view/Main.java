package view;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;


public class Main {

    /**
     * Start point of the game!
     * The main function makes the directories required for the game.
     * Then creates a start point for the game.
     * @param arg
     */

    public static void main(String[] arg) {

        CSVDataBaseController.load();

        DataBaseController.makeResourceDirectory();

    }

}