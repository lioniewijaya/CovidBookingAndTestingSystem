package CovidBookingTestingSystem.Model.BookingModel;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.Observable;
import CovidBookingTestingSystem.Model.TestModel.CovidTest;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSiteCollection;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.ArrayList;

/**
 * Booking done for the COVID Testing Registration System.
 */
public class Booking implements Observable {
    private TestingSite testingSite;                                // Booking's testings site
    private User customer;                                          // Booking's customer
    private String id;                                              // Booking's ID
    private String smsPin;                                          // Booking's PIN
    private Instant startTime;                                      // Booking's time
    private JSONObject jsonAdditionalInfo;                          // Booking's additional info
    private String qrCode;                                          // Booking's QR code
    private String urlVideo;                                        // Booking's URL video
    private BookingStatus status;                                   // Booking's status
    private boolean hasKit;                                         // Booking has kit
    private ArrayList<CovidTest> covidTests = new ArrayList<>();    // Booking's COVID tests

    /***
     * Constructor.
     * @param bookingJson booking in form of JSON object
     */
    public Booking(JSONObject bookingJson) throws InterruptedException, ParseException, IOException {
        id = (String) bookingJson.get("id");
        smsPin = (String) bookingJson.get("smsPin");
        status = BookingStatus.getStatus((String) bookingJson.get("status"));
        startTime = Instant.parse((String) bookingJson.get("startTime"));
        customer = new User((String) ((JSONObject) bookingJson.get("customer")).get("id"));
        jsonAdditionalInfo = (JSONObject) bookingJson.get("additionalInfo");
        qrCode = (String) jsonAdditionalInfo.get("qrCode");
        urlVideo = (String) jsonAdditionalInfo.get("url");
        if (jsonAdditionalInfo.get("hasKit") == null || jsonAdditionalInfo.get("hasKit").equals("false")) {
            hasKit = false;
        }
        else {
            hasKit = true;
        }

        if (bookingJson.get("testingSite") != null) {
            JSONObject siteJson = (JSONObject) bookingJson.get("testingSite");
            setTestingSite(siteJson);
        }

        if (bookingJson.get("covidTests") != null && !bookingJson.get("covidTests").toString().equals("[]")) {
            for (Object covidTest : (JSONArray) bookingJson.get("covidTests")) {
                covidTests.add(new CovidTest((JSONObject) covidTest));
            }
        }
        if (testingSite != null)
            testingSite.updateAdmins();
    }

    /***
     * Get booking's ID.
     * @return booking ID
     */
    public String getId(){ return id; }

    /***
     * Get customer's name.
     * @return customer name
     */
    public String getCustomerName() {
        return customer.getFullName();
    }

    /***
     * Get customer's ID.
     * @return customer ID
     */
    public String getCustomerID() {
        return customer.getId();
    }

    /***
     * Check if booking has an assigned testing site.
     * @return boolean indicating if booking has testing site
     */
    public boolean hasTestingSite() { return testingSite != null;}

    /***
     * Get testing site's name.
     * @return testing site's name
     */
    public String getTestingSiteName() { return testingSite.getName(); }

    /***
     * Get testing site's ID.
     * @return testing site's ID
     */
    public String getTestingSiteId() { return testingSite.getId(); }

    /***
     * Get sms PIN.
     * @return sms PIN.
     */
    public String getSmsPin() {
        return smsPin;
    }

    /***
     * Set booking's testing site.
     * @param siteJSON booking's testing site JSON
     */
    public void setTestingSite(JSONObject siteJSON) throws InterruptedException, ParseException, IOException {
        String testingSiteId = (String) siteJSON.get("id");
        testingSite = TestingSiteCollection.getInstance().findSiteById(testingSiteId);
    }

    /***
     * Get booking's covid tests.
     * @return booking's covid tests
     */
    public ArrayList<CovidTest> getCovidTests() {
        return covidTests;
    }

    /***
     * Get booking's additional info.
     * @return booking's additional info
     */
    public JSONObject getAdditionalInfo() {
        return jsonAdditionalInfo;
    }

    /***
     * Get booking's has RAT kit status.
     * @return booking's kit status
     */
    public boolean hasKit() {
        return hasKit;
    }

    /***
     * Set booking's has RAT kit status.
     * @param hasKit available status
     */
    public void setHasKit(boolean hasKit) {
        this.hasKit = hasKit;
    }

