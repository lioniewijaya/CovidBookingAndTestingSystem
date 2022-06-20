package CovidBookingTestingSystem.View.MainView;

import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

/**
 * Main frame to be displayed during the COVID booking and testing system, it is customized
 * accordingly to the user and their role as well as functionality allowed.
 */
public class MainFrame extends JFrame{

    /***
     * Constructor.
     * @param user user currently logged in
     * @param logoutButton log out button to allow going back to login process
     */
    public MainFrame(User user, JButton logoutButton) throws ParseException, IOException, InterruptedException {
        setTitle("COVID Booking and Testing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000,800));

        // Display user's identity, role, and logout button
        JPanel header = new JPanel();
        JLabel title = new JLabel("Welcome, " + user.getFullName() + "! [" + user.getRole().roleString() + "]");
        title.setFont(new Font("Times", Font.BOLD, 20));
        logoutButton.addActionListener(e -> {
            dispose();
        });

        header.setLayout(new BorderLayout());
        header.add(title,BorderLayout.CENTER);
        header.add(logoutButton,BorderLayout.EAST);

        // Menu tab customized based on user's role
        JTabbedPane menuTab = user.getRole().roleTab().getMenuTab(user);

        // Combining everything into the main frame to display
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(BorderLayout.NORTH,header);
        add(BorderLayout.CENTER,menuTab);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

