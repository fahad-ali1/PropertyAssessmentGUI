package main.utility;

import main.PropertyAssessments;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class Statistics {
    private final PropertyAssessments propertyAssessments;
    private final NumberFormat currencyFormatter;

    /**
     * Constructor for the Statistics class
     * Initializes the private instance variables with the provided values.
     * This class combines calculates statistics for a given an instance of PropertyAssessments (ArrayList) of property
     * objects.
     *
     * @param propertyAssessments      Instance of PropertyAssessments
     */
    public Statistics(PropertyAssessments propertyAssessments) {
        this.propertyAssessments = propertyAssessments;
        currencyFormatter = new DecimalFormat("$#,##0"); // create instance to format to dollars
    }

    private double getAssessedValue(int index) {
        // private method used for other method
        return propertyAssessments.getPropertyAssessmentList().get(index).getNeighborhoodInfo().getAssessedValue();
    }

    public int getNumOfProperties() {
        return propertyAssessments.size();
    }

    public String getMin() {
        return currencyFormatter.format(getAssessedValue(0));
    }

    public String getMax() {
        int lastIndex = propertyAssessments.size() - 1;
        return currencyFormatter.format(getAssessedValue(lastIndex));
    }

    public String getRange() {
        double min = getAssessedValue(0);
        double max = getAssessedValue(propertyAssessments.size() - 1);
        double range = max - min;
        return currencyFormatter.format(range);
    }

    public String getMean() {
        // calculates mean using stream
        double mean = propertyAssessments.getPropertyAssessmentList().stream()
                .mapToDouble(assessment -> assessment.getNeighborhoodInfo().getAssessedValue())
                .average()
                .orElse(0.0);

        return currencyFormatter.format(mean);
    }

    public String getMedian() {
        int lastIndex = propertyAssessments.size() / 2;
        return currencyFormatter.format(getAssessedValue(lastIndex));
    }

    @Override
    public String toString() {
        // override toString to return a proper output to the console
        currencyFormatter.setMaximumFractionDigits(0);

        return String.format("n = %d\n" +
                        "min = %s\n" +
                        "max = %s\n" +
                        "range = %s\n" +
                        "mean = %s\n" +
                        "median = %s\n",
                getNumOfProperties(),
                getMin(),
                getMax(),
                getRange(),
                getMean(),
                getMedian());
    }
}