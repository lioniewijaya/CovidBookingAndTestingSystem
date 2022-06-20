package CovidBookingTestingSystem.Controller.BookingModificationController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookingModificationView.AdminTabPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Admin tab for receptionist for the COVID Testing Registration System.
 */
public class AdminTab extends BookingModificationTab{
    private AdminTabPane tabPane;   // Admin onsite administer tab display

    /**
     * Constructor.
     * @param user administerer
     */
    public AdminTab(AdminTabPane adminTabPane, User user) throws InterruptedException, ParseException, IOException {
        super(user,adminTabPane);
        this.tabPane = adminTabPane;

        notify(user.getNotification());

        adminTabPane.getSearchPinButton().addActionListener(e -> {
            try {
                Booking booking = getBookingByPinCode();
                if (booking != null) {
                    bookings.add(booking);
                    adminTabPane.updateResult(bookings);
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });

        adminTabPane.getClearNotifButton().addActionListener(e -> {
            adminTabPane.getNotificationContainer().removeAll();
            try {
                user.deleteNotification();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });

        adminTabPane.getRefreshNotifButton().addActionListener(e -> {
            adminTabPane.getNotificationContainer().removeAll();
            try {
                notify(user.getNotification());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });
    }

    /**
     * Get booking by PIN code input
     * @return booking
     */
    public Booking getBookingByPinCode() throws IOException, InterruptedException, ParseException {
        bookings.clear();

        // Filter through the PIN input if given, else display all
        String pinInput = tabPane.getPinInput();
        if (!pinInput.equals("")) {

            String bookingUrl = "/booking?fields=covidTests";

            HttpResponse response = connection.getRequest(bookingUrl);

            // Filter bookings with testing site, which indicates onsite booking
            ArrayList<Booking> allBookings = new ArrayList<>();
            if (response.statusCode() == 200) {
                JSONParser parser = new JSONParser();
                String bookingsJsonString = response.body().toString();
                JSONArray bookingsJson = (JSONArray) parser.parse(bookingsJsonString);
                for (Object bookingJson : bookingsJson) {
                    Booking booking = new Booking((JSONObject) bookingJson);
                    if (booking.hasTestingSite()) {
                        allBookings.add(booking);
                    }
                }
            }

            for (Booking booking : allBookings) {
                if (booking.getSmsPin().toLowerCase().equals(pinInput.toLowerCase())) {
                    return booking;
                }
            }
        }
        return null;
    }

    /***
     * Update the admin tab pane with notification
     * @param notification an array of notifications
     */
    public void notify(ArrayList<String> notification) throws InterruptedException, ParseException, IOException {
        tabPane.updateNotification(notification);
    }
}
