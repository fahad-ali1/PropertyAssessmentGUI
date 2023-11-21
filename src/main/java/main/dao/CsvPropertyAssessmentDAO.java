package main.dao;

import main.utility.PropertyAssessment;
import main.utility.PropertyAssessments;
import main.processData.ProcessData;

import java.util.List;

/**
 * The CsvPropertyAssessmentDAO class implements the PropertyAssessmentDAO interface and provides access to property
 * assessment data in a predefined CSV file.
 * <p>
 * Author: Fahad Ali
 */
public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {
    private final ProcessData file;

    /**
     * Constructs a CsvPropertyAssessmentDAO and initializes it with the CSV file.
     * The CSV file is processed here.
     */
    public CsvPropertyAssessmentDAO() {
        String csvFilePath = "src/main/java/main/Property_Assessment_Data_2023.csv";
        this.file = new ProcessData(csvFilePath);
        file.processFile();
    }

    /**
     * Retrieves a PropertyAssessment by the account number.
     *
     * @param accountNumber The account number of the property.
     * @return The PropertyAssessment with the account number.
     */
    @Override
    public PropertyAssessment getByAccountNumber(String accountNumber) {
        return file.handleAccountNumber(accountNumber.trim());
    }

    /**
     * Retrieves PropertyAssessment(s) by the specified address, can be singled by suite, house number, or street number,
     * or all three.
     *
     * @param address The address (or part of address) of the property.
     * @return The PropertyAssessment with the specified address.
     */
    @Override
    public PropertyAssessments getByAddress(String address) {
        // set second parameter to null so that it uses the properties
        // that are already populated with the CSV inside this DAO
        return file.filterByAddress(address.trim(), null);
    }

    /**
     * Retrieves PropertyAssessments by the specified neighbourhood.
     *
     * @param neighbourhood The neighborhood of the properties.
     * @return The PropertyAssessments with the specified neighborhood.
     */
    @Override
    public PropertyAssessments getByNeighbourhood(String neighbourhood) {
        return file.filterByNeighborhood(neighbourhood.trim(), null);
    }

    /**
     * Retrieves PropertyAssessments by the specified assessmentClass.
     *
     * @param assessmentClass The assessmentClass of the properties.
     * @return The PropertyAssessments with the specified assessmentClass.
     */
    @Override
    public PropertyAssessments getByAssessmentClass(String assessmentClass) {
        return file.filterByAssessment(assessmentClass.trim(), null);
    }

    /**
     * Retrieves PropertyAssessments by the specified filters.
     *
     * @param accountNum      The account number of the property.
     * @param neighbourhood   The neighbourhood of the properties.
     * @param assessmentClass The assessment class of the properties.
     * @param address         The address for properties.
     * @param min             The minimum assessed value for filtering properties.
     * @param max             The maximum assessed value for filtering properties.
     * @return The PropertyAssessments that match the specified filters.
     */
    @Override
    public PropertyAssessments multipleFilter(String accountNum, String neighbourhood, String assessmentClass,
                                              String address, String min, String max) {
        int minValue = Integer.parseInt(min.trim());
        int maxValue = Integer.parseInt(max.trim());

        return file.filters(accountNum, neighbourhood, assessmentClass, address,
                minValue, maxValue, null);
    }

    /**
     * Retrieves a list of all PropertyAssessments from the CSV.
     *
     * @return List of all PropertyAssessments available in the CSV.
     */
    @Override
    public List<PropertyAssessment> getAll() {
        return file.getAllList();
    }
}