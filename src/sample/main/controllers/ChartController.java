package sample.main.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.otherdata.Note;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartController {
    @FXML
    public PieChart pieChart;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/private/listNote"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Storage.getToken())
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(Storage.getJournal())))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Note> notes = new Gson().fromJson(response.body(), new TypeToken<ArrayList<Note>>(){}.getType());
        Map<String, Long> categories = new HashMap<>();
        for (Note note : notes) {
            if (note.getSum() < 0) {
                if (!categories.containsKey(note.getCategory().getName())) {
                    categories.put(note.getCategory().getName(), 0L);
                }
                categories.put(note.getCategory().getName(),
                        categories.get(note.getCategory().getName()) + Math.abs(note.getSum()));
            }
        }
        List<String> keys = new ArrayList<>(categories.keySet());
        for (String key : keys) {
            pieChart.getData().add(new PieChart.Data(key, categories.get(key)));
        }
        pieChart.setLegendSide(Side.LEFT);
        pieChart.setStartAngle(30);

    }
}
