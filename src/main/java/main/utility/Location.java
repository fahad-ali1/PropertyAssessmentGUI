package main.utility;

/**
 * Location represents the geographic coordinates (latitude and longitude) of a single property.
 * <p>
 * Author: Fahad Ali
 */
public class Location {
    private final String lat;
    private final String lon;

    /**
     * Constructor for the BuildingInformation
     * Initializes the variables with the provided values.
     *
     * @param lat Latitude of property
     * @param lon Longitude of property
     */
    public Location(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Get the point location as a formatted string, latitude and longitude.
     *
     * @return A  string representing the point location with the format "(latitude, longitude)".
     */
    public String getPointLocation() {
        return String.format("(%s, %s)", lat, lon);
    }
}
