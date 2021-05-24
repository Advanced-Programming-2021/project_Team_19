package view.Printer;


import java.util.LinkedList;
import java.util.Queue;

public class Printer {

    static Queue<String> responses  = new LinkedList<>();

    private static boolean isInTestingMod=false;

    public static void setTestingMod(){
        isInTestingMod=true;
    }

    public static void print(Object object){
        if(isInTestingMod){
            responses.add(object.toString());
        }
        else {
            System.out.println(object.toString());
        }
    }

    public static  String getResponse(){
        String temp = responses.poll();
        if(temp.matches("\\s+")){
            return getResponse();
        }
        else{
            return temp;
        }
    }


    public static void printInvalidCommand(){
        print("invalid command!");
    }

}
