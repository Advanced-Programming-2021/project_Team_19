package AnythingIWant;

import controller.Utils;
import model.Data.DataForClientFromServer;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

public class LobbyHandler {


    private ArrayList<User> waitersForOneRound;

    private ArrayList<User> waitersForThreeRounds;


    public DataForClientFromServer run(User user, String command) {
        if (command.matches("lobby --play --rounds \\d")) {
            return waitingForRound(user, Utils.getMatcher(command, "lobby --play --rounds (\\d)"));
        } else if (command.equals("lobby exit")) {
//            return cancelRequest(user);
        } else if (command.equals("lobby --getStatus")) {

        }
        return null;
    }

    private DataForClientFromServer waitingForRound(User user, Matcher matcher) {
        int round = Integer.parseInt(matcher.group(1));
        waitOrTake(user, round);
        return null;
    }

    private synchronized void waitOrTake(User user, int round) {
        if (round == 1 && waitersForOneRound.isEmpty()) {
            waitersForOneRound.add(user);
        } else if (round == 1) {

        } else if (waitersForThreeRounds.isEmpty()) {

        }
    }

}
