package sample.main.tasks;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import sample.data.Storage;
import sample.data.postbodies.CategoryLoginJournal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AddCategoryTask extends Task<HttpResponse<String>> { //void

    CategoryLoginJournal categoryLoginJournal;

    public AddCategoryTask(CategoryLoginJournal categoryLoginJournal) {
        this.categoryLoginJournal = categoryLoginJournal;
    }

    @Override
    protected HttpResponse<String> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/addCategory"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(categoryLoginJournal)))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
