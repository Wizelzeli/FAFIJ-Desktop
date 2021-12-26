package sample.main.tasks;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import sample.data.Storage;
import sample.data.postbodies.LoginJournal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AddJournalTask extends Task<HttpResponse<String>> { //void

    LoginJournal loginJournal;

    public AddJournalTask(LoginJournal loginJournal) {
        this.loginJournal = loginJournal;
    }

    @Override
    protected HttpResponse<String> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/createJournal"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(loginJournal)))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
