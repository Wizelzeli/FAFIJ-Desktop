package sample.main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import sample.data.Storage;
import sample.data.otherdata.Category;
import sample.data.otherdata.Journal;
import sample.data.otherdata.Login;
import sample.data.otherdata.Note;
import sample.data.postbodies.CategoryLoginJournal;
import sample.data.postbodies.NoteLoginJournal;
import sample.main.tasks.AddCategoryTask;
import sample.main.tasks.DeleteCategoryTask;
import sample.main.tasks.DeleteNoteTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

public class CategoryController {

    @FXML
    public Button addCategoryButton;
    @FXML
    public ListView<String> categoryList;
    @FXML
    public Button deleteCategoryButton;
    @FXML
    public Label messageAdd;
    @FXML
    public Label messageDelete;
    @FXML
    public TextField categoryTextField;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        updateData();
    }

    public void updateData() throws IOException, InterruptedException {
        categoryList.getItems().clear();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/listCategory"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(Storage.getJournal())))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Category> categoryArrayListList = new Gson().fromJson(response.body(), new TypeToken<ArrayList<Category>>() {
        }.getType());
        for (Category category : categoryArrayListList) {
            categoryList.getItems().add(category.getName());
        }
    }

    @FXML
    public void add(ActionEvent actionEvent) throws IOException {
        if (categoryTextField.getText().equals("")) {
            messageAdd.setText("Введите название категории");
            return;
        }
        CategoryLoginJournal categoryLoginJournal = new CategoryLoginJournal(categoryTextField.getText(), Storage.getLogin(), Storage.getJournal());
        AddCategoryTask addCategoryTask = new AddCategoryTask(categoryLoginJournal);
        addCategoryTask.setOnSucceeded(e -> {
            switch (addCategoryTask.getValue().statusCode()) {
                case 406:
                    messageAdd.setText("Недостаточно прав");
                    break;
                case 409:
                    messageAdd.setText("Категория уже существует");
                    break;
                case 201:
                    try {
                        updateData();
                        messageAdd.setText("Категория добавлена");
                    } catch (IOException | InterruptedException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                default:
                    messageAdd.setText("Неизвестная ошибка");
                    break;
            }
        });
        new Thread(addCategoryTask).start();
    }

    @FXML
    public void delete(ActionEvent actionEvent) throws IOException {
        if (categoryList.getSelectionModel().getSelectedItem() == null) {
            messageDelete.setText("Выберете запись");
            return;
        }
        CategoryLoginJournal categoryLoginJournal = new CategoryLoginJournal(categoryList.getSelectionModel().getSelectedItem(), Storage.getLogin(), Storage.getJournal());
        DeleteCategoryTask deleteCategoryTask = new DeleteCategoryTask(categoryLoginJournal);
        deleteCategoryTask.setOnSucceeded(e -> {
            switch (deleteCategoryTask.getValue().statusCode()) {
                case 406:
                    messageDelete.setText("Недостаточно прав");
                    break;
                case 201:
                    try {
                        updateData();
                        messageDelete.setText("Категория удалена");
                    } catch (IOException | InterruptedException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                default:
                    messageDelete.setText("Неизвестная ошибка");
                    break;
            }
        });
        new Thread(deleteCategoryTask).start();
    }
}
