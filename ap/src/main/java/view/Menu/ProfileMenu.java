package view.Menu;

import model.Data.DataForServerFromClient;
import view.GetInput;
import view.Printer.Printer;
import view.Utils;

import java.util.regex.Matcher;

public class ProfileMenu extends Menu{

    private static ProfileMenu instance = null;

    private ProfileMenu() {
        super("Profile Menu");
    }

    public static ProfileMenu getInstance() {

        if (instance == null) {
            instance = new ProfileMenu();
        }
        return instance;

    }
    public void run(String username) {
        setUsername(username);

        while (true) {
            String command;
            command = GetInput.getString();
            if (command.matches("profile change --nickname \\S+")) {
                changeNickName(false, command, Utils.getMatcher(command, "profile change (.+)"));
            } else if(command.matches("profile change -n \\S+")){
                changeNickName(true, command, Utils.getMatcher(command, "profile change (.+)"));
            }
            else if (command.matches("profile change" +
                    "(?=.*?--password)(?=.*?--current \\S+)(?=.*--new \\S+)" +
                    "( --((current \\S+)|(new \\S+)|(password))){3}")) {

                changePassword(false, Utils.getMatcher(command, "profile change (.+)"));
            } else if(command.matches("profile change" +
                    "(?=.*?-p)(?=.*?-c \\S+)(?=.*?-n \\S+)" +
                    "(:? -((c \\S+)|(n \\S+)|(p))){3}")){
                changePassword(true, Utils.getMatcher(command,"profile change (.+)"));
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


    private void changeNickName(boolean isAbbreviated, String command, Matcher matcher) {

        matcher.find();
        String nickname;
        if(isAbbreviated){
            nickname = Utils.getDataInCommandByKey(matcher.group(1),"-n");
            command=command.replaceAll("-n","--nickname");
        }
        else{
            nickname = Utils.getDataInCommandByKey(matcher.group(1), "--nickname");
        }

        if(!Utils.checkFormatValidity(Utils.getHashMap
                ("nickname", nickname))){
            return;
        }

        Printer.print(sendDataToServer(
                new DataForServerFromClient(command ,username, menuName)).getMessage());
    }

    private void changePassword(boolean isAbbreviated, Matcher matcher) {
        matcher.find();
        String currentPassword;
        String newPassword;
        if(isAbbreviated){
            currentPassword = Utils.getDataInCommandByKey(matcher.group(1),"-c");
            newPassword = Utils.getDataInCommandByKey(matcher.group(1),"-n");
        }
        else{
            currentPassword = Utils.getDataInCommandByKey(matcher.group(1), "--current");
            newPassword = Utils.getDataInCommandByKey(matcher.group(1), "--new");
        }

        if(!Utils.checkFormatValidity(
                Utils.getHashMap(
                        "password", currentPassword, "newPassword", newPassword))){
            return;
        }

        Printer.print(sendDataToServer(
                new DataForServerFromClient("profile change --password" +
                        "--current " + currentPassword + " --new " + newPassword
                        ,username, menuName)).getMessage());

    }


    private void help() {
        System.out.print("""
                profile change --nickname [nickname]
                profile change -n [nickname]
                profile change --password --current [current password] --new [new password]
                profile change -p -c [current password] -n [new password]
                help
                menu show-current
                menu [menu name]
                menu exit
                """);

    }


}
