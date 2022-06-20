package CovidBookingTestingSystem.Controller.BookController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookView.BookPane;
import CovidBookingTestingSystem.View.BookView.OfflineOnSiteBookPane;
import org.json.simple.parser.ParseException;
import java.io.IOException;

/**
 * Offline booking for onsite testing for the COVID Testing Registration System.
 */
public class OfflineOnSiteBook extends Book {
    private OfflineOnSiteBookPane bookingPane; // Offline booking site testing pane

    /***
     * Constructor.
     * @param site testing site
     */
    public OfflineOnSiteBook(User user, TestingSite site) {
        this.user = user;
        this.testingSite = site;
    }

    /***
     * Create offline onsite booking pane.
     */
    @Override
    public BookPane createBookPane() {
        bookingPane = new OfflineOnSiteBookPane();

        // Check if user ID exists
        bookingPane.getCheckButton().addActionListener(e -> {
            try {
                if (user.checkUserIdExist(bookingPane.getUserId())) {
                    User existingUser = new User(bookingPane.getUserId());
                    bookingPane.setUserInfo(existingUser.description(),true);
                    customer = existingUser;
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

        // Confirm booking
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
     * Confirm booking which updates GUI and database for offline onsite booking.
     */
    @Override
    public void confirmBooking() throws IOException, InterruptedException, ParseException {
        Booking booking = updateDatabase(bookingPane.getDateSelected());
        bookingPane.getConfirmButton().setText("booking confirmed - sms pin: " + booking.getSmsPin());
        bookingPane.getConfirmButton().setEnabled(false);
        bookingPane.getCheckButton().setEnabled(false);
    }
}
