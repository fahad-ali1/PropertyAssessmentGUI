package main.utility;

/**
 * BuildingInformation holds information regarding building of the property
 * <p>
 * Author: Fahad Ali
 */
public class BuildingInformation {
    private final int accountNum;
    private final int suite;
    private final int houseNum;
    private final String streetName;
    private final String garage;

    /**
     * Constructor for the BuildingInformation
     * Initializes the private instance variables with the provided values.
     *
     * @param accountNum Account number for property
     * @param suite      Suite number for property
     * @param houseNum   House number for property
     * @param streetName Street name of property
     * @param garage     If property has garage or not
     */
    public BuildingInformation(int accountNum, int suite, int houseNum,
                               String streetName, String garage) {
        this.accountNum = accountNum;
        this.suite = suite;
        this.houseNum = houseNum;
        this.streetName = streetName;
        this.garage = garage;
    }

    /**
     * Get the account number of the property.
     *
     * @return The account number.
     */
    public int getAccountNum() {
        return accountNum;
    }

    /**
     * Get the suite number of the property.
     *
     * @return The suite number.
     */
    public int getSuite() {
        return suite;
    }

    /**
     * Get the house number of the property.
     *
     * @return The house number.
     */
    public int getHouseNum() {
        return houseNum;
    }

    /**
     * Get the street name of the property.
     *
     * @return The street name.
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Get the garage value of the property.
     *
     * @return True if garage exists, else False.
     */
    public boolean getGarage() {
        return "Y".equalsIgnoreCase(garage);
    }
}