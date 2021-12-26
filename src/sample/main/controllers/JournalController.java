package sample.main.controllers;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.otherdata.Note;

import java.io.IOException;
import java.net.URL;

public class JournalController {

    @FXML
    public BorderPane border_pane;
    @FXML
    private VBox v_box;


    @FXML
    public void showViewNotes(ActionEvent actionEvent) {
        loadFXML(getClass().getResource("/sample/resources/fxml/notes.fxml"));
    }
    @FXML
    public void showViewCategories(ActionEvent actionEvent) {
        loadFXML(getClass().getResource("/sample/resources/fxml/categories.fxml"));
    }
    @FXML
    public void showViewChart(ActionEvent actionEvent) {
        loadFXML(getClass().getResource("/sample/resources/fxml/chart.fxml"));
    }
    @FXML
    public void showViewInvite(ActionEvent actionEvent) {
        loadFXML(getClass().getResource("/sample/resources/fxml/invite.fxml"));
    }
    @FXML
    public void showViewInvitations(ActionEvent actionEvent) {
        loadFXML(getClass().getResource("/sample/resources/fxml/invitations.fxml"));
    }

    private void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            border_pane.setCenter(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() throws IOException {

    }

    public void showViewChanging(ActionEvent actionEvent) {
        Stage stage = (Stage) v_box.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/change_journal.fxml"));
            stage.setScene(new Scene(root, 500, 500));
            stage.setTitle("FAFIJ Desktop : : Смена журнала");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void exit(ActionEvent actionEvent) {
        Stage stage = (Stage) v_box.getScene().getWindow();
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
