import org.junit.jupiter.api.*;
import view.GetInput;
import view.Menu.WelcomeMenu;
import view.Printer.Printer;

public class LoginAndRegisterTest {
    @BeforeAll
    public static void setTestingMod() {
        GetInput.setTestingMod();
        Printer.setTestingMod();
    }


    @Test
    public void registerRandomUser() {
        GetInput.addCommand("user create --username " + Utils.getRandomString(10) + " --password Taha1506 --nickname " + Utils.getRandomString(10));
        GetInput.addCommand("user create --password Taha1506 --username " + Utils.getRandomString(10) + " --nickname " + Utils.getRandomString(10));
        GetInput.addCommand("menu exit");
        WelcomeMenu.getInstance().run();
        Assertions.assertEquals("user create successfully", Printer.getResponse());
        Assertions.assertEquals("user create successfully", Printer.getResponse());
    }

    @Test
    public void shortcutTest() {
        GetInput.addCommand("user create -u " + Utils.getRandomString(10) + " -p Taha1506 -n " + Utils.getRandomString(10));
        GetInput.addCommand("user create -p Taha1506 -u " + Utils.getRandomString(10) + " -n " + Utils.getRandomString(10));
        GetInput.addCommand("menu exit");
        WelcomeMenu.getInstance().run();
        Assertions.assertEquals("user create successfully", Printer.getResponse());
        Assertions.assertEquals("user create successfully", Printer.getResponse());
    }

}
