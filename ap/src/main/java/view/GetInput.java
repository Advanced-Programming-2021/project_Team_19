package view;

import Test.ProjectTest;

import java.util.Scanner;



public class GetInput {

    static Scanner scanner = new Scanner(System.in);

    static boolean testMod=false;

    public static void setTestingMod(){
        testMod=true;
    }

    public static String getString() {
        if(testMod) {
            return ProjectTest.giveInput();
        }
        else {
            return scanner.nextLine().replaceAll("\\s+", " ").trim();
        }
    }





}
