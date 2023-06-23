package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Countries;
import sample.model.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDb {

    /**
     * Creating observable list for all Users in the database to have to populate combo boxes
     * @return
     * @throws SQLException
     */

    public static ObservableList<Users> getAllUsers() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<Users> userList = FXCollections.observableArrayList();
        String query = "SELECT * FROM users";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int userId = resultSet.getInt("User_ID");
            String userName = resultSet.getString("User_Name");
            String userPassword = resultSet.getString("Password");

            Users user = new Users(
                    userId,
                    userName,
                    userPassword
            );


            userList.add(user);
        }

        return userList;
    }
}
