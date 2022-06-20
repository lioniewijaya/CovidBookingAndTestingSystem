package CovidBookingTestingSystem.Controller.AdministerController;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.View.AdministerView.HomeAdministerTabPane;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Home Administer Logic for Customer role.
 */
public class HomeAdministerTab extends AdministerTab {
    /**
     * Constructor.
     * @param user administerer
     */
    public HomeAdministerTab(User user, HomeAdministerTabPane homeAdministerTabPane) {
        super(user, homeAdministerTabPane);

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

    /***
     * Search for home booking associated with the user which are not completed for covid test yet
     * @return a list of home bookings
     */
    @Override
    public ArrayList<Booking> bookingsToAdminister() throws IOException, InterruptedException, ParseException {
        bookings.clear();

        String userUrl = "/booking?fields=covidTests";

        // Loop through the bookings and filter for user's home bookings without covid test
        Connection connection = new Connection();
        HttpResponse response = connection.getRequest(userUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String bookingsJsonString = response.body().toString();
            JSONArray bookingsJson = (JSONArray) parser.parse(bookingsJsonString);
            for (Object bookingJson : bookingsJson) {
                Booking booking = new Booking((JSONObject) bookingJson);
                if (booking.getCustomerID().equals(user.getId()) && booking.getQrCode() != null && booking.getCovidTests().toString().equals("[]")) {
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
}
