package main.dao;

import main.PropertyAssessment;
import main.PropertyAssessments;
import main.processData.ProcessData;

import java.util.List;

public class CsvPropertyAssessmentDAO implements PropertyAssessmentDAO {
    private final ProcessData file;

    public CsvPropertyAssessmentDAO() {
        String csvFilePath = "src/main/java/main/Property_Assessment_Data_2023.csv";
        this.file = new ProcessData(csvFilePath);
        file.processFile(null, null, null);
    }

    @Override
    public PropertyAssessment getByAccountNumber(String accountNumber) {
        return file.handleAccountNumber(accountNumber);
    }

    @Override
    public PropertyAssessments getByNeighbourhood(String neighbourhood) {
        // set second parameter to null so that it uses the properties
        // that are built with the CSV inside the class
        return file.filterByNeighborhood(neighbourhood, null);
    }

    @Override
    public PropertyAssessments getByAssessmentClass(String assessmentClass) {
        return file.filterByAssessment(assessmentClass, null);
    }

    @Override
    public List<PropertyAssessment> getAll() {
        return file.getAllList();
    }
}