package AnythingIWant;

import com.google.gson.Gson;
import model.Data.DataForClientFromServer;
import model.Data.DataForServerFromClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientNetwork {
    private static ClientNetwork clientNetwork;

    public static ClientNetwork getInstance() {
        if (clientNetwork == null) {
            clientNetwork = new ClientNetwork();
        }
        return clientNetwork;
    }

    private ClientNetwork() {
        initializeClientNetwork();
    }

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    public void initializeClientNetwork() {
        try {
            Socket socket = new Socket("localhost", 7777);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            this.socket = socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataForClientFromServer sendMessageAndGetFeedBack(DataForServerFromClient data) {
        Gson gson = new Gson();
        try {
            dataOutputStream.writeUTF(gson.toJson(data));
            String feedBack = dataInputStream.readUTF();
            return gson.fromJson(feedBack, DataForClientFromServer.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
