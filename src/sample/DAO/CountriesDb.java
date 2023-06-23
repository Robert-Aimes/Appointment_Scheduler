package sample.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.Countries;
import sample.model.firstLevelDivisions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountriesDb {

    /**
     * Creating observable list for all countries in the database to have to populate combo boxes
     * @return
     * @throws SQLException
     */

    public static ObservableList<Countries> getAllCountries() throws SQLException{
        Connection connection = JDBC.openConnection();
        ObservableList<Countries> countriesList = FXCollections.observableArrayList();
        String query = "SELECT * FROM countries";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            int countryId = resultSet.getInt("Country_ID");
            String countryName = resultSet.getString("Country");

            Countries country = new Countries(
                    countryId,
                    countryName
            );


            countriesList.add(country);
        }

        return countriesList;
    }
}
