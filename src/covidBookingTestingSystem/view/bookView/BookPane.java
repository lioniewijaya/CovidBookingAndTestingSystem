package CovidBookingTestingSystem.View.BookView;

import javax.swing.*;
import de.wannawork.jcalendar.*;

import java.time.Instant;
import java.util.Calendar;

/**
 * Book pane to be put inside book frame.
 */
public abstract class BookPane extends JPanel {
    protected JButton confirmButton = new JButton("confirm");
    protected JLabel userInfo;
    protected JLabel userIdLabel;
    protected JTextField userIdTextField;
    protected JButton checkButton = new JButton("search");
    protected JCalendarPanel calendar = new JCalendarPanel();

    /**
     * Get confirm button to confirm booking.
     * @return confirm button
     */
    public JButton getConfirmButton() {
        return confirmButton;
    }

    /***
     * Set user information or search result and whether or not to display confirm button.
     * @param txt text to show
     * @param canConfirm valid ID found and booking can be confirmed
     */
    public void setUserInfo(String txt, boolean canConfirm) {
        userInfo.setText(txt);
        if (canConfirm) {
            confirmButton.setVisible(true);
        }
        else {
            confirmButton.setVisible(false);
        }
    }

    /***
     * Get user's input for user ID.
     * @return user ID inputted.
     */
    public String getUserId() {
        return userIdTextField.getText();
    }

    /***
     * Get check button to check user's ID.
     * @return check button
     */
    public JButton getCheckButton() {
        return checkButton;
    }

    /***
     * Get date selected by user. Hour and minutes are set to the end of that date.
     * @return date
     */
    public Instant getDateSelected() {
        Calendar dateSelected = calendar.getCalendar();
        dateSelected.set(Calendar.HOUR_OF_DAY, 23);
        dateSelected.set(Calendar.MINUTE, 59);
        dateSelected.set(Calendar.SECOND, 59);
        dateSelected.set(Calendar.MILLISECOND, 999);
        return dateSelected.toInstant();
    }
}
