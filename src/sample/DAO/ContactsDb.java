package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdk.internal.jimage.ImageStrings;
import sample.model.Appointment;
import sample.model.Contacts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ContactsDb {

    /**
     * Creating observable list for all contacts in the database to have to populate combo boxes
     * @return
     * @throws SQLException
     */

    public static ObservableList<Contacts> getAllContacts() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();
        String query = "SELECT * FROM contacts";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int contactId = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            String contactEmail = resultSet.getString("Email");

            Contacts contacts = new Contacts(
                    contactId,
                    contactName,
                    contactEmail
            );


            contactList.add(contacts);
        }

        return contactList;
    }
}
