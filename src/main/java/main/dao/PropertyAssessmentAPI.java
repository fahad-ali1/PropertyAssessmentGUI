package main.dao;

import com.google.gson.annotations.SerializedName;

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

    public String getAccountNumber() {
        return accountNum;
    }

    public String getSuite() {
        return suite;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getGarage() {
        return garage;
    }

    public String getNeighbourhoodId() {
        return neighbourhoodId;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getWard() {
        return ward;
    }

    public String getAssessedValue() {
        return assessedValue;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public String getTaxClassPct1() {
        return taxClassPct1;
    }

    public String getMillClass1() {
        return millClass1;
    }

    public String getTaxClassPct2() {
        return taxClassPct2;
    }

    public String getMillClass2() {
        return millClass2;
    }

    public String getTaxClassPct3() {
        return taxClassPct3;
    }

    public String getMillClass3() {
        return millClass3;
    }


}
