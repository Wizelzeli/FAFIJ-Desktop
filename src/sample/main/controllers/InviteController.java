package sample.main.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import sample.data.Storage;
import sample.data.postbodies.JournalLoginRoleAdmin;
import sample.data.postbodies.LoginJournal;
import sample.main.tasks.AcceptInviteTask;
import sample.main.tasks.InviteTask;

import java.io.IOException;

public class InviteController {
    @FXML
    public Button invite_button;
    @FXML
    public TextField login_edittext;
    @FXML
    public Group radio_group;
    @FXML
    public RadioButton radio_adult;
    @FXML
    public RadioButton radio_kid;
    @FXML
    public Label message;

    @FXML
    public void inviteUser(ActionEvent actionEvent) throws IOException {
        if (login_edittext.getText().equals("")) {
            message.setText("Введите логин");
            return;
        }
        String role = "";
        if (radio_adult.isSelected()) role = "ADULT";
        if (radio_kid.isSelected()) role = "KID";
        if (role.equals("")) {
            message.setText("Выберете роль");
            return;
        }
        JournalLoginRoleAdmin journalLoginRoleAdmin = new JournalLoginRoleAdmin(Storage.getJournal(), login_edittext.getText(), role, Storage.getLogin());
        InviteTask inviteTask = new InviteTask(journalLoginRoleAdmin);
        inviteTask.setOnSucceeded(e-> message(inviteTask.getValue().statusCode()));
        new Thread(inviteTask).start();
    }

    public void message(int code) {
        String message = switch (code) {
            case 404 -> "Пользователь не найден";
            case 406 -> "Недостаточно прав";
            case 409 -> "Пользователь уже использует этот журнал";
            case 201 -> "Приглашение отправлено";
            case 303 -> "Пользователь уже был приглашён";
            default -> "Неизвестная ошибка";
        };
        this.message.setText(message);
    }
}
