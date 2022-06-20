package CovidBookingTestingSystem.Model;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Observer interface
 */
public interface Observer {
    /***
     * Update the admin tab pane with notification
     */
    ArrayList getNotification() throws InterruptedException, ParseException, IOException;
}
