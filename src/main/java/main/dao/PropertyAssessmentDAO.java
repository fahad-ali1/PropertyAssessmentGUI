package main.dao;

import main.PropertyAssessment;
import main.PropertyAssessments;

import java.util.List;

public interface PropertyAssessmentDAO {
    PropertyAssessment getByAccountNumber(String accountNumber);
    PropertyAssessments getByNeighbourhood(String neighbourhood);
    PropertyAssessments getByAssessmentClass(String assessmentClass);
    List<PropertyAssessment> getAll();
}