package main.utility;

public class NeighborhoodInfo {
    private final int neighborhoodId;
    private final String neighborhood;
    private final String ward;
    private final int assessedValue;

    /**
     * Constructor for the NeighborhoodInfo
     * Initializes the private instance variables with the provided values.
     *
     * @param neighborhoodId        Account number for property
     * @param neighborhood          Suite number for property
     * @param ward                  House number for property
     * @param assessedValue         Street name of property
     */
    public NeighborhoodInfo(int neighborhoodId, String neighborhood, String ward, int assessedValue) {
        this.neighborhoodId = neighborhoodId;
        this.neighborhood = neighborhood;
        this.ward = ward;
        this.assessedValue = assessedValue;
    }

    public int getNeighborhoodId() {
        return neighborhoodId;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getWard() {
        return ward;
    }

    public int getAssessedValue() {
        return assessedValue;
    }

}