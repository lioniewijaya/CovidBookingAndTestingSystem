package CovidBookingTestingSystem.View.BookingModificationView;

import CovidBookingTestingSystem.Model.BookingModel.Booking;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Base class for tab pane view to modify and delete booking for the COVID Testing Registration System.
 */
public abstract class BookingModificationTabPane extends JPanel {
    protected JPanel searchIdPane;
    protected JTextField idTextField;
    protected JButton searchIdButton = new JButton("Search ID");
    protected JPanel resultPane;

    /***
     * Get search ID button
     * @return search ID button
     */
    public JButton getSearchIdButton() {
        return searchIdButton;
    }

    /**
     * Get Booking ID input
     *
     * @return the Booking ID Input
     */
    public String getBookingID() {
        return idTextField.getText();
    }

    /**
     * Update result pane from OnsiteAdministerTab.
     *
     * @param bookings a list of bookings
     */
    public void updateResult(ArrayList<Booking> bookings) {
        // clear list and insert site
        resultPane.removeAll();
        for (Booking booking : bookings) {
            resultPane.add(makeBookingPane(booking));
        }
    }

    /**
     * Make individual site pane to be added to the result pane.
     * @param book book
     * @return the panel
     */
    abstract protected JPanel makeBookingPane(Booking book);
}
