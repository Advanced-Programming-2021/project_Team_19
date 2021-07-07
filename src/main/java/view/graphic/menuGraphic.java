package view.graphic;

import controller.DataBaseControllers.CSVDataBaseController;
import controller.DataBaseControllers.DataBaseController;
import controller.DataBaseControllers.UserDataBaseController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import controller.DataBaseControllers.CSVDataBaseController;
import view.Menu.*;

public class menuGraphic extends Application {

    public static double sceneX = 800;
    public static double sceneY = 600;

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        CSVDataBaseController.load();
        stage = primaryStage;
        Scene scene = new Scene(new Pane(), sceneX, sceneY);
        stage.setScene(scene);
        scene.getStylesheets().add("CSS/Css.css");
//        new Shop().testRun(UserDataBaseController.getUserByUsername("mohammad"));
//        new RockPaper().run();
//        WelcomeMenu.getInstance().run();
        //testRun function of your menu for test here
        stage.show();
    }


    public static Popup createPopup(final String message) {

        final Popup popup = new Popup();
        popup.setX(stage.getX() + 15);
        popup.setY(stage.getY() + stage.getHeight() - 65);
        popup.setHideOnEscape(true);

        Label label = new Label(" " + message + " ");
        label.setAlignment(Pos.CENTER);

        label.setMinWidth(80);
        label.setMinHeight(50);
        label.getStyleClass().add("popup1");

        popup.getContent().add(label);

        return popup;
    }

    public static Popup showPopupMessage(Stage stage, String message, double x, double y) {

        final Popup popup = new Popup();
        popup.setX(x);
        popup.setY(y);

        Label label = new Label(" " + message + " ");
        label.setAlignment(Pos.CENTER);

        label.setMinWidth(80);
        label.setMinHeight(50);
        label.getStyleClass().add("popup1");

        popup.getContent().add(label);
        popup.show(stage);
        return popup;
    }

    public static void showPopupMessage(final String message) {

        final Popup popup = createPopup(message);
        popup.show(stage);

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
    }


    public static void setBackButton(Button button) {

        button.setMinSize(40, 40);
        button.setMaxSize(40, 40);
        readyCursorForButton(button);

        Image img = new Image("Pictures/backArrow.png");
        ImageView view = new ImageView(img);

        view.fitHeightProperty().bind(button.heightProperty());
        view.fitWidthProperty().bind(button.widthProperty());
        button.setGraphic(view);
    }

    public static void readyCursorForButton(Button button) {
        button.setOnMouseEntered(mouseEvent -> stage.getScene().setCursor(Cursor.HAND));

        button.setOnMouseExited(mouseEvent -> stage.getScene().setCursor(Cursor.DEFAULT));
    }

    public void readyFxmlButtonsForCursor(Pane pane) {
        for (Node child : pane.getChildren()) {
            if (child instanceof Button)
                readyCursorForButton((Button) child);
        }
    }


    public static VBox setSeveralChoiceButtons(String... choices) {

        VBox buttonBox = new VBox(15);
        buttonBox.setLayoutX(300);
        buttonBox.setLayoutY(150);
        buttonBox.setAlignment(Pos.CENTER);

        for (String choice : choices) {
            Button button = new Button();
            button.setText(choice);
            button.setAlignment(Pos.CENTER);
            button.setTextAlignment(TextAlignment.CENTER);
            readyCursorForButton(button);

            buttonBox.getChildren().add(button);
        }

        return buttonBox;
    }

    public static HBox textFieldGridToEnterInfo(String... fieldNames) {
        VBox fieldNamesBox = new VBox(13);
        for (String fieldName : fieldNames) {
            fieldNamesBox.getChildren().add(new Label(fieldName));
        }
        VBox textFieldsBox = new VBox(5);
        for (int i = 0; i < fieldNames.length; i++) {
            textFieldsBox.getChildren().add(new TextField());
        }

        HBox fieldGrid = new HBox(10);
        fieldGrid.getChildren().addAll(fieldNamesBox, textFieldsBox);

        return fieldGrid;
    }

    public static void main(String[] args) {
        CSVDataBaseController.load();
        DataBaseController.makeResourceDirectory();
        launch(args);
    }
}