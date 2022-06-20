package CovidBookingTestingSystem.Model.UserModel;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.Observer;
import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;

/**
 * User (possibly patient) in the system. A user may have multiple different roles (customer, receptionist,
 * or administerer). Our implementation focuses on one specific role only, so GUI and functionality will be
 * limited to one of the roles user has.
 */
public class User implements Observer {
    private String id;                                  // User's ID
    private String givenName;                           // User's given name
    private String familyName;                          // User's family name
    private String phoneNumber;                         // User's phone number
    private boolean isCustomer;                         // Boolean indicating if user is a customer
    private boolean isReceptionist;                     // Boolean indicating if user is a receptionist
    private boolean isHealthcareWorker;                 // Boolean indicating if user is a health care worker
    private String testingSiteId;                       // User's working place

    private Role role;                                  // User's primary role

    private Connection connection = new Connection();   // Connection to web service

    /***
     * Constructor.
     * @param userJson user in form of JSON object
     */
    public User(JSONObject userJson)  {
        destructureJson(userJson);
    }

    /***
     * Constructor.
     * @param userId user's ID.
     */
    public User(String userId) throws IOException, InterruptedException, ParseException {
        String usersIdUrl = "/user/" + userId;

        JSONParser parser = new JSONParser();
        String userJsonString = connection.getRequest(usersIdUrl).body().toString();
        JSONObject userJson = (JSONObject) parser.parse(userJsonString);

        destructureJson(userJson);
    }

    /***
     * Destructure user JSON object into attributes.
     * @param userJson user JSON object
     */
    private void destructureJson(JSONObject userJson) {
        id = (String) userJson.get("id");
        givenName = (String) userJson.get("givenName");
        familyName = (String) userJson.get("familyName");
        phoneNumber = (String) userJson.get("phoneNumber");
        isCustomer = (boolean) userJson.get("isCustomer");
        isReceptionist = (boolean) userJson.get("isReceptionist");
        isHealthcareWorker = (boolean) userJson.get("isHealthcareWorker");
        Object additionalInfo = ((JSONObject) userJson).get("additionalInfo");

        if (isHealthcareWorker) {
            role = new HealthcareWorker();
        } else if (isReceptionist) {
            role = new Receptionist();
        } else if (isCustomer) {
            role = new Customer();
        }

        try {
            testingSiteId = (String) ((JSONObject) additionalInfo).get("siteId");
        } catch (Exception e){
        }
    }

    /***
     * Get user's full name.
     * @return user's full name
     */
    public String getFullName() {
        return givenName + " " + familyName;
    }

    /***
     * Get user's ID.
     * @return user's ID
     */
    public String getId() {
        return id;
    }

    /***
     * Get user's primary role.
     * @return user's role
     */
    public Role getRole() {
        return role;
    }

    /***
     * Get user's brief description.
     * @return user's description
     */
    public String description() {
        return getFullName() + " | " + phoneNumber + "|" + role.roleString();
    }

    /***
     * Check if a user ID exists.
     */
    public boolean checkUserIdExist(String userId) throws IOException, InterruptedException {
        String usersIdUrl = "/user/" + userId;
        return connection.getRequest(usersIdUrl).statusCode() == 200;
    }

    /***
     * Get user's working place
     * @return user's working place
     */
    public String getTestingSiteId(){
        return testingSiteId;
    }

    /***
     * Update the database with the new notification
     * @param command the actions
     */
    public void setNotification(BookingStatus command, User user, String bookingId) throws IOException, InterruptedException, ParseException {
        // Remake the previous notifications in JSON String
        ArrayList<String> notifications = getNotification();
        String notifJson = "";
        for (String notif: notifications){
            notifJson += "\"" + notif +"\",";
        }
        // Set notification to additional info
        String patchUserUrl = "/user/" + this.getId();
        String userString = "{\"additionalInfo\":" + "{" +
                "\"siteId\":\"" + testingSiteId + "\"," +
                "\"notifications\":" + "[" + notifJson +
                "\"" + user.getFullName() + " " + command.toString() + " " + "booking: "+bookingId+" at "+ Instant.now() +"." + "\"]}}";
        connection.patchRequest(patchUserUrl,userString).body();
    }

    /***
     * Get notifications from database.
     * @return notifications
     */
    @Override
    public ArrayList<String> getNotification() throws IOException, InterruptedException, ParseException {
        String userUrl = "/user";
        ArrayList<String> notifications = new ArrayList<>();

        HttpResponse response = connection.getRequest(userUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String usersJsonString = response.body().toString();
            JSONArray usersJson = (JSONArray) parser.parse(usersJsonString);
            for (Object userJson: usersJson) {
                Object additionalInfo = ((JSONObject) userJson).get("additionalInfo");
                if (((JSONObject) userJson).get("id").toString().equals(this.id)) {
                    try {
                        String notifJson = ((JSONObject) additionalInfo).get("notifications").toString();
                        JSONArray notifs = (JSONArray) parser.parse(notifJson);
                        notifications = notifs;
                    } catch (Exception e) {
                    }
                }
            }
        }

        return notifications;
    }

    /***
     * Update the database to delete notifications
     */
    public void deleteNotification() throws IOException, InterruptedException, ParseException {

        String userUrl = "/user";
        String siteId = "";
        HttpResponse response = connection.getRequest(userUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String usersJsonString = response.body().toString();
            JSONArray usersJson = (JSONArray) parser.parse(usersJsonString);
            for (Object userJson: usersJson) {
                Object additionalInfo = ((JSONObject) userJson).get("additionalInfo");
                if (((JSONObject) userJson).get("id").toString().equals(this.id)) {
                    try {
                        siteId = ((JSONObject) additionalInfo).get("siteId").toString();
                    } catch (Exception e) {
                    }
                }
            }
        }

        // Set notification to additional info
        String patchUserUrl = "/user/" + this.getId();
        String userString = "{\"additionalInfo\":" + "{" +
                "\"siteId\":" + "\"" + siteId + "\", " +
                "\"notifications\":" + "[" + "]}}";
        connection.patchRequest(patchUserUrl,userString).body();
    }

}
