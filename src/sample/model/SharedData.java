package sample.model;

public class SharedData {
    private static String enteredUsername;

    public static String getEnteredUsername() {
        return enteredUsername;
    }

    public static void setEnteredUsername(String username) {
        enteredUsername = username;
    }
}
