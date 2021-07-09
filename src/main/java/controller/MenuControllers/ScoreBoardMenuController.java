package controller.MenuControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.DataBaseControllers.UserDataBaseController;
import controller.Utils;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.User;
import model.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import static view.Printer.Printer.print;


public class ScoreBoardMenuController {

    static ScoreBoardMenuController instance = null;

    private ScoreBoardMenuController() {
    }

    public static ScoreBoardMenuController getInstance() {

        if (instance == null) {
            instance = new ScoreBoardMenuController();
        }
        return instance;
    }

    public DataForClientFromServer run(String command) {

        if (command.matches("scoreboard show")) {
            return showScores();
        }
        return Utils.getDataSendToClientForInvalidInput();
    }

    private final TreeSet<User> allUsers = new TreeSet<>(new UserComp());

    private DataForClientFromServer showScores() {

        ArrayList<Person> persons = new ArrayList<>();
        gatherAllUsers();
        int rank = 0;
        int currentScore = -10;
        for (User user : allUsers) {
            if (user.getScore() != currentScore) {
                currentScore = user.getScore();
                rank++;
            }
            persons.add(new Person(user, rank));
        }

        Gson gson = new GsonBuilder().create();
        String data = gson.toJson(persons);

        return new DataForClientFromServer(data, MessageType.SCORE);

    }

    private void gatherAllUsers() {
        allUsers.clear();
        allUsers.addAll(UserDataBaseController.allUsers());
    }

}


class UserComp implements Comparator<User> {

    public int compare(User firstUser, User secondUser) {
        int scoreComp = Integer.compare(firstUser.getScore(), secondUser.getScore());
        int nickNameComp = firstUser.getNickname().compareTo(secondUser.getNickname());
        if (scoreComp == 0) {
            return nickNameComp;
        } else {
            return -scoreComp;
        }
    }

}