package view;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;



public class GetInput {

    static Queue<String> commands = new LinkedList<>();

    static Scanner scanner = new Scanner(System.in);

    static boolean testMod=false;

    public static void setTestingMod(){
        testMod=true;
    }

    public static String getString() {
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
