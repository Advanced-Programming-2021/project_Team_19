package Test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.junit.jupiter.api.BeforeAll;
import view.GetInput;
import view.Menu.LoginMenu;
import view.Printer.Printer;

import java.util.LinkedList;
import java.util.Queue;

public class ProjectTest {
    private static final Queue<String> commands=new LinkedList<>();
    private static final Queue<String> responses=new LinkedList<>();

    public static String giveInput(){
        return commands.poll();
    }

    public static void getInput(String string){
        responses.add(string);
    }
    private boolean checkLastResponse(String string){
        return string.equals(responses.poll());
    }

    @BeforeAll
    public static  void setTestingMod(){
        GetInput.setTestingMod();
        Printer.setTestingMod();
    }

    @Test
    public void testRegister(){
        commands.add("user create --username hello --password ho --nickname badGuy");
        LoginMenu.getInstance().run();
        Assert.assertTrue(checkLastResponse("user cra"));
    }




}
