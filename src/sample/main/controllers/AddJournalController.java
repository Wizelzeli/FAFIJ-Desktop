package sample.main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.otherdata.Note;
import sample.data.postbodies.LoginJournal;
import sample.main.tasks.AddJournalTask;

import java.io.IOException;

public class AddJournalController {
    @FXML
    public Button add_new_journal_button;
    @FXML
    public TextField journal_name_edittext;
    @FXML
    public Label error_message;
    @FXML
    public Button back;


    @FXML
    public void addNewJournal(ActionEvent actionEvent) throws IOException {
        String journalName = journal_name_edittext.getText();
        if (journalName.equals("")) {
            error_message.setText("Введите название журнала");
            return;
        }
        if (journalName.length() > 25) {
            error_message.setText("Количество символов не должно превышать 25");
            return;
        }
        LoginJournal loginJournal = new LoginJournal(Storage.getLogin(), journalName);
        AddJournalTask addJournalTask = new AddJournalTask(loginJournal);
        addJournalTask.setOnSucceeded(e -> {
            if (addJournalTask.getValue().statusCode() >= 300) showMessage(addJournalTask.getValue().statusCode());
            else goBack();
        });
        new Thread(addJournalTask).start();
    }

    public void showMessage(int code) {
        String message = "Неизвестная ошибка";
        if (code == 406) message = "Журнал с таким названием уже существует";
        error_message.setText(message);
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        goBack();
    }

    private void goBack() {
        Stage stage = (Stage) add_new_journal_button.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/change_journal.fxml"));
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("FAFIJ Desktop : : Смена журнала");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
