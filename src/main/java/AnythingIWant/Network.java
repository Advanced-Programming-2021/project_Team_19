package AnythingIWant;

import com.google.gson.Gson;
import controller.ClientDataController;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private static Network network;

    public static Network getInstance() {
        if (network == null) {
            network = new Network();
        }
        return network;
    }

    private Network() {
        initializeNetwork();
    }

    public void initializeNetwork() {
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

    private void startNewConnection(Socket socket) {
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

    private void processCommandAndGiveOutput(String command, DataOutputStream dataOutputStream) {
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
        Network.getInstance();
    }
}
