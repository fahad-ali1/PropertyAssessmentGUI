package main.processData;

import main.PropertyAssessment;
import main.PropertyAssessments;
import main.utility.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessData {
    private final String csvFileName;
    private final PropertyAssessments propertyAssessments;

    /**
     * Constructor for the ProcessFile
     * Initializes the private instance variables with the provided values.
     * This class is one of the main driving classes for the program. It takes a CSV file and processes it line by line.
     * It has methods to handle an errors, filtering, and retrieve information.
     *
     * @param csvFileName      a CSV file with the proper formatting
     */
    public ProcessData(String csvFileName) {
        this.csvFileName = csvFileName;
        this.propertyAssessments = new PropertyAssessments(new ArrayList<>()); // create a PropertyAssessments object
    }

    /**
     * Constructor for ProcessFile without a filename.
     * Initializes the private instance variables without a filename.
     */
    public ProcessData() {
        this.csvFileName = null;
        this.propertyAssessments = new PropertyAssessments(new ArrayList<>());
    }

    public void processFile(String AccountNumber, String neighborhoodToMatch, String assessmentClassToMatch) {
        if (propertyAssessments.size() > 0) { // if there is already information in the list, then we can filter it
            filters(AccountNumber, neighborhoodToMatch, assessmentClassToMatch);
        } else { // means there is no information in the list, so we need to add some property objects
            try (BufferedReader fileReader = new BufferedReader(new FileReader(csvFileName))) {
                fileReader.readLine(); // Skip the first line in CSV as they are titles
                String line;

                while ((line = fileReader.readLine()) != null) { // read till end of file
                    String[] cell = line.split(",", Constants.NUM_COLUMNS);
                    PropertyAssessment property = getPropertyAssessment(cell);
                    addAssessment(property); // add parsed property to PropertyAssessments object
                }
            } catch (IOException e) {
                handleFileOpenError(); // handle error
            }
        }
    }

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

    private static int parseInt(String value) {
        // this method turns the string into an int and handles errors
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDouble(String value) {
        // this method turns the string into a double and handles errors
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private void handleFileOpenError() {
        // this method simplifies error handling for a file
        System.err.println("Error: Can't open file " + csvFileName);
    }

    private void addAssessment(PropertyAssessment property) {
        // this method will hash and add a property object to the PropertyAssessments instance
        propertyAssessments.hashProperty(property);
        propertyAssessments.addList(property);
    }

    private void filters (String AccountNumber, String neighborhoodToMatch, String assessmentClassToMatch){
        // this method calls the appropriate method to filter depending on what is inputted
        if (AccountNumber != null) {
            System.out.println(handleAccountNumber(AccountNumber));
        }else if (neighborhoodToMatch != null) {
            filterByNeighborhood(neighborhoodToMatch, propertyAssessments);
        }else if (assessmentClassToMatch != null) {
            filterByAssessment(assessmentClassToMatch, propertyAssessments);
        }
    }

    public PropertyAssessments filterByNeighborhood(String neighborhood, PropertyAssessments customPropertyAssessments) {
        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        propertyAssessment.getNeighborhoodInfo().getNeighborhood().equalsIgnoreCase(neighborhood))
                .collect(Collectors.toList()); // use stream to filter by neighborhood and ignore case

        return new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
    }

    public PropertyAssessments filterByAssessment(String assessmentClass, PropertyAssessments customPropertyAssessments) {
        PropertyAssessments assessments = (customPropertyAssessments != null) ? customPropertyAssessments : propertyAssessments;

        List<PropertyAssessment> filteredList = assessments.getPropertyAssessmentList()
                .stream()
                .filter(propertyAssessment ->
                        propertyAssessment.getAssessmentClass().getAssessmentClass1().equalsIgnoreCase(assessmentClass)
                        || propertyAssessment.getAssessmentClass().getAssessmentClass2().equalsIgnoreCase(assessmentClass)
                        || propertyAssessment.getAssessmentClass().getAssessmentClass3().equalsIgnoreCase(assessmentClass))
                .collect(Collectors.toList()); // use stream to filter by assessment classes and ignore case

        return new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
    }

//    private void filterStat(List<PropertyAssessment> filteredList){
//        // this method will take a new list to grab stats on
//        PropertyAssessments filteredPropertyAssessments =
//                new PropertyAssessments((ArrayList<PropertyAssessment>) filteredList);
//        getStat(filteredPropertyAssessments); // call stat method to get stats on the filtered list
//    }

    public PropertyAssessment handleAccountNumber(String accountNumberInput) {
        try {
            int accountNumber = Integer.parseInt(accountNumberInput);
            return propertyAssessments.getPropertyByAccountNum(accountNumber);
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid input...\nPlease provide a valid integer account number.");
            return null;
        }
    }

//    private void dataNotFound(PropertyAssessments list){
//        // this method will handle errors if there is no data in the PropertyAssessments list
//        if (list.size() == 0){
//            System.err.println("Data not found!");
//        }
//    }

//    private void getStat(PropertyAssessments list) {
//        // this method will return stats on a certain list as long as the list has data
//        if (list.size() > 0) {
//            Statistics statistics = new Statistics(list);
//            System.out.println(statistics);
//        }dataNotFound(list);
//    }

//    public void getAllStat() {
//        // this method will return all stats as long as the list has data
//        System.out.println("\nDescriptive statistics of all assessments");
//        if (propertyAssessments.size() > 0) {
//            Statistics statistics = new Statistics(propertyAssessments);
//            System.out.println(statistics);
//        }dataNotFound(propertyAssessments);
//    }

    public ArrayList<PropertyAssessment> getAllList(){
        return propertyAssessments.getPropertyAssessmentList();
    }
}