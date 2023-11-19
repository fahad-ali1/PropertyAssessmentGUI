package main.dao;

import com.google.gson.Gson;
import main.utility.PropertyAssessment;
import main.utility.PropertyAssessments;
import main.processData.ProcessData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * The ApiPropertyAssessmentDAO class implements the PropertyAssessmentDAO interface and provides access to property
 * assessment data in the API URL.
 * <p>
 * Author: Fahad Ali
 */
public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static String apiUrl = "https://data.edmonton.ca/resource/q7d6-ambg.json";

    private final ProcessData processData = new ProcessData();
    private final PropertyAssessments propertyAssessments = new PropertyAssessments();

    /**
     * Constructs an ApiPropertyAssessmentDAO.
     */
    public ApiPropertyAssessmentDAO() {
        apiUrl = "https://data.edmonton.ca/resource/q7d6-ambg.json";
    }

    /**
     * Sends a GET request to the specified query URL and parses the JSON response to a standard formatting.
     *
     * @param query The query URL.
     */
    private void getQuery(String query) {
        System.out.println(query);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            parseJson(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse JSON data into PropertyAssessment objects and adds them to propertyAssessments..
     *
     * @param jsonString The JSON data to parse.
     */
    private void parseJson(String jsonString) {
        Gson gson = new Gson();
        PropertyAssessmentAPI[] propertyAssessmentArray = gson.fromJson(jsonString, PropertyAssessmentAPI[].class);
        String[] formatJSON = new String[18];

        for (PropertyAssessmentAPI propertyAPI : propertyAssessmentArray) {
            if (propertyAssessments.getPropertyByAccountNum(Integer.parseInt(propertyAPI.getAccountNumber())) == null) {
                formatJSON[0] = propertyAPI.getAccountNumber();
                formatJSON[1] = propertyAPI.getSuite();
                formatJSON[2] = propertyAPI.getHouseNum();
                formatJSON[3] = propertyAPI.getStreetName();
                formatJSON[4] = propertyAPI.getGarage();
                formatJSON[5] = propertyAPI.getNeighbourhoodId();
                formatJSON[6] = propertyAPI.getNeighbourhood();
                formatJSON[7] = propertyAPI.getWard();
                formatJSON[8] = propertyAPI.getAssessedValue();
                formatJSON[9] = propertyAPI.getLatitude();
                formatJSON[10] = propertyAPI.getLongitude();
                formatJSON[11] = String.format("POINT (%s %s)", propertyAPI.getLongitude(), propertyAPI.getLatitude());
                formatJSON[12] = propertyAPI.getTaxClassPct1();
                formatJSON[13] = propertyAPI.getTaxClassPct2();
                formatJSON[14] = propertyAPI.getTaxClassPct3();
                formatJSON[15] = propertyAPI.getMillClass1();
                formatJSON[16] = propertyAPI.getMillClass2();
                formatJSON[17] = propertyAPI.getMillClass3();
                for (int i = 0; i < formatJSON.length; i++) {
                    if (formatJSON[i] == null) {
                        formatJSON[i] = "";
                    }
                }
                PropertyAssessment property = processData.getPropertyAssessment(formatJSON);
                propertyAssessments.addList(property);
                propertyAssessments.hashProperty(property);
            }
        }
    }

    /**
     * Retrieves a PropertyAssessment by the account number.
     *
     * @param accountNumber The account number of the property.
     * @return The PropertyAssessment with the account number.
     */
    @Override
    public PropertyAssessment getByAccountNumber(String accountNumber) {
        String accountNumQuery = apiUrl + "?$where=account_number=%27" + accountNumber + "%27";
        getQuery(accountNumQuery);
        return propertyAssessments.getPropertyByAccountNum(Integer.parseInt(accountNumber));
    }

    /**
     * Retrieves PropertyAssessment(s) by the specified address, can be singled by suite, house number, or street number,
     * or all three.
     *
     * @param input The address (or part of address) of the property.
     * @return The PropertyAssessment with the specified address.
     */
    public PropertyAssessments getByAddress(String input) {
        String address = input.replace(" ", "%20");
        String addressQuery = apiUrl +
                "?$where=suite%20LIKE%20%27" +
                address.toUpperCase() +
                "%25%27%20OR%20street_name%20LIKE%20%27" +
                address.toUpperCase() +
                "%25%27%20OR%20house_number%20LIKE%20%27" +
                address.toUpperCase() +
                "%25%27";
        getQuery(addressQuery);
        return processData.filterByAddress(input, propertyAssessments);
    }

    /**
     * Retrieves PropertyAssessments by the specified neighbourhood.
     *
     * @param neighbourhood The neighborhood of the properties.
     * @return The PropertyAssessments with the specified neighborhood.
     */
    @Override
    public PropertyAssessments getByNeighbourhood(String neighbourhood) {
        String neighbourhoodQuery = apiUrl + "?$where=neighbourhood%20LIKE%20%27" + neighbourhood.toUpperCase() + "%25%27";
        getQuery(neighbourhoodQuery);
        return processData.filterByNeighborhood(neighbourhood, propertyAssessments);
    }

    /**
     * Retrieves PropertyAssessments by the specified assessmentClass.
     *
     * @param input The assessmentClass of the properties.
     * @return The PropertyAssessments with the specified assessmentClass.
     */
    public PropertyAssessments getByAssessmentClass(String input) {
        String assessmentClass = input.replace(" ", "%20");
        String assessmentClassQuery = apiUrl +
                "?$where=mill_class_1=%27" +
                assessmentClass.toUpperCase() +
                "%27%20OR%20mill_class_2=%27" +
                assessmentClass.toUpperCase() +
                "%27%20OR%20mill_class_3=%27" +
                assessmentClass.toUpperCase() +
                "%27";
        getQuery(assessmentClassQuery);
        return processData.filterByAssessment(input, propertyAssessments);
    }

    /**
     * Retrieves PropertyAssessments by the specified filters.
     *
     * @param accountNumber   The account number of the property.
     * @param neighbourhood   The neighbourhood of the properties.
     * @param assessmentClass The assessment class of the properties.
     * @param address         The address for properties.
     * @param min             The minimum assessed value for filtering properties.
     * @param max             The maximum assessed value for filtering properties.
     * @return The PropertyAssessments that match the specified filters.
     */
    @Override
    public PropertyAssessments multipleFilter(String accountNumber, String neighbourhood, String assessmentClass,
                                              String address, String min, String max) {
        int minValue = Integer.parseInt(min);
        int maxValue = Integer.parseInt(max);

        StringBuilder finalQuery = new StringBuilder(apiUrl + "?$where=");

        if (accountNumber != null) {
            finalQuery.append("account_number=%27")
                    .append(accountNumber)
                    .append("%27");
        }

        if (neighbourhood != null) {
            String neighbourhoodInput = neighbourhood.toUpperCase();
            finalQuery.append("%20OR%20neighbourhood%20LIKE%20%27%25")
                    .append(neighbourhoodInput)
                    .append("%25%27");
        }

        if (assessmentClass != null) {
            finalQuery.append("%20AND%20(mill_class_1%20LIKE%20%27")
                    .append(assessmentClass.toUpperCase())
                    .append("%27%20OR%20mill_class_2%20LIKE%20%27")
                    .append(assessmentClass.toUpperCase())
                    .append("%27%20OR%20mill_class_3%20LIKE%20%27")
                    .append(assessmentClass.toUpperCase())
                    .append("%27)");
        }

        if (address != null) {
            String addressInput = address.replace(" ", "%20");
            finalQuery.append("%20AND%20(suite%20LIKE%20%27%25")
                    .append(addressInput.toUpperCase())
                    .append("%25%27%20OR%20street_name%20LIKE%20%27%25")
                    .append(addressInput.toUpperCase())
                    .append("%25%27%20OR%20house_number%20LIKE%20%27%25")
                    .append(addressInput.toUpperCase())
                    .append("%25%27)");
        }

        if (minValue > 0 || maxValue < 999999999) {
            finalQuery.append("%20AND%20(assessed_value%20%3E%20%27")
                    .append(minValue).append("%27%20AND%20assessed_value%20%3C%20%27")
                    .append(maxValue).append("%27)");
        }
        getQuery(String.valueOf(finalQuery));
        return processData.filters(accountNumber, neighbourhood, assessmentClass, address,
                minValue, maxValue, propertyAssessments);
    }

    //https://data.edmonton.ca/resource/q7d6-ambg.json?$where=account_number='' OR neighbourhood='OLIVER' AND (mill_class_1='COMMERCIAL' OR mill_class_2='COMMERCIAL' OR mill_class_3='COMMERCIAL') AND (suite LIKE '104' OR street_name LIKE '104' OR house_number LIKE '104') AND (assessed_value > '0' AND assessed_value < '30000000')
    //https://data.edmonton.ca/resource/q7d6-ambg.json?$where=account_number=%27%27%20OR%20neighbourhood=%27OLIVER%27%20AND%20(mill_class_1=%27COMMERCIAL%27%20OR%20mill_class_2=%27COMMERCIAL%27%20OR%20mill_class_3=%27COMMERCIAL%27)%20AND%20(suite%20LIKE%20%27104%27%20OR%20street_name%20LIKE%20%27104%27%20OR%20house_number%20LIKE%20%27104%27)%20AND%20(assessed_value%20%3E%20%270%27%20AND%20assessed_value%20%3C%20%2730000000%27)

    /**
     * Retrieves a list of all PropertyAssessments from the API (1000 calls).
     *
     * @return List of all PropertyAssessments available in the API (1000).
     */
    @Override
    public List<PropertyAssessment> getAll() {
        getQuery(apiUrl);
        return propertyAssessments.getPropertyAssessmentList();
    }
}