package CovidBookingTestingSystem.Controller.AdministerController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.Controller.TabController;
import CovidBookingTestingSystem.View.AdministerView.AdministerTabPane;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Administer tab to book a COVID test.
 */
public abstract class AdministerTab implements TabController {
    protected User user;                                          // User as administerer
    protected AdministerTabPane administerPane;                   // Administer tab pane
    protected ArrayList<Booking> bookings = new ArrayList<>();    // List of  bookings

    public AdministerTab(User user, AdministerTabPane administerPane) {
        this.user = user;
        this.administerPane = administerPane;
    }

    /***
     * Create administer tab pane for different booking type.
     */
    public abstract ArrayList<Booking> bookingsToAdminister() throws IOException, InterruptedException, ParseException;

    /**
     * Get a tab component containing tab pane view
     * @return tap component
     */
    @Override
    public JComponent getTabComponent() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout());
        panel.add(administerPane);
        return panel;
    }
}
