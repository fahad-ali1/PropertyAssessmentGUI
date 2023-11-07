package main.utility;

public class Location {
    private final String lat;
    private final String lon;

    /**
     * Constructor for the BuildingInformation
     * Initializes the private instance variables with the provided values.
     *
     * @param lat        Latitude of property
     * @param lon        Longitude of property
     */
    public Location(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public String getPointLocation() {
        return String.format("(%s, %s)", lat, lon);
    }
}
