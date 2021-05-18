package view.Menu;

import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Enums.MessageType;
import view.GetInput;
import view.Printer.Printer;
import view.Printer.RegisterProfilePrinter;
import view.Utils;

import java.util.regex.Matcher;

public class LoginMenu extends Menu {

    static LoginMenu instance = null;

    public static LoginMenu getInstance() {

        if (instance == null) {
            instance = new LoginMenu();
        }
        return instance;
    }

    private LoginMenu() {
        super("Login Menu");
    }

    public void run() {

        while (true) {

            String command;
            command = GetInput.getString();
            if (command.matches("user create" +
                    "(:?(:? --username \\S+)|(:? --nickname \\S+)|(:? --password \\S+)){3}")) {
                manageCreatingAccount(false, command);
            } else if(command.matches("user create" +
                    "(?=.*?-u \\S+)(?=.*?-n \\S+)(?=.*?-p \\S+)(:? -(:?p|u|n) \\S+){3}")){
                manageCreatingAccount(true, command);
            }
            else if (command.matches("user login" +
                    "(:?(:? --username \\S+)|(:? --password \\S+)){2}")) {
                manageLogin(false, command);
            }else if(command.matches("user login"+
                    "(?=.*?-u \\S+)(?=.*?-p \\S+)(:? -(:?p|u) \\S+){2}")){
                manageLogin(true, command);
            }
            else if (command.matches("menu exit")) {
                break;
            } else if (command.startsWith("menu ")) {
                handleMenuOrders(command);
            } else if (command.matches("help")) {
                help();
            } else {
                Printer.printInvalidCommand();
            }
        }

    }

    private void manageLogin(boolean isAbbreviated,String command) {

        Matcher matcher = controller.Utils.getMatcher(command, "user login (.+)");

        matcher.find();
        String username;
        String password;
        if(isAbbreviated){
            username = Utils.getDataInCommandByKey(matcher.group(1), "-u");
            password = Utils.getDataInCommandByKey(matcher.group(1), "-p");
        }
        else{
            username = Utils.getDataInCommandByKey(matcher.group(1), "--username");
            password = Utils.getDataInCommandByKey(matcher.group(1), "--password");
        }
        if (username == null | password == null) {
            Printer.printInvalidCommand();
            return;
        }

        if (!Utils.checkFormatValidity(Utils.getHashMap("username", username,
                "password", password))) {
            return;
        }

        String commandToSendToServer = "user login" +
                " --username " + username + " --password " + password;

        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient(commandToSendToServer, username, menuName));

        Printer.print(data.getMessage());

        if (data.getMessageType().equals(MessageType.ERROR)) {
            return;
        }

        login(username);
    }

    private void login(String username) {
        MainMenu.getInstance().run(username);
    }

    private void manageCreatingAccount(boolean isAbbreviated,String command) {

        Matcher matcher = Utils.getMatcher(command, "user create (.+)");

        matcher.find();
        String username;
        String password;
        String nickname;
        if(isAbbreviated){
            username = Utils.getDataInCommandByKey(matcher.group(1), "-u");
            password = Utils.getDataInCommandByKey(matcher.group(1), "-p");
            nickname = Utils.getDataInCommandByKey(matcher.group(1), "-n");
        }
        else {
            username = Utils.getDataInCommandByKey(matcher.group(1), "--username");
            password = Utils.getDataInCommandByKey(matcher.group(1), "--password");
            nickname = Utils.getDataInCommandByKey(matcher.group(1), "--nickname");
        }

        if (username == null | password == null | nickname == null) {
            Printer.printInvalidCommand();
            return;
        }

        if (!Utils.checkFormatValidity(Utils.getHashMap("username", username,
                "password", password, "nickname", nickname))) {
            return;
        }

        if (Utils.isPasswordWeak(password)) {
            RegisterProfilePrinter.printPasswordSafetyError();
            return;
        }

        String commandToSendToServer = "user create --username " + username +
                " --nickname " + nickname + " --password " + password;

        DataForClientFromServer data = sendDataToServer
                (new DataForServerFromClient(commandToSendToServer, username, menuName));

        Printer.print(data.getMessage());
    }

    private void help() {
        System.out.print("""
                user create --username [username] --nickname [nickname] --password [password]
                user create -u [username] -n [nickname] -p [password]
                user login --username <username> --password <password>
                user login -u <username> -p <password>
                help
                menu show-current
                menu enter [menu name]
                menu exit
                """);
    }

}
