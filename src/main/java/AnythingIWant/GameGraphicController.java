package AnythingIWant;

import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import model.Gamer;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class GameGraphicController {

    public static ArrayList<GameGraphicController> gameGraphicControllers = new ArrayList<>();
    Game game;
    int rounds;

    public GameGraphicController(User user1, User user2, int rounds){
        this.rounds = rounds;
        Gamer gamer1 = new Gamer(user1);
        Gamer gamer2 =  new Gamer(user2);
        game = new Game(new GameData(gamer1, gamer2), rounds, this);
        gameGraphicControllers.add(this);
    }

    public String getDataForStartGame() {
        return game.gameData.getGameStarter().getUsername() + ":" +
                game.gameData.getInvitedGamer().getUsername();
    }
}
