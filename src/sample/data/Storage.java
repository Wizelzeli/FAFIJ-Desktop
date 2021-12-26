package sample.data;

import java.io.*;

public class Storage {
    private final static String LOGIN = "C:\\Users\\Mi\\Desktop\\FAFIJ_Desktop\\login.txt";
    private final static String JOURNAL = "C:\\Users\\Mi\\Desktop\\FAFIJ_Desktop\\journal.txt";
    private final static String TOKEN = "C:\\Users\\Mi\\Desktop\\FAFIJ_Desktop\\token.txt";

    public static String getLogin() throws IOException{
        File file = new File(LOGIN);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String result = bufferedReader.readLine();
        if (result == null) return "";
        else return result;
    }

    public static String getJournal() throws IOException {
        File file = new File(JOURNAL);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String result = bufferedReader.readLine();
        if (result == null) return "";
        else return result;
    }

    public static String getToken() throws IOException {
        File file = new File(TOKEN);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String result = bufferedReader.readLine();
        if (result == null) return "";
        else return result;
    }

    public static void setLogin(String login) throws IOException {
        FileWriter fileWriter = new FileWriter(LOGIN, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(login);
        writer.close();
    }

    public static void setJournal(String journal) throws IOException {
        FileWriter fileWriter = new FileWriter(JOURNAL, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(journal);
        writer.close();
    }

    public static void setToken(String token) throws IOException {
        FileWriter fileWriter = new FileWriter(TOKEN, false);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(token);
        writer.close();
    }
}
