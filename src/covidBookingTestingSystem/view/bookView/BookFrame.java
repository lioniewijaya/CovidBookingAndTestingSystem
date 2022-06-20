package CovidBookingTestingSystem.View.BookView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Book frame to be displayed during the booking process.
 */
public class BookFrame extends JFrame{

    /***
     * Constructor.
     */
     public BookFrame() {
        setTitle("COVID Booking and Testing System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(500,500));
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);
    }

    /***
     * Set a customized booking pane inside this booking frame.
     * @param bookPane booking pane
     */
    public void setPane(BookPane bookPane) {
        add(bookPane,BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
