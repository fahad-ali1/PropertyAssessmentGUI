package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
     * @param propertyAssessmentList      an ArrayList
     */
    public PropertyAssessments(ArrayList<PropertyAssessment> propertyAssessmentList) {
        this.propertyAssessmentList = propertyAssessmentList;
        this.propertyAssessmentAPI = null;
    }

    public PropertyAssessments() {
        this.propertyAssessmentList = new ArrayList<>();
        this.propertyAssessmentAPI = "https://data.edmonton.ca/resource/q7d6-ambg.json";
    }

    public void addList(PropertyAssessment property) {
        propertyAssessmentList.add(property);
    }

    public ArrayList<PropertyAssessment> getPropertyAssessmentList() {
        // sorts list before returning as it is used in Statistics class to grab the minimum and maximum value
        return propertyAssessmentList;
    }

    public int size() {
        return propertyAssessmentList.size();
    }

    public void hashProperty(PropertyAssessment p) {
        // hash the property to access it quick later
        propertyByAccountNum.put(p.getBuildingInfo().getAccountNum(), p);
    }

    public PropertyAssessment getPropertyByAccountNum(int accountNum) {
        return propertyByAccountNum.get(accountNum);
    }
}