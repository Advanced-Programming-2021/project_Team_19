package AnythingIWant;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.Utils;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ShopAdmin {
    private static ShopAdmin shopAdmin;

    public static ShopAdmin getInstance() {
        if (shopAdmin == null) {
            shopAdmin = new ShopAdmin();
        }

        return shopAdmin;
    }

    private ShopAdmin() {}

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.matches("forbide --card (.+)")) {
                forbideCard(Utils.getMatcher(command, "forbide --card (.+)"));
            } else if (command.matches("allow --card (.+)")) {
                allowCard(Utils.getMatcher(command, "allow --card (.+)"));
            } else if (command.matches("increase --card .*? --amount \\d+")) {
                increaseCard(Utils.getMatcher(command, "increase --card (.*?) --amount (\\d+)"));
            } else if (command.matches("decrease --card .*? --amount \\d+")) {
                decreaseCard(Utils.getMatcher(command, "decrease --card (.*?) --amount (\\d+)"));
            } else if (command.equals("help")) {
                help();
            }
        }
    }

    private void forbideCard(Matcher matcher) {
        matcher.matches();
        String cardName = matcher.group(1);
        if (CSVDataBaseController.isCardNameValid(cardName)) {
            CSVDataBaseController.setCardState(cardName, true);
            System.out.println("successful");
        } else {
            System.out.println("no card with such name exists");
        }
    }

    private void allowCard(Matcher matcher) {
        matcher.matches();
        String cardName = matcher.group(1);
        if (CSVDataBaseController.isCardNameValid(cardName)) {
            CSVDataBaseController.setCardState(cardName, false);
            System.out.println("successful");
        } else {
            System.out.println("no card with such name exists");
        }
    }

    private void increaseCard(Matcher matcher) {
        matcher.matches();
        String cardName = matcher.group(1);
        int amount = Integer.parseInt(matcher.group(2));
        if (CSVDataBaseController.isCardNameValid(cardName)) {
            CSVDataBaseController.increaseCardCount(cardName, amount);
            System.out.println("successful");
        } else {
            System.out.println("no card with such name exists");
        }
    }

    private void decreaseCard(Matcher matcher) {
        matcher.matches();
        String cardName = matcher.group(1);
        int amount = Integer.parseInt(matcher.group(2));
        if (CSVDataBaseController.isCardNameValid(cardName)) {
            CSVDataBaseController.increaseCardCount(cardName, -amount);
            System.out.println("successful");
        } else {
            System.out.println("no card with such name exists");
        }
    }

    private void help() {
        System.out.println("""
                forbide --card <cardName>
                allow --card <cardName>
                increase --card <cardName> --amount <amount>
                decrease --card <cardName> --amount <amount>
                help""");
    }
}
