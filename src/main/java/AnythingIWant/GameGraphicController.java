package AnythingIWant;

import controller.DataForGameRun;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import model.Data.DataForClientFromServer;
import model.Gamer;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameGraphicController {

    public static HashMap<String, GameGraphicController> gameGraphicControllers = new HashMap<>();
    Game game;
    int rounds;
    String gameCode;

    public GameGraphicController(User user1, User user2, int rounds){
        this.rounds = rounds;
        Gamer gamer1 = new Gamer(user1);
        Gamer gamer2 =  new Gamer(user2);
        game = new Game(new GameData(gamer1, gamer2), rounds, this);
        gameCode = UUID.randomUUID().toString();
        gameGraphicControllers.put(gameCode, this);
    }

    public String getDataForStartGame() {
        return "match started &" + gameCode + "&" + game.gameData.getGameStarter().getUsername() + "&" +
                game.gameData.getInvitedGamer().getUsername();
    }

    public static DataForClientFromServer run(User user, DataForGameRun data){
        String command = data.getMessage();
        GameGraphicController controller = gameGraphicControllers.get(command.split("&")[0]);
        return controller.game.run(command.split("&")[1], user, data);
    }
}
