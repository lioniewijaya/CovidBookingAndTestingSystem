package CovidBookingTestingSystem.Model.BookingModel;

import java.time.Instant;

/**
 * Booking Memento storing a state of testing site and time for testing.
 */
public class BookingMemento {
    private String testingSideId;
    private Instant date;

    /***
     * Constructor.
     */
    public BookingMemento(String testingSideId, Instant date) {
        this.testingSideId = testingSideId;
        this.date = date;
    }

    /***
     * Get testing site ID from memento state.
     * @return testing site ID
     */
    public String getTestingSideId() {
        return testingSideId;
    }

    /***
     * Get date selected from memento state.
     * @return date selected
     */
    public Instant getDate() {
        return date;
    }
}
