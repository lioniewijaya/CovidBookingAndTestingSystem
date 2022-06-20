package CovidBookingTestingSystem.View;

import CovidBookingTestingSystem.Model.BookingModel.Booking;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Scanner tab pane to be displayed in the main frame during the scanning process.
 */
public class ScannerTabPane extends JPanel {
    private JPanel searchPinPane;
    private JTextField qrCodeTextField;
    private JPanel resultPane;
    private JButton searchButton = new JButton("Search");
    private JButton scanButton = new JButton("Scan to receive RAT kit");

    /***
     * Constructor.
     */
    public ScannerTabPane(){
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("ScannerTabPane"));

        searchPinPane = new JPanel();
        qrCodeTextField = new JTextField(20);
        searchPinPane.add(qrCodeTextField);
        searchPinPane.add(searchButton);

        resultPane = new JPanel();
        resultPane.setBorder(new TitledBorder("Result"));
        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(new JLabel("Enter QR Code: "));
        container.add(searchPinPane);

        add(container,BorderLayout.NORTH);
        add(resultPane,BorderLayout.CENTER);
    }

    /***
     * Get QR code inputted.
     * @return QR code string
     */
    public String getQrCodeInput() {
        return qrCodeTextField.getText();
    }

    /***
     * Get search QR code's button.
     * @return search button
     */
    public JButton getSearchButton() {
        return searchButton;
    }

    /***
     * Get scan QR code's button.
     * @return scan button
     */
    public JButton getScanButton() {
        return scanButton;
    }

    /***
     * Make a booking pane with the given QR code to be added to the result pane.
     * @param booking booking
     */
    private JPanel makeBookingPane(Booking booking){
        // Create UI
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(new TitledBorder(""));

        if (booking.hasKit()) {
            scanButton.setEnabled(false);
            scanButton.setText("RAT kit acquired.");
        } else {
            scanButton.setEnabled(true);
            scanButton.setText("Scan to receive RAT kit.");
        }
        // Adding Component
        userPanel.add(new JLabel("Patient's Name: " + booking.getCustomerName()));
        userPanel.add(scanButton);

        return userPanel;
    }

    /***
     * Update result pane from search.
     * @param validQrCode boolean to indicate if QR code is valid
     * @param booking booking
     */
    public void updateResult(boolean validQrCode, Booking booking) {
        // Clear list and insert booking
        resultPane.removeAll();

        if  (validQrCode) {
            resultPane.add(makeBookingPane(booking));
        }
        else {
            resultPane.add(new JLabel("Invalid QR code."));
        }
    }
}
