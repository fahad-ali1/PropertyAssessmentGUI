package main.utility;

/**
 * AssessmentClass holds information regarding assessment of the property
 * <p>
 * Author: Fahad Ali
 */
public class AssessmentClass {
    private final double assessmentClass1Percent;
    private final double assessmentClass2Percent;
    private final double assessmentClass3Percent;
    private final String assessmentClass1;
    private final String assessmentClass2;
    private final String assessmentClass3;

    /**
     * Constructor for the AssessmentClass.
     * Initializes the variables with the provided values.
     *
     * @param assessmentClass1Percent Percentage for assessment class 1.
     * @param assessmentClass2Percent Percentage for assessment class 2.
     * @param assessmentClass3Percent Percentage for assessment class 3.
     * @param assessmentClass1        Name of assessment class 1.
     * @param assessmentClass2        Name of assessment class 2.
     * @param assessmentClass3        Name of assessment class 3.
     */
    public AssessmentClass(double assessmentClass1Percent,
                           double assessmentClass2Percent, double assessmentClass3Percent,
                           String assessmentClass1, String assessmentClass2,
                           String assessmentClass3) {
        this.assessmentClass1Percent = assessmentClass1Percent;
        this.assessmentClass2Percent = assessmentClass2Percent;
        this.assessmentClass3Percent = assessmentClass3Percent;
        this.assessmentClass1 = assessmentClass1;
        this.assessmentClass2 = assessmentClass2;
        this.assessmentClass3 = assessmentClass3;
    }

    /**
     * Get the percentage for assessment class 1.
     *
     * @return The percentage for assessment class 1.
     */
    public double getAssessment1Percent() {
        return assessmentClass1Percent;
    }

    /**
     * Get the percentage for assessment class 2.
     *
     * @return The percentage for assessment class 2.
     */
    public double getAssessment2Percent() {
        return assessmentClass2Percent;
    }

    /**
     * Get the percentage for assessment class 3.
     *
     * @return The percentage for assessment class 3.
     */
    public double getAssessment3Percent() {
        return assessmentClass3Percent;
    }

    /**
     * Get the name of assessment class 1.
     *
     * @return The name of assessment class 1.
     */
    public String getAssessmentClass1() {
        return assessmentClass1;
    }

    /**
     * Get the name of assessment class 2.
     *
     * @return The name of assessment class 2.
     */
    public String getAssessmentClass2() {
        return assessmentClass2;
    }

    /**
     * Get the name of assessment class 3.
     *
     * @return The name of assessment class 3.
     */
    public String getAssessmentClass3() {
        return assessmentClass3;
    }

}