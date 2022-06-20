package CovidBookingTestingSystem.Controller.BookingModificationController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookingModificationView.ProfileTabPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Profile tab for resident to control onsite booking
 */
public class ProfileTab extends BookingModificationTab {
    private ProfileTabPane tabPane; // Profile tab display

    /**
     * Constructor.
     */
    public ProfileTab(ProfileTabPane profilePane, User user) {
        super(user,profilePane);
        this.tabPane = profilePane;

        profilePane.getActiveBookingButton().addActionListener(e -> {
            // display active bookings
            try {
                bookings = userActiveBooking();
                profilePane.updateResult(bookings);
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
     * Get user's active onsite bookings which have not performed any test yet.
     * @return array of bookings
     */
    public ArrayList<Booking> userActiveBooking() throws IOException, InterruptedException, ParseException {
        bookings.clear();

        String userUrl = "/booking?fields=covidTests";

        // Loop through the bookings and filter for user's onsite bookings without covid test
        HttpResponse response = connection.getRequest(userUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String bookingsJsonString = response.body().toString();
            JSONArray bookingsJson = (JSONArray) parser.parse(bookingsJsonString);
            for (Object bookingJson : bookingsJson) {
                Booking booking = new Booking((JSONObject) bookingJson);
                if (booking.getCustomerID().equals(user.getId()) && booking.getQrCode() == null && !booking.isCompleted()) {
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
}
