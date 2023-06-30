package sample.model;

public class firstLevelDivisions {
    private int divisionId;
    private String divisionName;
    public int countryId;

    /**
     *Constructor for firstLevelDivisions
     * @param divisionId
     * @param countryId
     * @param divisionName
     */
    public firstLevelDivisions(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    //Getters and Setters

    /**
     *
     * @return divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     *
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     *
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     *
     * @param divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     *
     * @return countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     *
     * @param countryId
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
