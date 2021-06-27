package view;

import javafx.scene.control.Label;
import view.Printer.Printer;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Matcher getMatcher(String matchingStr, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(matchingStr);
    }

    public static String getFirstGroupInMatcher(Matcher matcher) {
        matcher.find();
        return matcher.group(1);
    }

    public static <key, value> HashMap<key, value> getHashMap(Object... keysAndValues) {

        HashMap<key, value> hashMap = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            hashMap.put((key) keysAndValues[i], (value) keysAndValues[i + 1]);
        }
        return hashMap;
    }

    public static String getDataInCommandByKey(String command, String key) {
        Matcher matcher = controller.Utils.getMatcher(command, key + " (.+?)(:? -|$)");
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    public static boolean isPasswordWeak(String password) {

        return (password.length() < 5 ||
                !password.matches(".*?\\d.*") ||
                !password.matches(".*?[a-z].*") ||
                !password.matches(".*?[A-Z].*"));

    }

    public static boolean checkFormatValidity(Label label, HashMap<String, String> userData) {

        for (String dataKey : userData.keySet()) {
            if (isFormatInvalid(userData.get(dataKey))) {
                Printer.setFailureResponseToLabel(label, dataKey + " format is not valid");
                return false;
            }
        }

        return true;
    }

    public static boolean isFormatInvalid(String data) {
        return !data.matches("\\w+");
    }

}
