import controller.DataBaseControllers.UserDataBaseController;
import model.User;
import org.junit.jupiter.api.*;
import view.GetInput;
import view.Menu.LoginMenu;
import view.Printer.Printer;

public class LoginTest {
    static User user;

    @BeforeAll
    public static void createUser() {
        GetInput.setTestingMod();
        Printer.setTestingMod();
        user = new User(Utils.getRandomString(10), Utils.getRandomString(10), Utils.getRandomString(50));
        UserDataBaseController.createUser(user);
    }

    @Test
    public void loginTest() {
        GetInput.addCommand("user login --username " + user.getUsername() + " --password " + user.getPassword());
        GetInput.addCommand("menu exit");
        GetInput.addCommand("user login --password " + user.getPassword() + " --username " + user.getUsername());
        GetInput.addCommand("menu exit");
        GetInput.addCommand("menu exit");
        LoginMenu.getInstance().run();
        Assertions.assertEquals("user logged in successfully!", Printer.getResponse());
        Assertions.assertEquals("user logged in successfully!", Printer.getResponse());
    }

    @Test
    public void ShortcutLoginTest() {
        GetInput.addCommand("user login -u " + user.getUsername() + " -p " + user.getPassword());
        GetInput.addCommand("menu exit");
        GetInput.addCommand("user login -p " + user.getPassword() + " -u " + user.getUsername());
        GetInput.addCommand("menu exit");
        GetInput.addCommand("menu exit");
        LoginMenu.getInstance().run();
        Assertions.assertEquals("user logged in successfully!", Printer.getResponse());
        Assertions.assertEquals("user logged in successfully!", Printer.getResponse());
    }

}
