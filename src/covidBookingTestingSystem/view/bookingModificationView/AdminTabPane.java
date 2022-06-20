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
import java.util.ArrayList;

/**
 * Admin tab pane for Receptionist for the COVID Testing Registration System.
 */
public class AdminTabPane extends BookingModificationTabPane {
    private JPanel searchPinPane;
    private JTextField pinTextField;
    private JButton searchPinButton = new JButton("Search Pin");
    private JPanel notificationContainer;
    private JButton clearAll = new JButton("Clear All");
    private JButton refreshNotif = new JButton("Refresh");
    private User user;

    /**
     * Constructor.
     */
    public AdminTabPane(User user){
        this.user = user;
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("AdminTabPane"));

        // Construct UI Elements
        searchPinPane = new JPanel();
        pinTextField = new JTextField(15);
        searchPinPane.add(new JLabel("Enter PIN Code: "));
        searchPinPane.add(pinTextField);
        searchPinPane.add(searchPinButton);

        searchIdPane = new JPanel();
        idTextField = new JTextField(15);
        searchPinPane.add(new JLabel("Enter Booking ID: "));
        searchPinPane.add(idTextField);
        searchPinPane.add(searchIdButton);

        resultPane = new JPanel();
        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new TitledBorder("Result"));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(searchPinPane);
        container.add(searchIdPane);

        notificationContainer = new JPanel();
        notificationContainer.setLayout(new BoxLayout(notificationContainer,BoxLayout.Y_AXIS));
        JScrollPane scrollNotifPane = new JScrollPane();
        scrollNotifPane.setViewportView(notificationContainer);
        scrollNotifPane.setPreferredSize(new Dimension(200, 100));

        JPanel btnPane = new JPanel();
        btnPane.add(clearAll);
        btnPane.add(refreshNotif);

        JPanel notifPane = new JPanel();
        notifPane.setBorder(new TitledBorder("Notification"));
        notifPane.setLayout(new BorderLayout());
        notifPane.add(btnPane,BorderLayout.NORTH);
        notifPane.add(scrollNotifPane,BorderLayout.CENTER);

        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane,BoxLayout.Y_AXIS));
        topPane.add(container);
        topPane.add(notifPane);

        // Add UI Elements to panel
        add(topPane,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }

    /***
     * Get search pin button
     * @return search pin button
     */
    public JButton getSearchPinButton() {
        return searchPinButton;
    }

    /**
     * Get pin input
     * @return the pin Input
     */
    public String getPinInput() {
        return pinTextField.getText();
    }

    /***
     * Get clear all notification button
     * @return clear all notification button
     */
    public JButton getClearNotifButton() {
        return clearAll;
    }

    /***
     * Get refresh notification button
     * @return refresh notification button
     */
    public JButton getRefreshNotifButton() {
        return refreshNotif;
    }

    /***
     * Get user
     * @return user
     */
    public User getUser() {
        return user;
    }

    /***
     * Get notification panel
     * @return notification panel
     */
    public JPanel getNotificationContainer() {
        return notificationContainer;
    }

    /**
     * Make individual notification to be added to the notification pane.
     * @param notification notification
     * @return the panel
     */
    public JPanel makeNotificationPane(String notification){
        // Create UI
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new TitledBorder(""));
        // Adding Component
        userPanel.add(new JLabel(notification));

        return userPanel;
    }

    /**
     * Update notification pane.
     * @param notifications a list of notifications
     */
    public void updateNotification(ArrayList<String> notifications) {
        // clear list and insert site
        notificationContainer.removeAll();
        for (String notification : notifications){
            notificationContainer.add(makeNotificationPane(notification));
        }
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
            JButton deleteButton = new JButton("Delete");
            JButton modifyButton = new JButton("Modify");

            // Cancel button logic
            userPanel.add(cancelButton);
            cancelButton.addActionListener(e -> {
                // Cancel booking in database
                try {
                    book.cancelBooking();
                    cancelButton.setEnabled(false);
                    deleteButton.setEnabled(false);
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

            // Delete button logic
            userPanel.add(deleteButton);
            deleteButton.addActionListener(e -> {
                // Delete booking in database and view
                try {
                    book.deleteFromDatabase();
                    cancelButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    modifyButton.setEnabled(false);
                    resultPane.remove(userPanel);
                    book.notify(BookingStatus.DELETED,null,user);
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
