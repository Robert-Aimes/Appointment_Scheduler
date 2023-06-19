package sample.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.DAO.JDBC;
import sample.model.Customer;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class CustomerDb {

    private static Connection connection;

    /**
     * Creating observable list for all customers in the database to have to populate the tableview.
     * @return
     * @throws SQLException
     */

    public static ObservableList<Customer> getAllCustomers() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String query = "SELECT * FROM customers INNER JOIN first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int custId = resultSet.getInt("Customer_ID");
            String custName = resultSet.getString("Customer_Name");
            String custAddress = resultSet.getString("Address");
            String custPostalCode = resultSet.getString("Postal_Code");
            String custPhone = resultSet.getString("Phone");
            LocalDateTime custCreateDate = resultSet.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = resultSet.getString("Created_By");
            LocalDateTime custLastUpdate = resultSet.getTimestamp("Last_Update").toLocalDateTime();
            String lastUpdatedBy = resultSet.getString("Last_Updated_By");
            int custDivisionId = resultSet.getInt("Division_ID");
            String custDivisionName = resultSet.getString("Division");

            Customer customer = new Customer(
                    custId,
                    custName,
                    custAddress,
                    custPostalCode,
                    custPhone,
                    custCreateDate,
                    createdBy,
                    custLastUpdate,
                    lastUpdatedBy,
                    custDivisionId,
                    custDivisionName
            );

            customerList.add(customer);
        }

        return customerList;
    }

    public void deleteCustomer(int custId) throws SQLException{
        String query = "DELETE FROM customers WHERE Customer_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, custId);
        ps.executeUpdate();
        ps.close();
    }
}
