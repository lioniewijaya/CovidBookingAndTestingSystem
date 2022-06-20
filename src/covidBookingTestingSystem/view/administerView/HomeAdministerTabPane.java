package CovidBookingTestingSystem.View.AdministerView;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.TestModel.CovidTest;
import CovidBookingTestingSystem.Model.TestModel.CovidTestType;
import CovidBookingTestingSystem.Model.UserModel.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

/**
 * Home administer tab pane to be displayed in the main frame during the process.
 */
public class HomeAdministerTabPane extends AdministerTabPane {
    private JButton refresh = new JButton("Refresh");   // Refresh button
    private User administerer;                               // Administerer

    /**
     * Constructor.
     */
    public HomeAdministerTabPane(User administerer){
        this.administerer = administerer;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(""));

        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new TitledBorder("Home Bookings"));

        add(scrollPane,BorderLayout.CENTER);
        add(refresh,BorderLayout.NORTH);
    }

    @Override
    /**
     * Make a booking panel for each item encountered.
     * @param booking home booking
     * @return panel of each item
     */
    public JPanel makeBookingPane(Booking booking){
        // Create UI
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new TitledBorder(""));

        // Adding Component
        userPanel.add(new JLabel("Patient's Name: " + booking.getCustomerName()));
        userPanel.add(new JLabel("Start Time: " + booking.getStartTime()));
        userPanel.add(new JLabel("Additional Info: " + booking.getAdditionalInfo()));

        // Test button logic
        JButton testButton = new JButton("Test");
        testButton.setEnabled(false);
        userPanel.add(testButton);
        testButton.addActionListener(e -> {
            CovidTest covidTest = new CovidTest(booking.getId(), booking.getCustomerID(), CovidTestType.RAT, administerer);
            resultPane.remove(userPanel);
            try {
                covidTest.updateDatabase();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        // HasKit logic check, do not allow testing if kit is not available
        JLabel hasKitLabel = new JLabel("RAT kit status: available");
        userPanel.add(hasKitLabel);
        if (booking.hasKit()) {
            hasKitLabel.setText("RAT kit status: available");
            testButton.setEnabled(true);
        } else {
            hasKitLabel.setText("RAT kit status: not available [go to the nearest testing site to acquire RAT kit]");
        }
        return userPanel;
    }

    @Override
    /**
     * Get refresh button
     * @return refresh button
     */
    public JButton getButton() {
        return refresh;
    }
}
