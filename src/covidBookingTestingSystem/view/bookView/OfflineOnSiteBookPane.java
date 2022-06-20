package CovidBookingTestingSystem.View.BookView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Offline booking for onsite testing pane to be put inside booking frame.
 */
public class OfflineOnSiteBookPane extends BookPane {

    /***
     * Constructor.
     */
    public OfflineOnSiteBookPane() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("On-site booking for On-site Testing"));

        userInfo = new JLabel("");

        // Enter user info by ID
        userIdLabel = new JLabel("Enter patient's ID: ");
        userIdTextField = new JTextField(10);

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(userIdLabel, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        container.add(userIdTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        container.add(checkButton, gbc);

        add(container,BorderLayout.NORTH);

        userInfo = new JLabel("");
        JPanel userContainer = new JPanel();
        userContainer.setLayout(new BorderLayout());
        userContainer.add(container,BorderLayout.NORTH);
        userContainer.add(userInfo,BorderLayout.SOUTH);

        add(userContainer,BorderLayout.WEST);
        add(calendar,BorderLayout.EAST);
        add(confirmButton,BorderLayout.SOUTH);
        confirmButton.setVisible(false);
    }
}
