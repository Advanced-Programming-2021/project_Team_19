package view;


import controller.DataBaseControllers.DataBaseController;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import view.Menu.LoginMenu;

public class Main {

    public static void main(String[] arg) {

        new Game().run(GameData.getTestGameData());

//        DataBaseController.makeResourceDirectory();
//
//        LoginMenu.getInstance().run();
    }

}
