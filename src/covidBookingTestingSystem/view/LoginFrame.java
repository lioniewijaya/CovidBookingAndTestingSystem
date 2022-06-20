package CovidBookingTestingSystem.View;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Login frame to be displayed during the login process.
 */
public class LoginFrame extends JFrame{
    private JPanel loginPane;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JLabel loginMsg;

    /***
     * Constructor.
     */
    public LoginFrame() {
        setTitle("COVID Booking and Testing System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,800));
        loginPane = new JPanel(new GridBagLayout());

        loginPane.setLayout(new GridBagLayout());
        loginPane.setBorder(new TitledBorder("Login"));

        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");
        usernameTextField = new JTextField(10);
        passwordTextField = new JPasswordField(10);
        loginButton = new JButton("Login");
        loginMsg = new JLabel("");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPane.add(usernameLabel, gbc);
        gbc.gridy++;
        loginPane.add(passwordLabel, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        loginPane.add(usernameTextField, gbc);
        gbc.gridy++;
        loginPane.add(passwordTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginPane.add(loginButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        loginPane.add(loginMsg,gbc);

        add(loginPane);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Get user's username input
     * @return username inputted
     */
    public String getUsername() {
        return usernameTextField.getText();
    }

    /**
     * Get user's password input
     * @return password inputted
     */
    public String getPassword() {
        return String.valueOf(passwordTextField.getPassword());
    }

    /**
     * Get login button
     * @return login button
     */
    public JButton getLoginButton() {
        return loginButton;
    }

    /**
     * Set username text field
     * @param usernameTextField username text field to be set
     */
    public void setUsername(String usernameTextField) {
        this.usernameTextField.setText(usernameTextField);
    }

    /**
     * Set password text field
     * @param passwordTextField password text field to be set
     */
    public void setPassword(String passwordTextField) {
        this.passwordTextField.setText(passwordTextField);
    }

    /**
     * Set login message after a user tries to login
     * @param msg login message to be set
     */
    public void showLoginFailMessage(String msg) {
        loginMsg.setText(msg);
    }
}