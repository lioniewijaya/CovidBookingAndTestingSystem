package CovidBookingTestingSystem.Controller.AdministerController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.TestModel.CovidTest;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.AdministerView.OnSiteAdministerFrame;
import CovidBookingTestingSystem.View.AdministerView.OnSiteAdministerPane;

import javax.swing.*;
import java.io.IOException;

/**
 * Onsite administer for onsite testing for the COVID Testing Registration System.
 */
public class OnSiteAdminister {
    private OnSiteAdministerPane administerPane;    // Onsite administer display
    private CovidTest covidTest;                    // Covid test it relates to

    /**
     * Constructor.
     * @param administerPane onsite administer display
     * @param booking onsite book it relates to
     */
    public OnSiteAdminister(OnSiteAdministerPane administerPane, Booking booking, JButton testButton, User administerer) {
        this.administerPane = administerPane;
        new OnSiteAdministerFrame().setPane(administerPane);

        // Creates a new covid test after submitting
        this.administerPane.getSubmitButton().addActionListener(e -> {
            testButton.setEnabled(false);
            this.administerPane.getSubmitButton().setEnabled(false);
            this.administerPane.getSubmitButton().setText("Test taken");
            this.administerPane.getButton().setEnabled(false);
            covidTest = new CovidTest(booking.getId(), booking.getCustomerID(), administerPane.getType(), administerer);
            try {
                covidTest.updateDatabase();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }
}
