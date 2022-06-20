package CovidBookingTestingSystem.View.AdministerView;

import CovidBookingTestingSystem.Model.BookingModel.Booking;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Administer tab pane to book a COVID test.
 */
public abstract class AdministerTabPane extends JPanel{
    protected JPanel resultPane = new JPanel();

    /**
     * Get a button
     * @return a button
     */
    public abstract JButton getButton();

    /**
     * Update the result pane in the main display of each booking encountered.
     * @param bookings a list of bookings
     */
    public void updateResult(ArrayList<Booking> bookings) {
        // clear list and insert booking
        resultPane.removeAll();
        for (Booking booking : bookings){
            resultPane.add(makeBookingPane(booking));
        }
    }
    /**
     * Make booking pane for each booking displayed
     * @param booking booking to display
     */
    public abstract JPanel makeBookingPane(Booking booking);
}
