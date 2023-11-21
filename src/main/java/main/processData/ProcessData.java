package main.processData;

import main.utility.PropertyAssessment;
import main.utility.PropertyAssessments;
import main.utility.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ProcessData class is responsible for processing a CSV file containing property assessment data.
 * It provides methods to parse and manipulate the data, apply filters, and retrieve information about properties.
 * It can also retrieve information from any input that follows the defined formatting of the CSV.
 * <p>
 * Author: Fahad Ali
 */
public class ProcessData {
    private final String csvFileName;
    private final PropertyAssessments propertyAssessments;

    /**
     * Constructor for the ProcessFile class
     *
     * @param csvFileName a CSV file with the proper formatting
     */
    public ProcessData(String csvFileName) {
        this.csvFileName = csvFileName;
        this.propertyAssessments = new PropertyAssessments(new ArrayList<>()); // create a PropertyAssessments object
    }

    /**
     * Constructor for ProcessFile class without a filename.
     * Initializes without a filename.
     */
    public ProcessData() {
        this.csvFileName = null;
        this.propertyAssessments = new PropertyAssessments(new ArrayList<>());
    }

    /**
     * Process the CSV file, parsing each line and creating PropertyAssessment objects.
     * Skips the first line in the CSV as it contains titles.
     */
    public void processFile() {
        try {
            assert csvFileName != null;
            try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFileName))) {
                fileReader.readLine(); // Skip the first line in CSV as they are titles
                String line;

                while ((line = fileReader.readLine()) != null) { // read until end of file
                    String[] cell = line.split(",", Constants.NUM_COLUMNS);
                    PropertyAssessment property = getPropertyAssessment(cell);
                    addAssessment(property); // add parsed property to PropertyAssessments object
                }
            }
        } catch (IOException e) {
            handleFileOpenError(); // handle error
        }
    }

    /**
     * Parses a CSV row and creates a PropertyAssessment object.
     *
     * @param cell an array of strings representing the CSV row
     * @return a PropertyAssessment object
     */
    public PropertyAssessment getPropertyAssessment(String[] cell) {
        // this method creates a single property object by properly parsing and creating "sub" objects
        int accountNum = parseInt(cell[Constants.ACCOUNT_NUM_INDEX]);
        int suite = parseInt(cell[Constants.SUITE_INDEX]);
        int houseNum = parseInt(cell[Constants.HOUSE_NUM_INDEX]);
        int neighborhoodID = parseInt(cell[Constants.NEIGHBORHOOD_ID_INDEX]);
        int assessedValue = parseInt(cell[Constants.ASSESSED_VALUE_INDEX]);
        double assessmentClass1Percent = parseDouble(cell[Constants.ASSESSMENT_CLASS1_PERCENT_INDEX]);
        double assessmentClass2Percent = parseDouble(cell[Constants.ASSESSMENT_CLASS2_PERCENT_INDEX]);
        double assessmentClass3Percent = parseDouble(cell[Constants.ASSESSMENT_CLASS3_PERCENT_INDEX]);

        BuildingInformation buildingInfo = new BuildingInformation(accountNum, suite, houseNum,
                cell[Constants.STREET_NAME_INDEX], cell[Constants.GARAGE_INDEX]);

        NeighborhoodInfo neighborhoodInfo = new NeighborhoodInfo(neighborhoodID,
                cell[Constants.NEIGHBORHOOD_NAME_INDEX], cell[Constants.WARD_INDEX], assessedValue);

        Location location = new Location(cell[Constants.LATITUDE_INDEX], cell[Constants.LONGITUDE_INDEX]);

        AssessmentClass assessmentClass = new AssessmentClass(assessmentClass1Percent, assessmentClass2Percent,
                assessmentClass3Percent, cell[Constants.ASSESSMENT_CLASS1_INDEX],
                cell[Constants.ASSESSMENT_CLASS2_INDEX], cell[Constants.ASSESSMENT_CLASS3_INDEX]);

        return new PropertyAssessment(buildingInfo, neighborhoodInfo, location, assessmentClass);
    }

    /**
     * Parse a string into an integer and handle errors.
     *
     * @param value a string to be parsed
     * @return the parsed integer
     */
    private static int parseInt(String value) {
        // this method turns the string into an int and handles errors
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Parse a string into a double and handle errors.
     *
     * @param value a string to be parsed
     * @return the parsed double
     */
    private static double parseDouble(String value) {
        // this method turns the string into a double and handles errors
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Handle the error when opening a file.
     */
    private void handleFileOpenError() {
        // this method simplifies error handling for a file
        System.err.println("Error: Can't open file " + csvFileName);
    }

    /**
     * Add a PropertyAssessment object to the PropertyAssessments list of PropertyAssessment objects.
     *
     * @param property the PropertyAssessment to be added
     */
    private void addAssessment(PropertyAssessment property) {
        // this method will hash and add a property object to the PropertyAssessments instance
        propertyAssessments.hashProperty(property);
        propertyAssessments.addList(property);
    }

    /**
     * Check if the account number of a PropertyAssessment finds the provided account number.
     *
     * @param property   the PropertyAssessment to check
     * @param accountNum the account number to compare with
     * @return true if the account number matches or if the provided account number is null. Else, false
     */
    private boolean findAccountNum(PropertyAssessment property, String accountNum) {
        return accountNum == null || String.valueOf(property.getBuildingInfo().getAccountNum()).contains(accountNum);
    }

    /**
     * Check if the neighborhood of a PropertyAssessment finds the provided neighborhood.
     *
     * @param property     the PropertyAssessment to check
     * @param neighborhood the neighborhood to compare with
     * @return true if the neighborhood matches or if the provided neighborhood is null. Else, false
     */
    private boolean findNeighborhood(PropertyAssessment property, String neighborhood) {
        return neighborhood == null || property.getNeighborhoodInfo().getNeighborhood().toLowerCase()
                .contains(neighborhood.toLowerCase());
    }

    /**
     * Check if the assessment class of a PropertyAssessment finds the provided assessment class.
     *
     * @param property        the PropertyAssessment to check
     * @param assessmentClass the assessment class to compare with
     * @return true if the assessment class matches or if the provided assessment class is null.
     * Else, false
     */
    private boolean findAssessmentClass(PropertyAssessment property, String assessmentClass) {
        return assessmentClass == null ||
                property.getAssessmentClass().getAssessmentClass1().equalsIgnoreCase(assessmentClass) ||
                property.getAssessmentClass().getAssessmentClass2().equalsIgnoreCase(assessmentClass) ||
                property.getAssessmentClass().getAssessmentClass3().equalsIgnoreCase(assessmentClass);
    }

    /**
     * Check if the address of a PropertyAssessment finds the provided address.
     *
     * @param property the PropertyAssessment to check
     * @param address  the address to compare with
     * @return true if the address matches or if the provided address is null. Else, false
     */
    private boolean findAddress(PropertyAssessment property, String address) {
        String concatenatedAddress = (((property.getBuildingInfo().getHouseNum() + " " +
                property.getBuildingInfo().getSuite()).toUpperCase() + " " +
                property.getBuildingInfo().getStreetName()).toUpperCase().trim().replace(" ", ""));

        return address == null ||
                concatenatedAddress.contains(address.toUpperCase().replace(" ", ""))
                ;
    }

    /**
     * Apply multiple filters to the property assessments based on specified criteria.
     *
     * @param accountNum                the account number to filter
     * @param neighborhood              the neighborhood to filter
     * @param assessmentClass           the assessment class to filter
     * @param address                   the address to filter
     * @param minAssessedValue          the minimum assessed value to filter
     * @param maxAssessedValue          the maximum assessed value to filter
     * @param customPropertyAssessments a custom PropertyAssessments object to filter
     * @return a filtered PropertyAssessments object
     */
    public PropertyAssessments filters(String accountNum, String neighborhood, String assessmentClass, String address,
                                       int minAssessedValue, int maxAssessedValue, PropertyAssessments customPropertyAssessments) {

        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        findAccountNum(propertyAssessment, accountNum) &&
                                findNeighborhood(propertyAssessment, neighborhood) &&
                                findAssessmentClass(propertyAssessment, assessmentClass) &&
                                findAddress(propertyAssessment, address) &&
                                findAssessedRange(propertyAssessment, minAssessedValue, maxAssessedValue))
                .collect(Collectors.toList());

        return new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
    }

    /**
     * Check if the assessed value of a PropertyAssessment falls in the range.
     *
     * @param property    the PropertyAssessment to check
     * @param minAssessed the minimum assessed value
     * @param maxAssessed the maximum assessed value
     * @return true if the assessed value is within the range. Else, false
     */
    private boolean findAssessedRange(PropertyAssessment property, int minAssessed, int maxAssessed) {
        int assessedValue = property.getNeighborhoodInfo().getAssessedValue();
        return assessedValue >= minAssessed && assessedValue <= maxAssessed;
    }

    /**
     * Retrieve a PropertyAssessment by account number.
     *
     * @param accountNumberInput the account number input
     * @return a PropertyAssessment object
     */
    public PropertyAssessment handleAccountNumber(String accountNumberInput) {
        int accountNumber = Integer.parseInt(accountNumberInput);
        return propertyAssessments.getPropertyByAccountNum(accountNumber);
    }

    /**
     * Filter property assessments by neighborhood.
     *
     * @param neighborhood              the neighborhood filter
     * @param customPropertyAssessments a custom PropertyAssessments object to filter (optional)
     * @return a filtered PropertyAssessments object
     */
    public PropertyAssessments filterByNeighborhood(String neighborhood, PropertyAssessments customPropertyAssessments) {
        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        findNeighborhood(propertyAssessment, neighborhood))
                .collect(Collectors.toList()); // use stream to filter by neighborhood and ignore case

        return new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
    }

    /**
     * Filter property assessments by assessment class.
     *
     * @param assessmentClass           the assessment class filter
     * @param customPropertyAssessments a custom PropertyAssessments object to filter (optional)
     * @return a filtered PropertyAssessments object
     */
    public PropertyAssessments filterByAssessment(String assessmentClass, PropertyAssessments customPropertyAssessments) {
        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        findAssessmentClass(propertyAssessment, assessmentClass))
                .collect(Collectors.toList()); // use stream to filter by assessment classes and ignore case

        return new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
    }

    /**
     * Filter property assessments by address.
     *
     * @param address                   the address filter
     * @param customPropertyAssessments a custom PropertyAssessments object to filter (optional)
     * @return a filtered PropertyAssessments object
     */
    public PropertyAssessments filterByAddress(String address, PropertyAssessments customPropertyAssessments) {
        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        findAddress(propertyAssessment, address))
                .collect(Collectors.toList());

        return new PropertyAssessments(new ArrayList<>(filteredList));
    }

    /**
     * Get a list of all PropertyAssessments.
     *
     * @return an ArrayList of PropertyAssessment objects
     */
    public ArrayList<PropertyAssessment> getAllList() {
        return propertyAssessments.getPropertyAssessmentList();
    }
}