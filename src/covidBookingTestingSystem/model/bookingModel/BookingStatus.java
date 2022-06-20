package CovidBookingTestingSystem.Model.BookingModel;

/**
 * Booking Status Enum Class
 */
public enum BookingStatus {
    INITIATED("INITIATED"),
    MODIFIED("MODIFIED"),
    CANCELLED("CANCELLED"),
    DELETED("DELETED");

    public final String label;

    BookingStatus(String label) {
        this.label = label;
    }

    /***
     * Get string based on booking status
     * @return test type string
     */
    public String toString() {
        return label;
    }

    /***
     * Get booking status based on string
     * @return booking status
     */
    public static BookingStatus getStatus(String statusString) {
        for(BookingStatus status : BookingStatus.values()) {
            if(status.toString().equals(statusString)) {
                return status;
            }
        }
        return null;
    }
}
