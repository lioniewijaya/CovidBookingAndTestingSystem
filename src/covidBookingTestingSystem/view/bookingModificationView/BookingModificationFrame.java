package CovidBookingTestingSystem.View.BookingModificationView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;

import de.wannawork.jcalendar.*;

/**
 * Booking modification frame to be displayed during the booking modification process.
 */
public class BookingModificationFrame extends JFrame{
    private JPanel bookingModificationPane;
    private JTextField siteIdInput;
    private JCalendarPanel calendarInput;
    private JLabel bookingId;
    private JLabel testingSiteId;
    private JLabel dateSelected;
    private JButton modifyButton = new JButton("Modify booking");
    private JButton undoButton = new JButton("Undo booking");
    private JLabel latestStatus = new JLabel();

    /***
     * Constructor.
     */
    public BookingModificationFrame() {
        setTitle("COVID Booking and Testing System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500,500));
        bookingModificationPane = new JPanel(new BorderLayout());

        bookingModificationPane.setBorder(new TitledBorder("Booking Modification"));

        JPanel modificationPane = new JPanel();
        modificationPane.setBorder(new EmptyBorder(20,40,20,40));
        modificationPane.setLayout(new BoxLayout(modificationPane,BoxLayout.Y_AXIS));

        JLabel siteIdLabel = new JLabel("Enter testing site ID",JLabel.CENTER);
        siteIdInput = new JTextField();
        JLabel calendarLabel = new JLabel("Select date",JLabel.CENTER);
        calendarInput = new JCalendarPanel();

        modificationPane.add(siteIdLabel);
        modificationPane.add(siteIdInput);
        modificationPane.add(calendarLabel);
        modificationPane.add(calendarInput);
        modificationPane.add(latestStatus);

        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer,BoxLayout.X_AXIS));
        buttonContainer.add(Box.createHorizontalGlue());
        buttonContainer.add(modifyButton);
        buttonContainer.add(undoButton);
        buttonContainer.add(Box.createHorizontalGlue());

        bookingId = new JLabel("Booking ID: ",JLabel.CENTER);
        testingSiteId = new JLabel("Testing site ID: ",JLabel.CENTER);
        dateSelected = new JLabel("Date: ",JLabel.CENTER);

        JPanel bookingInfo = new JPanel(new BorderLayout());
        bookingInfo.setBorder(new TitledBorder("Last booking state"));
        bookingInfo.add(bookingId,BorderLayout.NORTH);
        bookingInfo.add(testingSiteId,BorderLayout.CENTER);
        bookingInfo.add(dateSelected,BorderLayout.SOUTH);

        bookingModificationPane.add(modificationPane,BorderLayout.NORTH);
        bookingModificationPane.add(buttonContainer,BorderLayout.CENTER);
        bookingModificationPane.add(bookingInfo,BorderLayout.SOUTH);

        add(bookingModificationPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /***
     * Set testing site ID input.
     * @param siteIdInput  testing site ID
     */
    public void setSiteIdInput(String siteIdInput) {
        this.siteIdInput.setText(siteIdInput);
    }

    /***
     * Set calendar input.
     * @param calendarInput calendar input.
     */
    public void setCalendarInput(Instant calendarInput) {
        this.calendarInput.setDate(Date.from(calendarInput));
    }

    /***
     * Set latest modification status.
     * @param status status
     */
    public void setLatestStatus(String status) {
        latestStatus.setText(status);
    }

    /***
     * Get modify button.
     * @return modify button
     */
    public JButton getModifyButton() {
        return modifyButton;
    }

    /***
     * Get undo button.
     * @return undo button
     */
    public JButton getUndoButton() {
        return undoButton;
    }

    /***
     * Get testing site ID input.
     * @return testing site ID
     */
    public String getTestingSiteId() {
        return siteIdInput.getText();
    }

    /***
     * Get date selected for testing.
     * @return date for testing
     */
    public Instant getDateSelected() {
        Calendar dateSelected = calendarInput.getCalendar();
        dateSelected.set(Calendar.HOUR_OF_DAY, 23);
        dateSelected.set(Calendar.MINUTE, 59);
        dateSelected.set(Calendar.SECOND, 59);
        dateSelected.set(Calendar.MILLISECOND, 999);
        return dateSelected.toInstant();
    }

    /***
     * Update state view of ID, site ID, and date selected.
     * @param bookingId booking ID
     * @param testingSiteId site ID
     * @param dateSelected date
     */
    public void updateStateView(String bookingId, String testingSiteId, Instant dateSelected) {
        this.bookingId.setText("Booking ID: " + bookingId);
        this.testingSiteId.setText("Testing site ID: " + testingSiteId);
        setSiteIdInput(testingSiteId);
        this.dateSelected.setText("Date: " + dateSelected);
        setCalendarInput(dateSelected);
    }
}
