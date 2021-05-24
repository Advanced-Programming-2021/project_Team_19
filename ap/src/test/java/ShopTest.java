import controller.DataBaseControllers.UserDataBaseController;
import model.User;
import org.junit.jupiter.api.*;
import view.GetInput;
import view.Menu.ShopMenu;
import view.Printer.Printer;

public class ShopTest {
    static User user;
    @BeforeAll
    public static void createUser(){
        GetInput.setTestingMod();
        Printer.setTestingMod();
        user = new User(Utils.getRandomString(10),Utils.getRandomString(10),Utils.getRandomString(50));
        UserDataBaseController.createUser(user);
    }

    @Test
    public void buyCard(){
        GetInput.addCommand("shop buy Battle OX");
        GetInput.addCommand("menu exit");
        ShopMenu.getInstance().run(user.getUsername());
        user = UserDataBaseController.getUserByUsername(user.getUsername());
        Assertions.assertEquals("you successfully bought the card",Printer.getResponse());
        Assertions.assertEquals(user.getCards().size(),1);
    }
}
