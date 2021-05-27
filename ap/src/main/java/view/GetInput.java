package view;


import controller.DuelControllers.AI;
import controller.DuelControllers.GameData;
import view.Printer.Printer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class GetInput {
    private static Scanner scanner = new Scanner(System.in);

    private static boolean testMod = false;
    private static Queue<String> commands = new LinkedList<>();

    private static Scanner AIScanner;
    private static int scannerCounter = 0;

    public static void initializeAIScanner(Scanner scanner, int counter) {
        AIScanner = scanner;
        scannerCounter = counter;
    }

    public static void setTestingMod() {
        testMod = true;
    }


    public static String getString() {

        if (AIMod()) {
            if (scannerCounter > 0) {
                scannerCounter--;
                String ans = AIScanner.nextLine();
                Printer.print(ans);
                return ans;
            } else {
                Printer.print("enter hehe for see AI power");
                scanner.nextLine();
//                try {
//                    Thread.sleep(1500);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                AI.run(GameData.getGameData(0));
                return getString();
            }
        }
        if (testMod) {
            return commands.poll();
        } else {
            return scanner.nextLine().replaceAll("\\s+", " ").trim();
        }
    }

    public static void addCommand(String command) {
        commands.add(command);
    }

    public static int getInt() {
        return scanner.nextInt();
    }

    public static boolean AIMod(){
        try {
            if (AI.getGamer(0).equals(GameData.getGameData(0).getCurrentGamer())) {
                return true;
            }
        }catch(NullPointerException e) {
            return false;
        }
        return false;
    }

}
