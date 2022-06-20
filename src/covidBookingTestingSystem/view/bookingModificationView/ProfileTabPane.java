package CovidBookingTestingSystem.View.BookingModificationView;

import CovidBookingTestingSystem.Controller.BookingModificationController.BookingCaretaker;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

/**
 * Profile tab pane for resident to show onsite booking
 */
public class ProfileTabPane extends BookingModificationTabPane {
    private JButton activeBookingButton = new JButton("Active Bookings");
    private User user;

    /**
     * Constructor.
     */
    public ProfileTabPane(User user){
        this.user = user;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Profile"));

        // Construct UI Elements
        JPanel searchIdPane = new JPanel();
        idTextField = new JTextField(15);
        searchIdPane.add(new JLabel("Enter Booking ID:"));
        searchIdPane.add(idTextField);
        searchIdPane.add(searchIdButton);
        JPanel activeBookingPane = new JPanel();
        activeBookingPane.add(activeBookingButton);

        resultPane = new JPanel();
        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new TitledBorder("Result"));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(activeBookingPane);
        container.add(searchIdPane);

        // Add UI Elements to panel
        add(container,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }

    /**
     * Get active booking button
     * @return active booking button
     */
    public JButton getActiveBookingButton() {
        return activeBookingButton;
    }

    /**
     * Make individual site pane to be added to the result pane.
     * @param book book
     * @return the panel
     */
    protected JPanel makeBookingPane(Booking book){
        // Create UI
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new TitledBorder(""));

        // Adding Component
        userPanel.add(new JLabel("Patient's Name: " + book.getCustomerName()));
        userPanel.add(new JLabel("Booking ID: " + book.getId()));

        if (book.isCompleted()) {
            userPanel.add(new JLabel("Test already performed for this booking - can no longer be modified"));
        }
        else if (book.isElapsed()) {
            userPanel.add(new JLabel("Booking elapsed - can no longer be modified"));
        }
        else if (book.getStatus() == BookingStatus.CANCELLED) {
            userPanel.add(new JLabel("Booking cancelled - can no longer be modified"));
        }
        else {
            JButton cancelButton = new JButton("Cancel");
            JButton modifyButton = new JButton("Modify");

            // Cancel button logic
            userPanel.add(cancelButton);
            cancelButton.addActionListener(e -> {
                // Cancel booking in database
                try {
                    book.cancelBooking();
                    cancelButton.setEnabled(false);
                    modifyButton.setEnabled(false);
                    book.notify(BookingStatus.CANCELLED,null,user);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
            });

            // Modify button logic
            userPanel.add(modifyButton);
            modifyButton.addActionListener(e -> {
                new BookingCaretaker(book,user);
            });
        }

        return userPanel;
    }
}
