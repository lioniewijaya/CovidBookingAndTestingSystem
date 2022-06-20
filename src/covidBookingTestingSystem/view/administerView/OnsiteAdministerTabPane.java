package CovidBookingTestingSystem.View.AdministerView;

import CovidBookingTestingSystem.Controller.AdministerController.OnSiteAdminister;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.UserModel.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Onsite administer tab pane for onsite testing for the COVID Testing Registration System.
 */
public class OnsiteAdministerTabPane extends AdministerTabPane {
    private JPanel searchPinPane;
    private JTextField pinTextField;
    private JButton searchButton = new JButton("Search");
    private User administerer;

    /**
     * Constructor.
     */
    public OnsiteAdministerTabPane(User administerer){
        this.administerer = administerer;

        setLayout(new BorderLayout());
        setBorder(new TitledBorder("OnsiteAdministerTabPane"));

        // Construct UI Elements
        searchPinPane = new JPanel();
        pinTextField = new JTextField(20);
        searchPinPane.add(pinTextField);
        searchPinPane.add(searchButton);

        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new TitledBorder("Result"));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(new JLabel("Enter PIN Code: "));
        container.add(searchPinPane);

        // Add UI Elements to panel
        add(container,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }

    @Override
    public JButton getButton() {
        return searchButton;
    }

    /**
     * Get pin input
     * @return the pin Input
     */
    public String getPinInput() {
        return pinTextField.getText();
    }

    @Override
    /**
     * Make individual site pane to be added to the result pane.
     * @param book book
     * @return the panel
     */
    public JPanel makeBookingPane(Booking book){
        // Create UI
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new TitledBorder(""));

        // Adding Component
        userPanel.add(new JLabel("Patient's Name: " + book.getCustomerName()));
        userPanel.add(new JLabel("Testing Site: " + book.getTestingSiteName()));
        userPanel.add(new JLabel("Start Time: " + book.getStartTime()));
        userPanel.add(new JLabel("Additional Info: " + book.getAdditionalInfo()));

        // Test button logic
        JButton testButton = new JButton("Test");
        userPanel.add(testButton);
        testButton.addActionListener(e -> {
            new OnSiteAdminister(new OnSiteAdministerPane(), book, testButton, administerer);
        });

        return userPanel;
    }
}
