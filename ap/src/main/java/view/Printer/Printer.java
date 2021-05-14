package view.Printer;

import Test.ProjectTest;

public class Printer {

    private static boolean isInTestingMod=false;

    public static void setTestingMod(){
        isInTestingMod=true;
    }

    public static void print(String string){
        if(isInTestingMod){
            ProjectTest.getInput(string);
        }
        else {
            System.out.println(string);
        }
    }

    public static void printInvalidCommand(){
        print("invalid command!");
    }

}
