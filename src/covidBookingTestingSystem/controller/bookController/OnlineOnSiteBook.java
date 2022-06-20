package CovidBookingTestingSystem.Controller.BookController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookView.BookPane;
import CovidBookingTestingSystem.View.BookView.OnlineOnSiteBookPane;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Online booking for onsite testing for the COVID Testing Registration System.
 */
public class OnlineOnSiteBook extends Book {
    private OnlineOnSiteBookPane bookingPane;

    /***
     * Constructor.
     * @param user user
     * @param site testing site
     */
    public OnlineOnSiteBook(User user, TestingSite site) {
        this.user = user;
        this.customer = user;
        this.testingSite = site;
    }

    /***
     * Create online onsite booking pane.
     */
    @Override
    public BookPane createBookPane() {
        bookingPane = new OnlineOnSiteBookPane();

        bookingPane.displaySearch(false);
        bookingPane.setUserInfo(user.description(),true);

        // Check if user is patient
        bookingPane.getCurrentUserCheckbox().addActionListener(e -> {
            if (bookingPane.userIsPatient()) {
                bookingPane.getConfirmButton().setVisible(true);
                bookingPane.displaySearch(false);
                customer = user;
                bookingPane.setUserInfo(user.description(),true);
            } else {
                bookingPane.getConfirmButton().setVisible(false);
                bookingPane.displaySearch(true);
                bookingPane.setUserInfo("",false);
            }
        });

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

    @Override
    /***
     * Confirm booking which updates GUI and database for online onsite booking.
     */
    public void confirmBooking() throws IOException, InterruptedException, ParseException {
        Booking booking = updateDatabase(bookingPane.getDateSelected());
        bookingPane.getConfirmButton().setText("booking confirmed - sms pin: " + booking.getSmsPin());
        bookingPane.getConfirmButton().setEnabled(false);
    }
}
