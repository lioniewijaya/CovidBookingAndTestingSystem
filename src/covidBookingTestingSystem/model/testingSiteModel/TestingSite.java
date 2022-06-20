package CovidBookingTestingSystem.Model.TestingSiteModel;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Testing site to test for COVID. Each testing site have different facilities provided (e.g. Drive Through, Walk-in, Clinics, GPs or Hospitals), it has information
 * whether a facility is open or close as well as waiting time. Not all testing site provide onsite booking.
 */
public class TestingSite {
    private String id;                                          // Testing site's ID
    private String name;                                        // Testing site's name
    private String description;                                 // Testing site's description
    private String websiteUrl;                                  // Testing site's website URL
    private JSONObject address;                                 // Testing site's address
    private JSONArray bookings;                                 // Testing site's booking
    private JSONObject additionalInfo;                          // Testing site's additional info
    private ArrayList<Facility> facilities = new ArrayList<>(); // Facilities available
    private boolean allowOnsiteBooking;                         // Testing site's allow on site booking status
    private List<Integer> openingHours;                         // Testing site's opening hour
    private List<Integer> closingHours;                         // Testing site's closing hour
    private ArrayList<User> admins = new ArrayList<>();         // Testing site's admins or workers

    /***
     * Constructor.
     * @param siteJSON Testing site in form of JSON object
     */
    public TestingSite(JSONObject siteJSON) throws InterruptedException, ParseException, IOException {
        destructureJson(siteJSON);
    }

    /***
     * Constructor.
     * @param testingSiteId testing site's ID.
     */
    public TestingSite(String testingSiteId) throws IOException, InterruptedException, ParseException {
        String sitesIdUrl = "/testing-site/" + testingSiteId;

        JSONParser parser = new JSONParser();
        Connection connection = new Connection();
        String siteJsonString = connection.getRequest(sitesIdUrl).body().toString();
        JSONObject siteJSON = (JSONObject) parser.parse(siteJsonString);

        destructureJson(siteJSON);
    }

    /***
     * Destructure site JSON object into attributes.
     * @param siteJSON site JSON object
     */
    private void destructureJson(JSONObject siteJSON) throws InterruptedException, ParseException, IOException {
        id = (String) siteJSON.get("id");
        name = (String) siteJSON.get("name");
        description = (String) siteJSON.get("description");
        websiteUrl = (String) siteJSON.get("websiteUrl");
        address = (JSONObject) siteJSON.get("address");
        bookings = (JSONArray) siteJSON.get("bookings");
        additionalInfo = (JSONObject) siteJSON.get("additionalInfo");
        allowOnsiteBooking = getAdditionalValue("allowOnSiteBooking");

        for (Object key: additionalInfo.keySet()) {
            if (Facility.getFacility((String) key) != null && getAdditionalValue((String) key)) {
                facilities.add(Facility.getFacility((String) key));
            }
        }

        String openingHoursString = "0:0";
        String closingHoursString = "23:59";
        if (additionalInfo.get("openingHours") != null) {
            openingHoursString = additionalInfo.get("openingHours").toString();
        }
        if (additionalInfo.get("closingHours") != null) {
            closingHoursString = additionalInfo.get("closingHours").toString();
        }
        openingHours = Arrays.asList(openingHoursString.split(":")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        closingHours = Arrays.asList(closingHoursString.split(":")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        updateAdmins();
    }

    /***
     * Check if testing site is open.
     * @return boolean indicating if testing site is open
     */
    public boolean isOpen() {
        Instant open = OffsetDateTime.now( ZoneOffset.UTC )
                .with( LocalTime.of(openingHours.get(0), openingHours.get(1)))
                .toInstant()
                ;
        Instant close = OffsetDateTime.now( ZoneOffset.UTC )
                .with( LocalTime.of( closingHours.get(0),closingHours.get(1)))
                .toInstant()
                ;
        return Instant.now().isAfter(open) && Instant.now().isBefore(close);
    }

    /***
     * Get site's ID.
     * @return site's ID
     */
    public String getId() {
        return id;
    }

    /***
     * Get site's name.
     * @return site's name
     */
    public String getName() {
        return name;
    }

    /***
     * Get site's description.
     * @return site's description
     */
    public String getDescription() {
        return description;
    }

    /***
     * Get site's website URL.
     * @return site's website URL
     */
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    /***
     * Get site's address.
     * @return site's address
     */
    public JSONObject getAddress() {
        return address;
    }

    /***
     * Get site's part address.
     * @param part part of address
     * @return site's part address
     */
    public String getPartAddress(String part) {
        return (String) address.get(part);
    }

    /***
     * Get site's available facilities.
     * @return a list of site's facilities
     */
    public ArrayList<Facility> getFacilities() {
        return facilities;
    }

    /***
     * Get site's opening hour in UTC separated by semicolon.
     * @return site's opening hour.
     */
    public String getOpeningHours() {
        return String.join(":", Arrays.asList(openingHours.toString().replaceAll("\\[(.*)\\]", "$1").split(", ")));
    }

    /***
     * Get site's closing hour in UTC separated by semicolon.
     * @return site's closing hour
     */
    public String getClosingHours() {
        return String.join(":", Arrays.asList(closingHours.toString().replaceAll("\\[(.*)\\]", "$1").split(", ")));
    }

    /***
     * Get site's waiting time assuming each pending booking takes an hour.
     * @return site's waiting time
     */
    public int getWaitingHours() {
        int hours = 0;
        for (Object bookingJson : bookings) {
            JSONObject booking = (JSONObject) bookingJson;
            JSONArray covidTest = (JSONArray) booking.get("covidTests");
            if (covidTest.toString().equals("[]")) {
                hours += 1;
            }
        }
        return hours;
    }

    /***
     * Get site's additional info.
     * @return site's additional info
     */
    public JSONObject getAdditionalInfo() {
        return additionalInfo;
    }

    /***
     * Get site's additional value in additional info in boolean form.
     * @return site's additional value
     */
    public boolean getAdditionalValue(String key) {
        if (additionalInfo.containsKey(key)) {
            return (boolean) additionalInfo.get(key);
        }
        return false;
    }

    /***
     * Check if a facility exist in site.
     * @return boolean indicating if facility exist
     */
    public boolean checkFacility(Facility facility) {
        return facilities.contains(facility);
    }

    /***
     * Check if a facility allows on site booking.
     * @return boolean indicating if on site booking is allowed
     */
    public boolean checkAllowOnsiteBooking() {
        return allowOnsiteBooking;
    }

    /***
     * Get site's admins/workers.
     * @return a list of site's admins/workers
     */
    public ArrayList<User> getAdmins() {
        return admins;
    }

    /***
     * Set admins to get latest update on all admins related to testing sites.
     */
    public void updateAdmins() throws IOException, InterruptedException, ParseException {
        admins.clear();

        // Get all admins
        String userUrl = "/user";
        ArrayList<User> users = new ArrayList<>();

        Connection connection = new Connection();
        HttpResponse response = connection.getRequest(userUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String usersJsonString = response.body().toString();
            JSONArray usersJson = (JSONArray) parser.parse(usersJsonString);
            for (Object userJson: usersJson) {
                users.add(new User((JSONObject) userJson));
            }
        }

        for (User user : users) {
            String testingSiteWorker = user.getTestingSiteId();
            // Filter though them
            if (getId().equals(testingSiteWorker)) {
                admins.add(user);
            }
        }

    }
}
