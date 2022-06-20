package CovidBookingTestingSystem.Controller.BookingModificationController;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.Controller.TabController;
import CovidBookingTestingSystem.View.BookingModificationView.BookingModificationTabPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Base class for logic in tab to modify and delete booking for the COVID Testing Registration System.
 */
public abstract class BookingModificationTab implements TabController {
    protected User user;                                            // User
    protected BookingModificationTabPane modificationPane;          // Modification tab pane
    protected ArrayList<Booking> bookings = new ArrayList<>();      // List of bookings
    protected Connection connection = new Connection();             // Connection

    /**
     * Constructor.
     * @param user user
     * @param tabPane tab pane
     */
    public BookingModificationTab(User user, BookingModificationTabPane tabPane) {
        this.user = user;
        this.modificationPane = tabPane;

        this.modificationPane.getSearchIdButton().addActionListener(e -> {
            // search by ID
            try {
                Booking booking = getBookingById();
                if (booking != null) {
                    bookings.add(booking);
                    tabPane.updateResult(bookings);
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    /**
     * Get booking by ID input
     * @return booking
     */
    private Booking getBookingById() throws InterruptedException, ParseException, IOException {
        bookings.clear();
        String idInput = modificationPane.getBookingID();

        if (!idInput.equals("")) {
            String bookingUrl = "/booking/" + idInput;

            HttpResponse response = connection.getRequest(bookingUrl);

            if (response.statusCode() == 200) {
                JSONParser parser = new JSONParser();
                String bookingJsonString = response.body().toString();
                JSONObject bookingJson = (JSONObject) parser.parse(bookingJsonString);
                return new Booking(bookingJson);

            }
        }
        return null;
    }

    /**
     * Get a tab component containing tab pane view
     * @return tap component
     */
    @Override
    public JComponent getTabComponent() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout());
        panel.add(modificationPane);
        return panel;
    }
}
