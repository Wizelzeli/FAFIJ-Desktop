package sample.main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.postbodies.CategoryLoginJournal;
import sample.data.postbodies.NotePost;
import sample.main.tasks.AddCategoryTask;
import sample.main.tasks.AddNoteTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNoteController {
    @FXML
    public Button addNote;
    @FXML
    public Button backToJournal;
    @FXML
    public TextField sumField;
    @FXML
    public TextField categoryField;
    @FXML
    public TextArea commentArea;
    @FXML
    public Label message;

    @FXML
    public void add(ActionEvent actionEvent) throws IOException {
        if (sumField.getText().equals("") || categoryField.getText().equals("")) {
            message.setText("Введите данные");
            return;
        }
        try {
            Long.parseLong(sumField.getText());
        } catch (Exception e) {
            message.setText("Недействительное значение денежной суммы");
            return;
        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        NotePost notePost = new NotePost(format.format(cal.getTime())
                , Long.parseLong(sumField.getText())
                , categoryField.getText()
                , commentArea.getText()
                , Storage.getJournal());
        AddNoteTask addNoteTask = new AddNoteTask(notePost);
        addNoteTask.setOnSucceeded(e -> {
            if (addNoteTask.getValue().statusCode() == 500) {
                message.setText("Категория не найдена");
                return;
            }
            if (addNoteTask.getValue().statusCode() == 201) {
                back(actionEvent);
            }
        });
        new Thread(addNoteTask).start();
    }

    @FXML
    public void back(ActionEvent actionEvent) {
        Stage stage = (Stage) addNote.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/journal.fxml"));
            stage.setScene(new Scene(root, 700, 500));
            stage.setTitle("FAFIJ Desktop : : Журнал");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}