    /***
     * Get booking's QR code.
     * @return booking's QR code.
     */
    public String getQrCode() {
        return qrCode;
    }

    /***
     * Get booking's URL video.
     * @return booking's URL video.
     */
    public String getUrlVideo() { return urlVideo;
    }

    /***
     * Get booking's start time.
     * @return booking's start time.
     */
    public Instant getStartTime() {
        return startTime;
    }

    /***
     * Set booking's start time.
     * @param startTime booking's start time.
     */
    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    /***
     * Get booking's status.
     * @return status
     */
    public BookingStatus getStatus() {
        return status;
    }

    /***
     * Set booking's status.
     * @param status status
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /***
     * Check if booking has elapsed.
     */
    public boolean isElapsed() {
        return startTime.isBefore(Instant.now());
    }

    /***
     * Check if any test has been performed for booking.
     */
    public boolean isCompleted() {
        return !covidTests.isEmpty();
    }

    /***
     * Return current state as a memento
     * @return booking's memento.
     */
    public BookingMemento saveMemento() {
        return new BookingMemento(this.testingSite.getId(), startTime);
    }

    /***
     * Apply state from a memento.
     * @param memento memento to get state from
     */
    public void restoreMemento(BookingMemento memento) throws IOException, InterruptedException, ParseException {
        JSONParser parser = new JSONParser();
        status = BookingStatus.MODIFIED;
        String bookingPatchString = "{\"customerId\":\"" + customer.getId() + "\"," +
                "\"testingSiteId\":\"" + memento.getTestingSideId() + "\"," +
                "\"startTime\":\"" + memento.getDate() + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"notes\":\"" + "null" + "\"," +
                "\"additionalInfo\":" + "{}" + "}";

        startTime = memento.getDate();
        String bookingJsonString = updateDatabase(bookingPatchString).body().toString();
        JSONObject bookingJson = (JSONObject) parser.parse(bookingJsonString);
        setTestingSite((JSONObject) bookingJson.get("testingSite"));
    }

    /***
     * Cancel booking.
     */
    public void cancelBooking() throws IOException, InterruptedException {
        status = BookingStatus.CANCELLED;
        String bookingPatchString = "{\"customerId\":\"" + customer.getId() + "\"," +
                "\"testingSiteId\":\"" + testingSite.getId() + "\"," +
                "\"startTime\":\"" + startTime.toString() + "\"," +
                "\"status\":\"" + status + "\"," +
                "\"notes\":\"" + "null" + "\"," +
                "\"additionalInfo\":" + "{}" + "}";

        updateDatabase(bookingPatchString);
    }

    /***
     * Update booking in database.
     * @param bookingPatchString booking json string
     */
    public HttpResponse updateDatabase(String bookingPatchString) throws IOException, InterruptedException {
        String bookingPatchUrl = "/booking/" + id;
        Connection connection = new Connection();
        return connection.patchRequest(bookingPatchUrl, bookingPatchString);
    }

    /***
     * Delete booking from database
     */
    public void deleteFromDatabase() throws IOException, InterruptedException {
        status = BookingStatus.DELETED;
        String bookingDeleteUrl = "/booking/" + id;
        Connection connection = new Connection();
        connection.deleteRequest(bookingDeleteUrl);
    }

    /***
     * To update the admin and notification list
     * @param command the command used
     * @param additionalSiteId additional testing site id
     */
    @Override
    public void notify(BookingStatus command, String additionalSiteId, User user) throws InterruptedException, ParseException, IOException {
        admins.clear();

        testingSite.updateAdmins();
        for (User admin: testingSite.getAdmins()){
            admins.add(admin);
        }
        if (additionalSiteId != null && command == BookingStatus.MODIFIED && !additionalSiteId.equals(testingSite.getId())) {
            TestingSite additionalSite = TestingSiteCollection.getInstance().findSiteById(additionalSiteId);
            additionalSite.updateAdmins();
            for (User admin: additionalSite.getAdmins()){
                admins.add(admin);
            }
        }
        setNotification(command,user, id);
    }

    /***
     * Update the database with the new notification
     * @param command the actions
     */
    public void setNotification(BookingStatus command, User user, String bookingId) throws InterruptedException, ParseException, IOException {
        for (User admin: admins){
            admin.setNotification(command,user,bookingId);
        }
    }
}
