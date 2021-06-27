package view.Menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Data.DataForServerFromClient;
import model.Person;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreBoardMenu extends Menu {

    private BorderPane mainPane = new BorderPane();
    private TableView tableView = new TableView();
    private HBox buttonBox = new HBox(5);

    public ScoreBoardMenu() {
        super("Scoreboard Menu");
    }

    public void run() {

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 10));

        Label title = new Label("Score Board");
        title.setFont(new Font("Arial", 20));
        setButtons();
        setTableView();

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance().run(username));

        box.getChildren().addAll(title, tableView, backButton);
        mainPane.setCenter(box);

        stage.getScene().setRoot(mainPane);
    }

    private void setTableView() {

        TableColumn<Person, String> column0 = new TableColumn<>("rank");
        column0.setCellValueFactory(new PropertyValueFactory<>("rank"));
        column0.setMinWidth(100);

        TableColumn<Person, String> column1 = new TableColumn<>("username");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));
        column1.setMinWidth(100);

        TableColumn<Person, String> column2 = new TableColumn<>("score");
        column2.setCellValueFactory(new PropertyValueFactory<>("score"));
        column2.setMinWidth(100);

        tableView.getColumns().add(column0);
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        tableView.setMaxWidth(301);

        ArrayList<Person> people = getPersons();
        for (Person person : people) {
            tableView.getItems().add(person);
        }
    }

    private void setButtons() {

//        Button backButton = new Button();
//        setBackButton(backButton);
//        User tempUser = currentUser;
//
//        backButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if(tempUser == null){
//                    new FirstPage().run();
//                }else{
//                    new MainMenu().run(currentUser);
//                }
//
//            }
//        });
//
//        buttonBox.getChildren().add(backButton);
//        buttonBox.getChildren().add(Audio.getAudioButton());
//        mainPane.setTop(buttonBox);
    }


    public ArrayList<Person> getPersons() {

        Type type = new TypeToken<ArrayList<Person>>() {
        }.getType();

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(sendDataToServer(
                new DataForServerFromClient
                        ("scoreboard show", menuName)).getMessage(), type);


    }

}
