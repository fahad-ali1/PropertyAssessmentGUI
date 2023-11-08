
package main.utility;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * The PropertyAssessments class manages a list of PropertyAssessment objects and provides methods
 * for adding, accessing, and manipulating the property assessment data.
 */
public class PropertyAssessments {
    private final ArrayList<PropertyAssessment> propertyAssessmentList;
    private final String propertyAssessmentAPI;
    private final HashMap<Integer, PropertyAssessment> propertyByAccountNum = new HashMap<>();

    /**
     * Constructor for the PropertyAssessments
     * Initializes the private instance variables with the provided values.
     * This class is one of the main driving classes for the program. It creates an ArrayList of PropertyAssessment objects
     * and manages them through the provided methods. It also hashes the account number for quick retrieval.
     *
     * @param propertyAssessmentList an ArrayList
     */
    public PropertyAssessments(ArrayList<PropertyAssessment> propertyAssessmentList) {
        this.propertyAssessmentList = propertyAssessmentList;
        this.propertyAssessmentAPI = null;
    }


    /**
     * Default constructor for PropertyAssessments.
     * Initializes an empty list of PropertyAssessment objects and uses specified API.
     */
    public PropertyAssessments() {
        this.propertyAssessmentList = new ArrayList<>();
        this.propertyAssessmentAPI = "https://data.edmonton.ca/resource/q7d6-ambg.json";
    }

    /**
     * Add a PropertyAssessment object to the list of property assessments.
     *
     * @param property The PropertyAssessment object to be added.
     */
    public void addList(PropertyAssessment property) {
        propertyAssessmentList.add(property);
    }

    /**
     * Get the list of PropertyAssessment objects.
     *
     * @return An ArrayList of PropertyAssessment objects.
     */
    public ArrayList<PropertyAssessment> getPropertyAssessmentList() {
        return propertyAssessmentList;
    }

    /**
     * Get the size of the list of PropertyAssessment objects.
     *
     * @return The size of the list.
     */
    public int size() {
        return propertyAssessmentList.size();
    }

    /**
     * Hash a PropertyAssessment by its account number for quick retrieval.
     *
     * @param property The PropertyAssessment to be hashed.
     */
    public void hashProperty(PropertyAssessment property) {
        propertyByAccountNum.put(property.getBuildingInfo().getAccountNum(), property);
    }

    /**
     * Retrieve a PropertyAssessment by its account number.
     *
     * @param accountNum The account number for the PropertyAssessment to retrieve.
     * @return The PropertyAssessment associated with the specified account number.
     */
    public PropertyAssessment getPropertyByAccountNum(int accountNum) {
        return propertyByAccountNum.get(accountNum);
    }
}