package sample.model;

public class Countries {

    private int countryId;
    private String countryName;

    /**
     *
     * @param countryId
     * @param countryName
     */
    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    //Getters and Setters

    /**
     *
     * @return countryID
     */
    public int getCountryID() {
        return countryId;
    }

    /**
     *
     * @param countryID
     */
    public void setCountryID(int countryID) {
        this.countryId = countryID;
    }

    /**
     *
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     *
     * @param countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
