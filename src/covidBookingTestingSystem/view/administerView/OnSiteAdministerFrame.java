package CovidBookingTestingSystem.View.AdministerView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Administer frame to be displayed during the COVID booking and testing system, it is customized
 * accordingly to the functionality allowed for each type of booking.
 */
public class OnSiteAdministerFrame extends JFrame{

    /**
     * Constructor.
     */
    public OnSiteAdministerFrame(){
        setTitle("COVID Booking and Testing System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(800,800));
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);
    }

    /**
     *
     * @param administerPane display for administer
     */
    public void setPane(OnSiteAdministerPane administerPane) {
        add(administerPane,BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
