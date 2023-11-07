package main;

import main.utility.AssessmentClass;
import main.utility.BuildingInformation;
import main.utility.Location;
import main.utility.NeighborhoodInfo;

import java.text.NumberFormat;
import java.util.Objects;

public class PropertyAssessment implements Comparable<PropertyAssessment> {
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
    private final BuildingInformation buildingInfo;
    private final NeighborhoodInfo neighborhoodInfo;
    private final Location location;
    private final AssessmentClass assessmentClass;

    /**
     * Constructor for the PropertyAssessment
     * Initializes the private instance variables with the provided values.
     * This class combines all the classes into one singular property.
     *
     * @param buildingInfo          Building information class
     * @param neighborhoodInfo      Neighborhood information class
     * @param location              Location information class
     * @param assessmentClass       Assessment information class
     */
    public PropertyAssessment(BuildingInformation buildingInfo,
                              NeighborhoodInfo neighborhoodInfo,
                              Location location, AssessmentClass assessmentClass){
        this.buildingInfo = buildingInfo; // account num, suite, house number, street name, garage
        this.neighborhoodInfo = neighborhoodInfo; // neighborhoodID, neighborhood, ward, assessed, value
        this.location = location; // point location tuple
        this.assessmentClass = assessmentClass; // assessment class 1,2,3 and their percents
    }

    public BuildingInformation getBuildingInfo() {
        return buildingInfo;
    }

    public NeighborhoodInfo getNeighborhoodInfo() {
        return neighborhoodInfo;
    }

    public Location getLocation() {
        return location;
    }

    public AssessmentClass getAssessmentClass() {
        return assessmentClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return Objects.equals(buildingInfo.getAccountNum(), that.buildingInfo.getAccountNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(buildingInfo.getAccountNum());
    }

    @Override
    public int compareTo(PropertyAssessment o) {
        if (this.neighborhoodInfo.getAssessedValue() == o.neighborhoodInfo.getAssessedValue()) {
            return 0;
        } else if (this.neighborhoodInfo.getAssessedValue() > o.neighborhoodInfo.getAssessedValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        // format to currency
        currencyFormatter.setMaximumFractionDigits(0);

        // deal with 0 and 0.0 that are returned during parsing form ReadFile.java (if the cell is blank)
        String suiteValue = buildingInfo.getSuite() == 0 ? "" : String.valueOf(buildingInfo.getSuite());
        String assessmentClass1PercentStr = formatPercentage(assessmentClass.getAssessment1Percent());
        String assessmentClass2PercentStr = formatPercentage(assessmentClass.getAssessment2Percent());
        String assessmentClass3PercentStr = formatPercentage(assessmentClass.getAssessment3Percent());

        // todo: fix space that is left if assessment class is empty
        return String.format("Account Number = %s\n" +
                        "Address =  %s %s %s\n" +
                        "Assessed Value = %s\n" +
                        "Assessment Class = [%s %s, %s %s, %s %s]\n" +
                        "Neighborhood = %s (%s)\n" +
                        "Location = %s",
                buildingInfo.getAccountNum(),
                suiteValue, buildingInfo.getHouseNum(), buildingInfo.getStreetName(),
                currencyFormatter.format(neighborhoodInfo.getAssessedValue()),
                assessmentClass.getAssessmentClass1(), assessmentClass1PercentStr,
                assessmentClass.getAssessmentClass2(), assessmentClass2PercentStr,
                assessmentClass.getAssessmentClass3(), assessmentClass3PercentStr,
                neighborhoodInfo.getNeighborhood(), neighborhoodInfo.getWard(),
                location.getPointLocation());
    }

    public String formatPercentage(double percentage) {
        if (percentage == 0.0) {
            return "";
        }
        return String.format("%.0f%%", percentage);
    }
}