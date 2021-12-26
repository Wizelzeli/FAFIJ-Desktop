package sample.main.controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.data.Storage;
import sample.data.postbodies.LoginPass;
import sample.main.tasks.LoginTask;
import sample.data.otherdata.TokenCatch;
import sample.main.tasks.RegistrationTask;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class LoginController {
    @FXML
    public TextField login_edittext_login;
    @FXML
    public PasswordField login_edittext_password;
    @FXML
    public Button login_button;
    @FXML
    public Button registration_button;
    @FXML
    public Label error_message;

    @FXML
    public void loginClick(ActionEvent actionEvent) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String login = login_edittext_login.getText();
        String password = login_edittext_password.getText();
        if (login.equals("") || password.equals("")) {
            error_message.setText("Введите полные данные");
            return;
        }
        LoginPass postLoginPass = new LoginPass(login, getHash(password));
        LoginTask loginTask = new LoginTask(postLoginPass);
        loginTask.setOnSucceeded(e-> {
            if (loginTask.getValue().statusCode() >= 300) showMessage(loginTask.getValue().statusCode());
            else {
                TokenCatch token = new Gson().fromJson(loginTask.getValue().body(), TokenCatch.class);
                System.out.println(token.getJwtToken());
                try {
                    Storage.setLogin(login);
                    Storage.setToken(token.getJwtToken());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Stage stage = (Stage) login_button.getScene().getWindow();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/fxml/change_journal.fxml"));
                    stage.setScene(new Scene(root, 500, 500));
                    stage.setTitle("FAFIJ Desktop : : Смена журнала");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        new Thread(loginTask).start();
    }

    @FXML
    public void registrationClick(ActionEvent actionEvent) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String login = login_edittext_login.getText();
        String password = login_edittext_password.getText();
        if (login.equals("") || password.equals("")) {
            error_message.setText("Введите полные данные");
            return;
        }
        if (login.contains(" ") || password.contains(" ")) {
            error_message.setText("Логин и пароль не могут содержать пробелы");
            return;
        }
        if (password.length() <= 6) {
            error_message.setText("Длина пароля должна быть больше 6 символов");
            return;
        }
        LoginPass postRegIn = new LoginPass(login, getHash(password));
        RegistrationTask registrationTask = new RegistrationTask(postRegIn);
        registrationTask.setOnSucceeded(e-> showMessage(registrationTask.getValue().statusCode()));
        new Thread(registrationTask).start();
    }

    public void showMessage(int code) {
        String message = "Неизвестная ошибка";
        if (code == 401) message = "Неверное имя пользователя или пароль";
        if (code == 201) message = "Аккаунт создан";
        if (code == 500) message = "Пользователь уже существует";
        error_message.setText(message);
    }

    public static String getHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 512, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        BigInteger bi = new BigInteger(1, hash);
        String hex = bi.toString(16);
        int paddingLength = (hash.length * 2) - hex.length();
        if(paddingLength > 0) return String.format("%0"  +paddingLength + "d", 0) + hex;
        else return hex;
    }
}
