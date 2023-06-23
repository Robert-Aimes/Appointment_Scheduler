package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Contacts;
import sample.model.firstLevelDivisions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class firstLevelDivisionsDb {

    /**
     * Creating observable list for all divisions in the database to have to populate combo boxes
     * @return
     * @throws SQLException
     */

    public static ObservableList<firstLevelDivisions> getAllDivisions() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<firstLevelDivisions> divisionsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM first_level_divisions";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int divisionId = resultSet.getInt("Division_ID");
            String divisionName = resultSet.getString("Division");
            int countryId = resultSet.getInt("Country_ID");

            firstLevelDivisions division = new firstLevelDivisions(
                    divisionId,
                    divisionName,
                    countryId
            );


            divisionsList.add(division);
        }

        return divisionsList;
    }
}
