package main.utility;

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
     * @param accountNum        Account number for property
     * @param suite             Suite number for property
     * @param houseNum          House number for property
     * @param streetName        Street name of property
     * @param garage            If property has garage or not
     */
    public BuildingInformation(int accountNum, int suite, int houseNum,
                               String streetName, String garage) {
        this.accountNum = accountNum;
        this.suite = suite;
        this.houseNum = houseNum;
        this.streetName = streetName;
        this.garage = garage;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public int getSuite(){
        return suite;
    }

    public int getHouseNum(){
        return houseNum;
    }

    public String getStreetName(){
        return streetName;
    }

    public boolean getGarage() {
        return "Y".equalsIgnoreCase(garage);
    }
}