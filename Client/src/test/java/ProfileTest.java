import model.User;
import org.junit.jupiter.api.*;
import view.Menu.ProfileMenu;
import view.Printer.Printer;

public class ProfileTest {
    static User user;

    @BeforeAll
    public static void createUser() {
        GetInput.setTestingMod();
        Printer.setTestingMod();
        user = new User(Utils.getRandomString(10), Utils.getRandomString(10), Utils.getRandomString(50));
        UserDataBaseController.createUser(user);
    }

    @AfterEach
    public void deleteExtraCommands() {
        Printer.clearLog();
    }


    @Test
    public void testChangingNickName() {
        String newNickname = Utils.getRandomString(10);
        GetInput.addCommand("profile change --nickname " + newNickname);
        GetInput.addCommand("menu exit");
        ProfileMenu.getInstance().run(user.getUsername(), user.getNickname());
        user = UserDataBaseController.getUserByUsername(user.getUsername());
        Assertions.assertEquals("nickname changed successfully!", Printer.getResponse());
        Assertions.assertEquals(newNickname, user.getNickname());
    }

    @Test
    public void testChangingNickNameAbbreviated() {
        String newNickname = Utils.getRandomString(10);
        GetInput.addCommand("profile change -n " + newNickname);
        GetInput.addCommand("menu exit");
        ProfileMenu.getInstance().run(user.getUsername(), user.getNickname());
        user = UserDataBaseController.getUserByUsername(user.getUsername());
        Assertions.assertEquals("nickname changed successfully!", Printer.getResponse());
        System.out.println(Printer.getResponse());
        Assertions.assertEquals(newNickname, user.getNickname());
    }

    @Test
    public void changingPasswordTest() {
        String newPassword = Utils.getRandomString(50);
        GetInput.addCommand("profile change --password " + "--current " + user.getPassword() + " --new " + newPassword);
        GetInput.addCommand("menu exit");
        ProfileMenu.getInstance().run(user.getUsername(), user.getNickname());
        user = UserDataBaseController.getUserByUsername(user.getUsername());
        Assertions.assertEquals("password changed successfully!", Printer.getResponse());
        Assertions.assertEquals(newPassword, user.getPassword());
    }

    @Test
    public void changePasswordTestAbbreviated() {
        String newPassword = Utils.getRandomString(50);
        GetInput.addCommand("profile change -p " + "-c " + user.getPassword() + " -n " + newPassword);
        GetInput.addCommand("menu exit");
        ProfileMenu.getInstance().run(user.getUsername(), user.getNickname());
        user = UserDataBaseController.getUserByUsername(user.getUsername());
        Assertions.assertEquals("password changed successfully!", Printer.getResponse());
        Assertions.assertEquals(newPassword, user.getPassword());
    }

}
