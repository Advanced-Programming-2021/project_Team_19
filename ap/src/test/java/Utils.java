public class Utils {

    public static String getRandomString(int n) {
        String letterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int index = (int) (letterSet.length() * Math.random());
            stringBuilder.append(letterSet.charAt(index));
        }
        return stringBuilder.toString();
    }
}
