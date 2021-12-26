package sample.main.tasks;//NoteLoginJournal noteLoginJournal

import com.google.gson.Gson;
import javafx.concurrent.Task;
import sample.data.Storage;
import sample.data.postbodies.NoteLoginJournal;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DeleteNoteTask extends Task<HttpResponse<String>> { //void

    NoteLoginJournal noteLoginJournal;

    public DeleteNoteTask(NoteLoginJournal noteLoginJournal) {
        this.noteLoginJournal = noteLoginJournal;
    }

    @Override
    protected HttpResponse<String> call() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/deleteNote"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(noteLoginJournal)))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
