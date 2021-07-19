package AnythingIWant;

import com.google.gson.Gson;
import controller.ClientDataController;
import controller.DataBaseControllers.CSVDataBaseController;
import model.Card.Card;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Network {
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
                    processCommandAndGiveOutput(command, dataOutputStream);
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
            dataOutputStream.writeUTF(gson.toJson(dataBack));
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CSVDataBaseController.load();
        setNumberOfCard();
        initializeNetwork();
    }

    private static void setNumberOfCard() {
        File file = new File("Resource/Cards/getCardCountsAndRules.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file, false);
                for (String cardName : CSVDataBaseController.getClassByName.keySet()) {
                    fileWriter.append(cardName).append(",").append(Integer.toString(100))
                            .append(",").append("false").append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
