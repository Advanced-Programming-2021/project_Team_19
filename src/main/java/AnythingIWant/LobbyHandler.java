package AnythingIWant;

import controller.Utils;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.Pair;
import model.User;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class LobbyHandler {

    private static LobbyHandler lobbyHandler;

    public static LobbyHandler getInstance() {
        if (lobbyHandler == null) {
            lobbyHandler = new LobbyHandler();
        }
        return lobbyHandler;
    }



    private final ArrayList<User> waitersForOneRound = new ArrayList<>();

    private final ArrayList<User> waitersForThreeRounds = new ArrayList<>();

    private final ArrayList<Pair<Pair<User, User>, String>> matchPairs = new ArrayList<>();

    private final ArrayList<Pair<User, User>> readyPairs = new ArrayList<>();


    public synchronized DataForClientFromServer run(User user, String command) {
        if (command.matches("lobby --play --rounds \\d")) {
            return waitingForRound(user, Utils.getMatcher(command, "lobby --play --rounds (\\d)"));
        } else if (command.equals("lobby exit")) {
            return cancelRequest(user);
        } else if (command.equals("lobby --getStatus")) {
            return getStatus(user);
        }
        return null;
    }

    private DataForClientFromServer waitingForRound(User user, Matcher matcher) {
        matcher.matches();
        int round = Integer.parseInt(matcher.group(1));
        if (round == 1) {
            if (waitersForOneRound.isEmpty()) {
                waitersForOneRound.add(user);
                return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
            } else {
                User second = waitersForOneRound.get(0);
                String message =  new GameGraphicController(user, second, 1).getDataForStartGame();
                matchPairs.add(new Pair<>(new Pair<>(user, second), message));
                waitersForOneRound.clear();
                return new DataForClientFromServer(message, MessageType.SUCCESSFUL);
            }
        } else {
            if (waitersForThreeRounds.isEmpty()) {
                waitersForThreeRounds.add(user);
                return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
            } else {
                User second = waitersForThreeRounds.get(0);
//                matchPairs.add(new Pair<>(user, second));
                waitersForThreeRounds.clear();
                return new DataForClientFromServer("match started", MessageType.SUCCESSFUL);
            }
        }
    }

    private DataForClientFromServer getStatus(User user) {
        for (Pair<Pair<User, User>, String> matched :
                (ArrayList<Pair<Pair<User, User>, String>>)matchPairs.clone()) {
            if (matched.getFirst().getSecond().getUsername().equals(user.getUsername())) {
                matchPairs.remove(matched);
                return new DataForClientFromServer(matched.getSecond(), MessageType.SUCCESSFUL);
            }
        }
        return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
    }

    private DataForClientFromServer cancelRequest(User user) {
        waitersForOneRound.removeIf(user1 -> user1.getUsername().equals(user.getUsername()));
        waitersForThreeRounds.removeIf(user1 -> user1.getUsername().equals(user.getUsername()));
        return new DataForClientFromServer("ok", MessageType.SUCCESSFUL);
    }

}
