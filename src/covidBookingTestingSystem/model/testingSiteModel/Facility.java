package CovidBookingTestingSystem.Model.TestingSiteModel;

/**
 * Enum to indicate different facilities available in a testing site.
 */
public enum Facility {
    DRIVE_THROUGH("Drive-through"),
    WALK_IN("Walk-in"),
    CLINIC("Clinic"),
    GP("General Practitioner"),
    HOSPITAL("Hospital");

    public final String label;

    Facility(String label) {
        this.label = label;
    }

    /***
     * Get string based on facility
     * @return facility string
     */
    public String toString() {
        return label;
    }

    /***
     * Get facility based on string
     * @return facility
     */
    public static Facility getFacility(String facilityString) {
        for(Facility type : Facility.values()) {
            if(type.toString().equals(facilityString)) {
                return type;
            }
        }
        return null;
    }
}