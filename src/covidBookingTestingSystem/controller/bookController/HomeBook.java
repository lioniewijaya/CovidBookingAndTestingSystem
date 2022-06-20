package CovidBookingTestingSystem.Controller.BookController;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.View.BookView.BookPane;
import CovidBookingTestingSystem.View.BookView.HomeBookPane;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Instant;

/**
 * Book home testing for the COVID Testing Registration System.
 */
public class HomeBook extends Book {
    private HomeBookPane bookingPane;    // Home booking pane

    /***
     * Constructor.
     * @param user user
     */
    public HomeBook(User user) {
        this.user = user;
        this.customer = user;
        this.testingSite = null;
    }

    /***
     * Create home booking pane.
     */
    @Override
    public BookPane createBookPane() {
        bookingPane = new HomeBookPane();
        bookingPane.displaySearch(false);
        bookingPane.setUserInfo(user.description(),true);

        // Check if user ID exists
        bookingPane.getCheckButton().addActionListener(e -> {
            try {
                if (user.checkUserIdExist(bookingPane.getUserId())) {
                    User userInput = new User(bookingPane.getUserId());
                    customer = userInput;
                    bookingPane.setUserInfo(userInput.description(),true);
                }
                else {
                    bookingPane.setUserInfo("User not found",false);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException | ParseException interruptedException) {
                interruptedException.printStackTrace();
            }
        });


        // Check if current user is patient
        bookingPane.getCurrentUserCheckbox().addActionListener(e -> {
            if (bookingPane.userIsPatient()) {
                bookingPane.getConfirmButton().setVisible(true);
                bookingPane.displaySearch(false);
                customer = user;
                bookingPane.setUserInfo(customer.description(),true);
            } else {
                bookingPane.getConfirmButton().setVisible(false);
                bookingPane.displaySearch(true);
                bookingPane.setUserInfo("",false);
            }
        });

        bookingPane.getConfirmButton().addActionListener(e -> {
            try {
                confirmBooking();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });

        return bookingPane;
    }

    /***
     * Update database in web service for home booking.
     */
    @Override
    public Booking updateDatabase(Instant date) throws IOException, InterruptedException, ParseException {
        Booking booking = super.updateDatabase(date);

        // Set QR code, url, and hasKit
        String patchBookingUrl = "/booking/" + booking.getId();
        String bookingString = "{\"additionalInfo\":" + "{" +
                    "\"qrCode\":\"" + booking.getId() + "\"," +
                    "\"url\":\"" + "dummyUrlHere" + "\"," +
                    "\"hasKit\":\"" + bookingPane.haveKit() + "\"}}";

        Connection connection = new Connection();

        String bookingJsonString = (String) connection.patchRequest(patchBookingUrl,bookingString).body();
        JSONParser parser = new JSONParser();
        JSONObject bookingJson = (JSONObject) parser.parse(bookingJsonString);
        return new Booking(bookingJson);
    }

    /***
     * Confirm booking which updates GUI and database for home booking.
     */
    @Override
    public void confirmBooking() throws IOException, InterruptedException, ParseException {
        Booking booking = updateDatabase(bookingPane.getDateSelected());
        bookingPane.getConfirmButton().setText("booking confirmed - qr code: " + booking.getQrCode() + " | url: " + booking.getUrlVideo());
        bookingPane.getConfirmButton().setEnabled(false);
    }
}
