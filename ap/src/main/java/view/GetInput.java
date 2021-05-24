package view;


import controller.DuelControllers.AI;
import controller.DuelControllers.GameData;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;



public class GetInput {
    private static Scanner scanner = new Scanner(System.in);

    private static boolean testMod = false;
    private static Queue<String> commands = new LinkedList<>();


    public static boolean AIMod = false;
    private static Scanner AIScanner;
    private static int scannerCounter;

    public static void initializeAIScanner(Scanner scanner, int counter){
        AIScanner = scanner;
        scannerCounter = counter;
    }

    public static void setTestingMod(){
        testMod=true;
    }


    public static String getString() {

        if(AIMod){
            if(scannerCounter > 0){
                scannerCounter--;
                return AIScanner.nextLine();
            } else{
                AI.run(GameData.getGameData(0));
                return getString();
            }
        }
        if(testMod) {
            return commands.poll();
        }
        else {
            return scanner.nextLine().replaceAll("\\s+", " ").trim();
        }
    }

    public static void addCommand(String command){
        commands.add(command);
    }

    public static int getInt(){
        return scanner.nextInt();
    }

}
