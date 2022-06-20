package CovidBookingTestingSystem.View.BookView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Home booking pane to be put inside booking frame.
 */
public class HomeBookPane extends BookPane {
    private JLabel currentUserLabel;
    private JCheckBox currentUserCheckbox;
    private JLabel haveKitLabel;
    private JCheckBox haveKitCheckbox;

    /***
     * Constructor.
     */
    public HomeBookPane() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Home Book"));

        userInfo = new JLabel("User found");
        haveKitLabel = new JLabel("Patient has own RAT kit");
        haveKitCheckbox = new JCheckBox();
        currentUserLabel = new JLabel("I am the patient");
        currentUserCheckbox = new JCheckBox("",true);
        confirmButton = new JButton("confirm");
        userIdLabel = new JLabel("Enter patient's ID:");
        userIdTextField = new JTextField();
        checkButton = new JButton("search");

        JPanel haveKit = new JPanel();
        haveKit.add(haveKitCheckbox);
        haveKit.add(haveKitLabel);

        JPanel searchId = new JPanel();
        searchId.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchId.add(userIdLabel, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        searchId.add(userIdTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        searchId.add(checkButton, gbc);

        JPanel currentUser = new JPanel();
        currentUser.add(currentUserCheckbox);
        currentUser.add(currentUserLabel);

        JPanel userContainer = new JPanel();
        userContainer.setLayout(new BorderLayout());
        userContainer.add(currentUser,BorderLayout.NORTH);
        userContainer.add(searchId,BorderLayout.CENTER);
        userContainer.add(userInfo,BorderLayout.SOUTH);

        add(haveKit,BorderLayout.NORTH);
        add(userContainer,BorderLayout.WEST);
        add(calendar,BorderLayout.EAST);
        add(confirmButton,BorderLayout.SOUTH);
        confirmButton.setVisible(true);
    }

    /***
     * Check whether user has kit.
     * @return boolean to indicate user has kit
     */
    public boolean haveKit() {
        return haveKitCheckbox.isSelected();
    }

    /***
     * Check whether user is the patient.
     */
    public boolean userIsPatient() {
        return currentUserCheckbox.isSelected();
    }

    /***
     * Get check box to check if user select themselves as patient.
     * @return checkbox
     */
    public JCheckBox getCurrentUserCheckbox() {
        return currentUserCheckbox;
    }

    /***
     * Display search bar and reset text field based on boolean.
     * @param display boolean value
     */
    public void displaySearch(boolean display) {
        checkButton.setEnabled(display);
        userIdTextField.setEditable(display);
    }
}
