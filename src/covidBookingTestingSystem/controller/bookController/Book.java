package CovidBookingTestingSystem.Controller.BookController;

import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import CovidBookingTestingSystem.View.BookView.BookPane;
import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Instant;

/**
 * Book for the COVID Testing Registration System. Can be classified further into book for home testing and book for onsite testing.
 */
public abstract class Book {
    protected TestingSite testingSite;  // Booking's testing site
    protected User user;                // Booking's user
    protected User customer;            // Booking's customer

    /***
     * Create booking pane for different booking type.
     */
    public abstract BookPane createBookPane();

    /***
     * Confirm booking which updates GUI and database, slightly differs for different booking type.
     */
    public abstract void confirmBooking() throws IOException, InterruptedException, ParseException;

    /***
     * Update database in web service for general booking.
     */
    public Booking updateDatabase(Instant date) throws IOException, InterruptedException, ParseException {
        String testingSiteId;
        if (testingSite != null) {
            testingSiteId = "\"testingSiteId\":\"" + testingSite.getId() + "\",";
        } else {
            testingSiteId = "";
        }

        String createBookingUrl = "/booking";
        String bookingString = "{\"customerId\":\"" + customer.getId() + "\"," +
                testingSiteId +
                "\"startTime\":\"" + date.toString() + "\"," +
                "\"status\":\"" + BookingStatus.INITIATED + "\"," +
                "\"notes\":\"" + "null" + "\"," +
                "\"additionalInfo\":" + "{}" + "}";

        Connection connection = new Connection();

        String bookingJsonString = (String) connection.postRequest(createBookingUrl,bookingString).body();
        JSONParser parser = new JSONParser();
        JSONObject bookingJson = (JSONObject) parser.parse(bookingJsonString);

        Booking booking = new Booking(bookingJson);
        try {
            user.setNotification(BookingStatus.INITIATED, user, booking.getId());
        } catch (Exception e) {}

        return booking;
    }
}
