package view.Menu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreBoardMenu extends Menu {

    private BorderPane mainPane = new BorderPane();
    private TableView tableView = new TableView();

    public ScoreBoardMenu() {
        super("Scoreboard Menu");
    }

    public void run(User user) {

        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 0, 10));

        Label title = new Label("Score Board");
        title.setFont(new Font("Arial", 20));
        setTableView(user);

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        box.getChildren().addAll(title, tableView, backButton);
        mainPane.getStylesheets().add("CSS/Css.css");
        mainPane.setId("background1");
        mainPane.setCenter(box);

        stage.getScene().setRoot(mainPane);
    }

    private void setTableView(User user) {

        TableColumn<Person, String> column0 = new TableColumn<>("rank");
        column0.setCellValueFactory(new PropertyValueFactory<>("rank"));
        column0.setMinWidth(100);

        TableColumn<Person, String> column1 = new TableColumn<>("username");
        column1.setCellValueFactory(new PropertyValueFactory<>("username"));
        column1.setMinWidth(100);

        TableColumn<Person, String> column2 = new TableColumn<>("score");
        column2.setCellValueFactory(new PropertyValueFactory<>("score"));
        column2.setMinWidth(100);

        tableView.getColumns().addAll(column0, column1, column2);

        tableView.setMaxWidth(301);

        ArrayList<Person> people = getPersons();

        Person currentPerson = null;

        for (Person person : people) {
            if(person.getUsername().equals(user.getUsername())){
                currentPerson = person;
            }
            tableView.getItems().add(person);
        }

        setUserColor(currentPerson);
    }

    private void setUserColor(Person currentPerson){

        if (currentPerson != null) {

            ObjectProperty<Person> criticalPerson = new SimpleObjectProperty<>();
            criticalPerson.set(currentPerson);

            tableView.setRowFactory(tv -> {
                TableRow<Person> row = new TableRow<>();
                BooleanBinding critical = row.itemProperty().isEqualTo(criticalPerson).and(row.itemProperty().isNotNull());
                row.styleProperty().bind(Bindings.when(critical)
                        .then("-fx-background-color: red ;")
                        .otherwise(""));
                return row;
            });

        }
    }

    public ArrayList<Person> getPersons() {

        Type type = new TypeToken<ArrayList<Person>>() {
        }.getType();

        Gson gson = new GsonBuilder().create();

        ArrayList<Person> people = gson.fromJson(sendDataToServer(
                new DataForServerFromClient
                        ("scoreboard show", menuName)).getMessage(), type);

        for(Person person : people){
            System.out.println(person);
        }
        return people;


    }

}
