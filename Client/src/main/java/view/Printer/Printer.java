package view.Printer;


import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Data.DataForClientFromServer;
import model.Enums.MessageType;

import java.util.LinkedList;
import java.util.Queue;

public class Printer {

    static Queue<String> responses = new LinkedList<>();

    private static boolean isInTestingMod = false;

    public static void setTestingMod() {
        isInTestingMod = true;
    }

    public static void print(Object object) {
        if (isInTestingMod) {
            responses.add(object.toString());
        } else {
            System.out.println(object.toString());
        }
    }

    public static String getResponse() {
        return responses.poll();
    }

    public static void clearLog() {
        responses.clear();
    }

    public static void printInvalidCommand() {
        print("invalid command!");
    }

    public static void setSuccessResponseToLabel(Label label, String text){
        label.setText(text);
        label.setTextFill(Color.GREEN);
        label.setFont(new Font(16));
    }

    public static void setFailureResponseToLabel(Label label, String text){
        label.setText(text);
        label.setTextFill(Color.RED);
        label.setFont(new Font(16));
    }

    public static void setAppropriateResponseToLabelFromData(DataForClientFromServer data, Label label){
        if (data.getMessageType().equals(MessageType.SUCCESSFUL)) {
            Printer.setSuccessResponseToLabel(label, data.getMessage());
        } else {
            Printer.setFailureResponseToLabel(label, data.getMessage());
        }
    }

}
