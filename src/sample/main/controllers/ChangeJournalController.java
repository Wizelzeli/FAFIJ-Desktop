package sample.main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.otherdata.Journal;
import sample.data.otherdata.Login;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class ChangeJournalController {
    @FXML
    public Button add_journal_button;
    @FXML
    public Button change_journal_button;
    @FXML
    public ListView<String> journals_list;
    @FXML
    public Button exit_button;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/userJournals"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(new Login(Storage.getLogin()))))
                .build();
        System.out.println(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Journal> journalsList = new Gson().fromJson(response.body(), new TypeToken<ArrayList<Journal>>(){}.getType());
        for (Journal journal : journalsList) {
            journals_list.getItems().add(journal.getJournalName());
        }
    }

    @FXML
    public void addClick(ActionEvent actionEvent) {
        Stage stage = (Stage) add_journal_button.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/add_journal.fxml"));
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("FAFIJ Desktop : : Добавление журнала");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void changeClick(ActionEvent actionEvent) throws IOException {
        Storage.setJournal(journals_list.getSelectionModel().getSelectedItem());
        Stage stage = (Stage) exit_button.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/journal.fxml"));
            stage.setScene(new Scene(root, 700, 500));
            stage.setTitle("FAFIJ Desktop : : Главная");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) exit_button.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/login.fxml"));
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("FAFIJ Desktop : : Авторизация / Регистрация");
            Storage.setLogin("");
            Storage.setJournal("");
            Storage.setToken("");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
