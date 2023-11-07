package main.dao;

import com.google.gson.Gson;
import main.PropertyAssessment;
import main.PropertyAssessments;
import main.processData.ProcessData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiPropertyAssessmentDAO implements PropertyAssessmentDAO {
    private final String apiUrl;
    private final ProcessData processData;
    private final PropertyAssessments propertyAssessments;

    public ApiPropertyAssessmentDAO() {
        this.processData = new ProcessData();
        this.propertyAssessments = new PropertyAssessments();
        this.apiUrl = "https://data.edmonton.ca/resource/q7d6-ambg.json";
    }

    private String buildQuery(String name, String value) {
        return apiUrl + "?" + name + "=" + value;
    }

    private void getQuery(String query) {
        HttpClient client = HttpClient.newHttpClient();
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

    private void parseJson(String jsonString) {
        Gson gson = new Gson();
        PropertyAssessmentAPI[] propertyAssessmentArray = gson.fromJson(jsonString, PropertyAssessmentAPI[].class);
        String[] formatJSON = new String[18];

        for (PropertyAssessmentAPI propertyAPI : propertyAssessmentArray) {
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

    @Override
    public PropertyAssessment getByAccountNumber(String accountNumber) {
        String query = buildQuery("account_number", accountNumber.toUpperCase());
        getQuery(query);
        return propertyAssessments.getPropertyByAccountNum(Integer.parseInt(accountNumber));
    }

    @Override
    public PropertyAssessments getByNeighbourhood(String neighbourhood) {
        String query = buildQuery("neighbourhood", neighbourhood.toUpperCase());
        getQuery(query);
        // set second parameter to propertyAssessments that are populated from the API
        return processData.filterByNeighborhood(neighbourhood, propertyAssessments);
    }

    @Override
    public PropertyAssessments getByAssessmentClass(String assessmentClass) {
        String query1 = buildQuery("mill_class_1", assessmentClass.toUpperCase());
        String query2 = buildQuery("mill_class_2", assessmentClass.toUpperCase());
        String query3 = buildQuery("mill_class_3", assessmentClass.toUpperCase());
        getQuery(query1);
        getQuery(query2);
        getQuery(query3);
        return processData.filterByAssessment(assessmentClass, propertyAssessments);
    }

    @Override
    public List<PropertyAssessment> getAll() {
        getQuery(apiUrl);
        return propertyAssessments.getPropertyAssessmentList();
    }
}