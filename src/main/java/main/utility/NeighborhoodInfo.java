package main.utility;

/**
 * NeighborhoodInfo holds information regarding neighborhood of the property
 * <p>
 * Author: Fahad Ali
 */
public class NeighborhoodInfo {
    private final int neighborhoodId;
    private final String neighborhood;
    private final String ward;
    private final int assessedValue;

    /**
     * Constructor for NeighborhoodInfo
     * Initializes the private instance variables with the provided values.
     *
     * @param neighborhoodId Neighborhood ID of property
     * @param neighborhood   Neighborhood of property
     * @param ward           Ward of property
     * @param assessedValue  Assessed Value of property
     */
    public NeighborhoodInfo(int neighborhoodId, String neighborhood, String ward, int assessedValue) {
        this.neighborhoodId = neighborhoodId;
        this.neighborhood = neighborhood;
        this.ward = ward;
        this.assessedValue = assessedValue;
    }

    /**
     * Get the unique identifier for the neighborhood of a property.
     *
     * @return The neighborhood ID of property.
     */
    public int getNeighborhoodId() {
        return neighborhoodId;
    }

    /**
     * Get the name of the neighborhood the property is in.
     *
     * @return The name of the neighborhood property is in.
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Get the ward associated with the neighborhood the property is in.
     *
     * @return The ward of the neighborhood the property is in.
     */
    public String getWard() {
        return ward;
    }

    /**
     * Get the assessed value of property in the neighborhood.
     *
     * @return The assessed value of property in the neighborhood.
     */
    public int getAssessedValue() {
        return assessedValue;
    }

}