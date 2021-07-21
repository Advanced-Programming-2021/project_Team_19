package AnythingIWant;

import com.google.gson.Gson;
import controller.ClientDataController;
import controller.DataBaseControllers.CSVDataBaseController;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;
import model.Pair;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Network {

    private static HashMap<String, User> UserByToken = new HashMap<>();

    public static void addToken(String token, User user) {
        UserByToken.put(token, user);
    }

    public static void removeToken(String token) {
        UserByToken.remove(token);
    }

    public static User getUserByToken(String token) {
        return UserByToken.get(token);
    }

    private static void initializeNetwork() {
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            while (true) {
                Socket socket = serverSocket.accept();
                startNewConnection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startNewConnection(Socket socket) {
        new Thread(() -> {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    String command = dataInputStream.readUTF();
                    if (command.equals("exit")){
                        dataInputStream.close();
                        dataOutputStream.close();
                        socket.close();
                        break;
                    } else {
                        processCommandAndGiveOutput(command, dataOutputStream);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void processCommandAndGiveOutput(String command, DataOutputStream dataOutputStream) {
        Gson gson = new Gson();
        DataForServerFromClient data = gson.fromJson(command, DataForServerFromClient.class);
        System.out.println(data.getMessage());
        DataForClientFromServer dataBack = ClientDataController.handleMessageOfClientAndGetFeedback(data);
        try {
            String result = gson.toJson(dataBack);
            System.out.println("result       " + result);
            dataOutputStream.writeUTF(result);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CSVDataBaseController.load();
        CSVDataBaseController.setNumberOfCard();
        new Thread(Network::initializeNetwork).start();
        ShopAdmin.getInstance().run();
    }


}
