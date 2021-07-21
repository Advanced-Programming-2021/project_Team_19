package model;

public class Message {
    public String author;
    public String message;

    public Message(String author, String message){
        this.author = author;
        this.message = message;
    }

    public void editMessage(String newMessageText){
        this.message = newMessageText;
    }

    @Override
    public String toString() {
        return author + "~" + message;
    }
}
