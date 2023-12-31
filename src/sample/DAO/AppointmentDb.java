package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Appointment;
import sample.DAO.JDBC;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class AppointmentDb {

    private static Connection connection;


    /**
     * Creating observable list for all appointments in the database to have to populate the tableview.
     * @return
     * @throws SQLException
     */

    public static ObservableList<Appointment> getAllAppointments() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        String query = "SELECT * FROM appointments";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int apptId = resultSet.getInt("Appointment_ID");
            String apptTitle = resultSet.getString("Title");
            String apptDescription = resultSet.getString("Description");
            String apptLocation = resultSet.getString("Location");
            String apptType = resultSet.getString("Type");
            LocalDateTime apptStartTime = resultSet.getTimestamp("Start").toLocalDateTime();
            LocalDateTime apptEndTime = resultSet.getTimestamp("End").toLocalDateTime();
            LocalDateTime apptCreateDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String apptCreatedBy = resultSet.getString("Created_By");
            LocalDateTime apptLastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String apptLastUpdatedBy = resultSet.getString("Last_Updated_By");
            int customerId = resultSet.getInt("Customer_ID");
            int userId = resultSet.getInt("User_ID");
            int contactId = resultSet.getInt("Contact_ID");

            Appointment appointment = new Appointment(
                    apptId,
                    apptTitle,
                    apptDescription,
                    apptLocation,
                    apptType,
                    apptStartTime,
                    apptEndTime,
                    apptCreateDate,
                    apptCreatedBy,
                    apptLastUpdate,
                    apptLastUpdatedBy,
                    customerId,
                    userId,
                    contactId
            );

            appointmentList.add(appointment);
        }

        return appointmentList;
    }

    /**
     * Method to delete an appointment from the DB table based on appointment id
     * @param apptId
     * @throws SQLException
     */
    public static void deleteAppointment(int apptId) throws SQLException{
        Connection connection = JDBC.openConnection();
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, apptId);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Appointment deleted successfully.");
        } else {
            System.out.println("No appointment found with the given ID.");
        }


    }


}
