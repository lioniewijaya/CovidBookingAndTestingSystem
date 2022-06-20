package CovidBookingTestingSystem.Model;

import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Observable class
 */
public interface Observable {
    ArrayList<User> admins = new ArrayList<>();
    /***
     * To update the admin and notification list
     * @param command the command used
     * @param additionalSiteId additional testing site
     */
    void notify(BookingStatus command, String additionalSiteId, User user) throws InterruptedException, ParseException, IOException;
}
