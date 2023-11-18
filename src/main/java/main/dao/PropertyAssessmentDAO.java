package main.dao;

import main.utility.PropertyAssessment;
import main.utility.PropertyAssessments;

import java.util.List;

/**
 * The PropertyAssessmentDAO interface holds methods for retrieving property assessment data.
 * <p>
 * Author: Fahad Ali
 */
public interface PropertyAssessmentDAO {
    /**
     * Retrieve a single PropertyAssessment by account number.
     *
     * @param accountNumber The account number used to search for a specific PropertyAssessment.
     * @return A PropertyAssessment object given the account number, or null if not found.
     */
    PropertyAssessment getByAccountNumber(String accountNumber);

    /**
     * Retrieve range of PropertyAssessments by neighborhood.
     *
     * @param neighbourhood The neighborhood name to filter PropertyAssessments by.
     * @return PropertyAssessments containing assessments in the neighborhood.
     */
    PropertyAssessments getByNeighbourhood(String neighbourhood);

    /**
     * Retrieve range of PropertyAssessments by address.
     *
     * @param address The address (street name, house number, or suite) to filter PropertyAssessments by.
     * @return PropertyAssessments containing assessments matching the provided address.
     */
    PropertyAssessments getByAddress(String address);

    /**
     * Retrieve PropertyAssessments by assessment class.
     *
     * @param assessmentClass The assessment class to filter PropertyAssessments by.
     * @return PropertyAssessments containing assessments matching the specified assessment class.
     */
    PropertyAssessments getByAssessmentClass(String assessmentClass);

    /**
     * Get range of Properties based on multiple filters
     *
     * @param accountNum             The account number to filter assessments.
     * @param neighborhoodToMatch    The neighborhood to filter assessments.
     * @param assessmentClassToMatch The assessment class to filter assessments.
     * @param addressToMatch         The address to filter assessments.
     * @param minAssessedValue       The minimum assessed value to filter assessments.
     * @param maxAssessedValue       The maximum assessed value to filter assessments.
     * @return PropertyAssessments containing assessments that match all specified criteria.
     */
    PropertyAssessments multipleFilter(String accountNum, String neighborhoodToMatch, String assessmentClassToMatch,
                                       String addressToMatch, String minAssessedValue, String maxAssessedValue);

    /**
     * Retrieve a list of all PropertyAssessments.
     *
     * @return A list of all PropertyAssessment objects available.
     */
    List<PropertyAssessment> getAll();
}