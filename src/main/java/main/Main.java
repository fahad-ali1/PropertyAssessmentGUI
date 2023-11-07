package main;

import main.dao.ApiPropertyAssessmentDAO;
import main.dao.CsvPropertyAssessmentDAO;
import main.dao.PropertyAssessmentDAO;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a scanner for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a data source (1 for CSV, 2 for API):");
        int dataSourceChoice = scanner.nextInt();

        PropertyAssessmentDAO dao;

        if (dataSourceChoice == 1) {
            // User chooses CSV data source
            dao = new CsvPropertyAssessmentDAO();
        } else if (dataSourceChoice == 2) {
            // User chooses API data source
            dao = new ApiPropertyAssessmentDAO();
        } else {
            System.out.println("Invalid data source choice.");
            return;
        }

        String accountNumTest = "1179381";
        System.out.println(dao.getByAccountNumber(accountNumTest));
//
//        String neighborhoodTest = "Granville";
//        dao.getByNeighbourhood(neighborhoodTest);
//
//        String assessmentTest = "residential";
//        dao.getByAssessmentClass(assessmentTest);

//        List<PropertyAssessment> allAssessments = dao.getAll();
//            for (PropertyAssessment assessment : allAssessments) {
//                System.out.println(assessment);
//            }
    }
}
