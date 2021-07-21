package controller.MenuControllers;

import controller.Utils;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;
import model.Message;
import model.User;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ChatRoomController {
    public static ArrayList<Message> messages = new ArrayList<>();

    public static DataForClientFromServer run(User user, String command){
        if (command.equals("get all messages")){
            return getAllMessages();
        } else if (command.matches("delete message \\d+")){
            return deleteMessage(Integer.parseInt(command.substring(15)), user.getUsername());
        } else if (command.matches("add message .+")){
            return addMessage(Utils.getMatcher(command, "add message (.+)"), user.getUsername());
        } else if (command.matches("edit message \\d+ .+")){
            return editMessage(Utils.getMatcher(command, "edit message (\\d+) (.+)"), user.getUsername());
        }
        return null;
    }

    public static DataForClientFromServer addMessage(Matcher matcher, String username){
        matcher.find();
        String messageText = matcher.group(1);
        messages.add(new Message(username, messageText));
        return getAllMessages();
    }

    public static DataForClientFromServer editMessage(Matcher matcher, String username){
        matcher.find();
        int id = Integer.parseInt(matcher.group(1));
        String newMessageText = matcher.group(2);
        if (!messages.get(id).author.equals(username)){
            return new DataForClientFromServer("you cannot edit this message", MessageType.ERROR);
        }
        messages.get(id).editMessage(newMessageText);
        return getAllMessages();
    }

    public static DataForClientFromServer deleteMessage(int id, String username){
        if (messages.get(id).author.equals(username)){
            return new DataForClientFromServer("you cannot delete this message", MessageType.ERROR);
        }

        messages.remove(id);
        return getAllMessages();
    }

    public static DataForClientFromServer getAllMessages(){
        ArrayList<String> toReturn = new ArrayList<>();
        for (Message message : messages) {
            toReturn.add(message.toString());
        }
        return new DataForClientFromServer(toReturn, MessageType.SUCCESSFUL);
    }
}
