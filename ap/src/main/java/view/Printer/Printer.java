package view.Printer;



public class Printer {

    private static boolean isInTestingMod=false;

    public static void setTestingMod(){
        isInTestingMod=true;
    }

    public static void print(Object object){
        if(isInTestingMod){
//            ProjectTest.getInput((String) object);
        }
        else {
            System.out.println(object);
        }
    }

    public static void printInvalidCommand(){
        print("invalid command!");
    }

}
