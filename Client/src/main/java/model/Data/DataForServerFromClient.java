package model.Data;

public class DataForServerFromClient {

    public String message;
    public String token;
    public String menuName;

    public DataForServerFromClient(String message, String token, String menuName) {
        this.message = message;
        this.token = token;
        this.menuName = menuName;
    }

    public DataForServerFromClient(String message, String menuName) {
        this.message = message;
        this.token = null;
        this.menuName = menuName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


}
