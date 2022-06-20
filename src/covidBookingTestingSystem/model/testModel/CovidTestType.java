package CovidBookingTestingSystem.Model.TestModel;

/**
 * Covid Test Type Enum Class
 */
public enum CovidTestType {
    RAT("RAT"),
    PCR("PCR");

    public final String label;

    CovidTestType(String label) {
        this.label = label;
    }

    /***
     * Get string based on test type
     * @return test type string
     */
    public String toString() {
        return label;
    }

    /***
     * Get test type based on string
     * @return test type
     */
    public static CovidTestType getTestType(String testTypeString) {
        for(CovidTestType type : CovidTestType.values()) {
            if(type.toString().equals(testTypeString)) {
                return type;
            }
        }
        return null;
    }
}
