package main.dao;

import com.google.gson.annotations.SerializedName;

/**
 * The PropertyAssessmentAPI class represents a standard data structure format for properties retrieved from API
 * <p>
 * Author: Fahad Ali
 */
public class PropertyAssessmentAPI {
    @SerializedName("account_number")
    private String accountNum;

    @SerializedName("suite")
    private String suite;

    @SerializedName("house_number")
    private String houseNum;

    @SerializedName("street_name")
    private String streetName;

    @SerializedName("garage")
    private String garage;

    @SerializedName("neighbourhood_id")
    private String neighbourhoodId;

    @SerializedName("neighbourhood")
    private String neighbourhood;

    @SerializedName("ward")
    private String ward;

    @SerializedName("assessed_value")
    private String assessedValue;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("type")
    private String type;

    @SerializedName("coordinates")
    private double[] coordinates;

    @SerializedName("tax_class_pct_1")
    private String taxClassPct1;

    @SerializedName("mill_class_1")
    private String millClass1;

    @SerializedName("tax_class_pct_2")
    private String taxClassPct2;

    @SerializedName("mill_class_2")
    private String millClass2;

    @SerializedName("tax_class_pct_3")
    private String taxClassPct3;

    @SerializedName("mill_class_3")
    private String millClass3;

    /**
     * Get the account number of the property.
     *
     * @return The account number.
     */
    public String getAccountNumber() {
        return accountNum;
    }

    /**
     * Get the suite number of the property.
     *
     * @return The suite number.
     */
    public String getSuite() {
        return suite;
    }

    /**
     * Get the house number of the property.
     *
     * @return The house number.
     */
    public String getHouseNum() {
        return houseNum;
    }

    /**
     * Get the street number of the property.
     *
     * @return The street number.
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Get the garage of the property.
     *
     * @return The garage.
     */
    public String getGarage() {
        return garage;
    }

    /**
     * Get the neighborhood ID of the property.
     *
     * @return The neighborhood ID.
     */
    public String getNeighbourhoodId() {
        return neighbourhoodId;
    }

    /**
     * Get the neighborhood of the property.
     *
     * @return The neighborhood.
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Get the ward of the property.
     *
     * @return The ward.
     */
    public String getWard() {
        return ward;
    }

    /**
     * Get the assessed value of the property.
     *
     * @return The  assessed value.
     */
    public String getAssessedValue() {
        return assessedValue;
    }

    /**
     * Get the latitude of the property.
     *
     * @return The latitude of property.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Get the longitude of the property.
     *
     * @return The longitude of property.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Get the tax class 1 of the property.
     *
     * @return The tax class 1 of property.
     */
    public String getTaxClassPct1() {
        return taxClassPct1;
    }

    /**
     * Get the mill class 1 of the property.
     *
     * @return The mill class 1 of property.
     */
    public String getMillClass1() {
        return millClass1;
    }

    /**
     * Get the tax class 2 of the property.
     *
     * @return The tax class 2 of property.
     */
    public String getTaxClassPct2() {
        return taxClassPct2;
    }

    /**
     * Get the mill class 2 of the property.
     *
     * @return The mill class 2 of property.
     */
    public String getMillClass2() {
        return millClass2;
    }

    /**
     * Get the tax class 3 of the property.
     *
     * @return The tax class 3 of property.
     */
    public String getTaxClassPct3() {
        return taxClassPct3;
    }

    /**
     * Get the mill class 3 of the property.
     *
     * @return The mill class 3 of property.
     */
    public String getMillClass3() {
        return millClass3;
    }
}