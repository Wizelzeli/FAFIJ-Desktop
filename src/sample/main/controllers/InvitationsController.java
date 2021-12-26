package sample.main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.data.Storage;
import sample.data.otherdata.Invitation;
import sample.data.otherdata.Note;
import sample.data.postbodies.LoginJournal;
import sample.main.tasks.AcceptInviteTask;
import sample.main.tasks.DeclineInviteTask;
import sample.main.tasks.DeleteNoteTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class InvitationsController {
    @FXML
    public TableView<Invitation> tableInvitations;
    @FXML
    public TableColumn<Invitation, String> journalColumn;
    @FXML
    public TableColumn<Invitation, String> roleColumn;
    @FXML
    public Button acceptButton;
    @FXML
    public Button declineButton;

    @FXML
    private void initialize() throws IOException, InterruptedException {
        updateData();
    }

    public void updateData() throws IOException, InterruptedException {
        journalColumn.setCellValueFactory(cg -> new SimpleStringProperty(cg.getValue().getJournalName().getJournalName()));
        roleColumn.setCellValueFactory(cg -> new SimpleStringProperty(cg.getValue().getRole().getName()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/invitations"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(Storage.getLogin())))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Invitation> invitationsList = new Gson().fromJson(response.body(), new TypeToken<ArrayList<Invitation>>(){}.getType());
        tableInvitations.setItems(FXCollections.observableList(invitationsList));
    }

    @FXML
    public void accept(ActionEvent actionEvent) throws IOException {
        if (tableInvitations.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        LoginJournal loginJournal = new LoginJournal(Storage.getLogin(), tableInvitations.getSelectionModel().getSelectedItem().getJournalName().getJournalName());
        AcceptInviteTask acceptInviteTask = new AcceptInviteTask(loginJournal);
        acceptInviteTask.setOnSucceeded(e-> {
            if (acceptInviteTask.getValue().statusCode() == 200) {
                try {
                    updateData();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        new Thread(acceptInviteTask).start();
    }

    @FXML
    public void decline(ActionEvent actionEvent) throws IOException {
        if (tableInvitations.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        LoginJournal loginJournal = new LoginJournal(Storage.getLogin(), tableInvitations.getSelectionModel().getSelectedItem().getJournalName().getJournalName());
        DeclineInviteTask declineInviteTask = new DeclineInviteTask(loginJournal);
        declineInviteTask.setOnSucceeded(e-> {
            if (declineInviteTask.getValue().statusCode() == 200) {
                try {
                    updateData();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        new Thread(declineInviteTask).start();
    }
}
