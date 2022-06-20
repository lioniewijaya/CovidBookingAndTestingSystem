package CovidBookingTestingSystem.Controller.AdministerController;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.AdministerView.OnsiteAdministerTabPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Onsite administer tab for onsite testing for the COVID Testing Registration System.
 */
public class OnsiteAdministerTab extends AdministerTab {
    private OnsiteAdministerTabPane administerTabPane;          // Onsite administer tab display

    /**
     * Constructor.
     * @param user administerer
     */
    public OnsiteAdministerTab(User user, OnsiteAdministerTabPane onsiteAdministerTabPane) {
        super(user, onsiteAdministerTabPane);
        this.administerTabPane = onsiteAdministerTabPane;

        administerPane.getButton().addActionListener(e -> {
            try {
                ArrayList<Booking> bookingsFiltered = bookingsToAdminister();
                administerPane.updateResult(bookingsFiltered);
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
     * Search booking based on PIN Code to filter the booking on display or display all bookings
     * @return a list of onsite booking
     */
    @Override
    public ArrayList<Booking> bookingsToAdminister() throws IOException, InterruptedException, ParseException {
        bookings.clear();
        String bookingUrl = "/booking?fields=covidTests";

        Connection connection = new Connection();
        HttpResponse response = connection.getRequest(bookingUrl);

        // Filter bookings with testing site, which indicates onsite booking
        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String bookingsJsonString = response.body().toString();
            JSONArray bookingsJson = (JSONArray) parser.parse(bookingsJsonString);
            for (Object bookingJson : bookingsJson) {
                Booking booking = new Booking((JSONObject) bookingJson);
                if (booking.hasTestingSite() && !booking.isCompleted()) {
                    bookings.add(booking);
                }
            }
        }

        // Filter through the PIN input if given, else display all
        String pinInput = administerTabPane.getPinInput();
        if (pinInput.equals("")) {
            return this.bookings;
        }
        ArrayList<Booking> bookingsWithPin = new ArrayList<Booking>();
        for (Booking booking : bookings) {
            if (booking.getSmsPin().toLowerCase().equals(pinInput.toLowerCase())) {
                bookingsWithPin.add(booking);
            }
        }

        return bookingsWithPin;
    }
}
