package sample.main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.data.Storage;
import sample.data.otherdata.*;
import sample.data.postbodies.NoteLoginJournal;
import sample.main.tasks.DeleteCategoryTask;
import sample.main.tasks.DeleteNoteTask;
import sample.main.tasks.LoginTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class NotesController {
    @FXML
    public Button deleteButton;
    @FXML
    public Label error_message;
    @FXML
    public TableView<Note> tableNotes;
    @FXML
    public TableColumn<Note, String> dateColumn;
    @FXML
    public TableColumn<Note, Long> sumColumn;
    @FXML
    public TableColumn<Note, String> commentColumn;
    @FXML
    public TableColumn<Note, String > categoryColumn;
    @FXML
    public Button deleteNoteButton;
    @FXML
    public Button addNoteButton;

    @FXML
    private void initialize() throws IOException, InterruptedException {
        updateData();
    }

    public void updateData() throws IOException, InterruptedException {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        categoryColumn.setCellValueFactory(cg -> new SimpleStringProperty(cg.getValue().getCategory().getName()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/listNote"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(Storage.getJournal())))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Note> notesList = new Gson().fromJson(response.body(), new TypeToken<ArrayList<Note>>(){}.getType());
        tableNotes.setItems(FXCollections.observableList(notesList));
    }

    @FXML
    public void delete(ActionEvent actionEvent) throws IOException {
        if (tableNotes.getSelectionModel().getSelectedItem() == null) {
            error_message.setText("Выберете запись");
            return;
        }
        Note note = tableNotes.getSelectionModel().getSelectedItem();
        NoteLoginJournal noteLoginJournal = new NoteLoginJournal(note.getId(), Storage.getLogin(), Storage.getJournal());
        DeleteNoteTask deleteNoteTask = new DeleteNoteTask(noteLoginJournal);
        deleteNoteTask.setOnSucceeded(e-> {
            if (deleteNoteTask.getValue().statusCode() == 406) {
                error_message.setText("Недостаточно прав");
                return;
            }
            if (deleteNoteTask.getValue().statusCode() == 201) {
                try {
                    updateData();
                    error_message.setText("Запись удалена");
                    return;
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }
            error_message.setText("Неизвестная ошибка");
        });
        new Thread(deleteNoteTask).start();
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        Stage stage = (Stage) addNoteButton.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/add_note.fxml"));
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("FAFIJ Desktop : : Добавление записи");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
