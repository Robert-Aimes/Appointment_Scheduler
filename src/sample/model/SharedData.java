package sample.model;

/**
 * Model class created to store the username of the user who logs in to the application. The stored username will be used when creating/updating appointments and customers.
 */
public class SharedData {
    private static String enteredUsername;

    /**
     * Getter
     * @return enteredUsername
     */
    public static String getEnteredUsername() {
        return enteredUsername;
    }

    /**
     * Setter
     * @param username
     */
    public static void setEnteredUsername(String username) {
        enteredUsername = username;
    }
}
