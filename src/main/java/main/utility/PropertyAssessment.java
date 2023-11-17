package main.utility;

import java.text.NumberFormat;
import java.util.Objects;

/**
 * PropertyAssessment represents a property assessment, contains information
 * such as building details, neighborhood information, location, and assessment class.
 * <p>
 * Author: Fahad Ali
 */
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
     * @param buildingInfo     Building information class
     * @param neighborhoodInfo Neighborhood information class
     * @param location         Location information class
     * @param assessmentClass  Assessment information class
     */
    public PropertyAssessment(BuildingInformation buildingInfo,
                              NeighborhoodInfo neighborhoodInfo,
                              Location location, AssessmentClass assessmentClass) {
        this.buildingInfo = buildingInfo; // account num, suite, house number, street name, garage
        this.neighborhoodInfo = neighborhoodInfo; // neighborhoodID, neighborhood, ward, assessed, value
        this.location = location; // point location tuple
        this.assessmentClass = assessmentClass; // assessment class 1,2,3 and their percents
    }

    /**
     * Get the building information.
     *
     * @return The BuildingInformation object.
     */
    public BuildingInformation getBuildingInfo() {
        return buildingInfo;
    }

    /**
     * Get the neighborhood information.
     *
     * @return The NeighborhoodInfo object.
     */
    public NeighborhoodInfo getNeighborhoodInfo() {
        return neighborhoodInfo;
    }

    /**
     * Get the location information.
     *
     * @return The Location object.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the assessment class information.
     *
     * @return The AssessmentClass object.
     */
    public AssessmentClass getAssessmentClass() {
        return assessmentClass;
    }

    /**
     * Check if this PropertyAssessment is equal to another PropertyAssessment.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyAssessment that = (PropertyAssessment) o;
        return Objects.equals(buildingInfo.getAccountNum(), that.buildingInfo.getAccountNum());
    }

    /**
     * Generate a hash code for this PropertyAssessment.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(buildingInfo.getAccountNum());
    }

    /**
     * Compare this PropertyAssessment to another PropertyAssessment based on assessed values.
     *
     * @param o The PropertyAssessment to compare.
     * @return 0 if assessed values are equal, 1 if greater, -1 if smaller.
     */
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

    /**
     * Convert this PropertyAssessment to a string representation.
     *
     * @return A formatted string representation of the PropertyAssessment.
     */
    @Override
    public String toString() {
        // format to currency
        currencyFormatter.setMaximumFractionDigits(0);

        // deal with 0 and 0.0 that are returned during parsing form ProcessData.java (if the cell is blank)
        String suiteValue = buildingInfo.getSuite() == 0 ? "" : String.valueOf(buildingInfo.getSuite());
        String assessmentClass1PercentStr = formatPercentage(assessmentClass.getAssessment1Percent());
        String assessmentClass2PercentStr = formatPercentage(assessmentClass.getAssessment2Percent());
        String assessmentClass3PercentStr = formatPercentage(assessmentClass.getAssessment3Percent());

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

    /**
     * Format a percentage value to a string.
     *
     * @param percentage The value to format.
     * @return Formatted percentage as a string.
     */
    public String formatPercentage(double percentage) {
        if (percentage == 0.0) {
            return "";
        }
        return String.format("%.0f%%", percentage);
    }
}