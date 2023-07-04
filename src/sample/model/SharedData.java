package sample.model;

import javafx.collections.ObservableList;
import sample.DAO.UsersDb;

import java.sql.SQLException;

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

    public static int getUserIdFromUsername(String username) throws SQLException {
        ObservableList<Users> userList = UsersDb.getAllUsers();
        int userId = 0;
        for(Users user: userList){
            String listUserName = user.getUserName();
            if(listUserName.equals(username)){
                userId = user.getUserID();
            }
        }
        return userId;
    }
}
