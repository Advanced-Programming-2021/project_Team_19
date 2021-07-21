package AnythingIWant;

import controller.Utils;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.Pair;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final ArrayList<Pair<User, User>> matchPairs = new ArrayList<>();


    public synchronized DataForClientFromServer run(User user, String command) {
        if (command.matches("lobby --play --rounds \\d")) {
            return waitingForRound(user, Utils.getMatcher(command, "lobby --play --rounds (\\d)"));
        } else if (command.equals("lobby exit")) {
//            return cancelRequest(user);
        } else if (command.equals("lobby --getStatus")) {
            return getStatus(user);
        }
        return null;
    }

    private DataForClientFromServer waitingForRound(User user, Matcher matcher) {
        int round = Integer.parseInt(matcher.group(1));
        if (round == 1) {
            if (waitersForOneRound.isEmpty()) {
                waitersForOneRound.add(user);
                return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
            } else {
                matchPairs.add(new Pair<>(user, waitersForOneRound.get(0)));
                waitersForOneRound.clear();
                return new DataForClientFromServer("match started", MessageType.SUCCESSFUL);
            }
        } else {
            if (waitersForThreeRounds.isEmpty()) {
                waitersForThreeRounds.add(user);
                return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
            } else {
                matchPairs.add(new Pair<>(user, waitersForThreeRounds.get(0)));
                waitersForThreeRounds.clear();
                return new DataForClientFromServer("match started", MessageType.SUCCESSFUL);
            }
        }
    }

    private DataForClientFromServer getStatus(User user) {
        for (Pair<User, User> matched : matchPairs) {
            if (matched.getSecond().getUsername().equals(user.getUsername())) {
                return new DataForClientFromServer("match started", MessageType.SUCCESSFUL);
            }
        }
        return new DataForClientFromServer("wait", MessageType.SUCCESSFUL);
    }

}
