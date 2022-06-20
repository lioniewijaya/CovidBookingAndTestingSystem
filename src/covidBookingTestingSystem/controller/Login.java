package CovidBookingTestingSystem.Controller;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.View.LoginFrame;
import CovidBookingTestingSystem.View.MainView.MainFrame;
import CovidBookingTestingSystem.Model.JwtToken;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * Login for the COVID Testing Registration System for individuals.
 */
public class Login {
    private LoginFrame loginFrame = new LoginFrame(); // Login frame to be displayed to user

    /***
     * Constructor.
     */
    public Login() {
        loginFrame.getLoginButton().addActionListener(e -> {
            try {
                verifyLogin();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });
    }

    /***
     * Verify if user's login credential is valid, if credential is valid and JWT verification is successful this will lead user to the main
     * functionality of the system else user is prompted to try again.
     */
    private void verifyLogin() throws IOException, InterruptedException, ParseException {
        // Check if login credential is valid
        String usersLoginUrl = "/user/login?jwt=true";
        String jsonLoginString = "{" +
                "\"userName\":\"" + loginFrame.getUsername() + "\"," +
                "\"password\":\"" + loginFrame.getPassword() + "\"" +
                "}";

        Connection connection= new Connection();
        HttpResponse response = connection.postRequest(usersLoginUrl,jsonLoginString);

        if (response.statusCode() != 200) {
            loginFrame.showLoginFailMessage("Login fail - try again");
            return;
        }

        // Correct credential, JWT is returned to do user's authentication and authorization
        JSONParser parser = new JSONParser();
        JSONObject jwtTokenJson = (JSONObject) parser.parse(response.body().toString());
        JwtToken jwtToken = new JwtToken(jwtTokenJson);

        if (jwtToken.verifyJwt() == false) {
            loginFrame.showLoginFailMessage("Token verification failed");
            return;
        }

        // Successful JWT verification, user can now proceed to login
        loginFrame.showLoginFailMessage("");
        loginFrame.setUsername("");
        loginFrame.setPassword("");
        loginFrame.setVisible(false);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            loginFrame.setVisible(true);
        });

        new MainFrame(new User(jwtToken.getUserIdFromJwt()),logoutButton);
    }
}
