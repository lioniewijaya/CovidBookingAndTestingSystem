package CovidBookingTestingSystem.Model.TestModel;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.UserModel.User;
import org.json.simple.JSONObject;
import java.io.IOException;

/**
 * Covid Test for the COVID Testing Registration System.
 */
public class CovidTest {

    private String bookingId;    // Covid test's booking ID
    private String customerId;   // Covid test's customer ID
    private CovidTestType type;  // Covid test's type
    private User administerer;   // Covid test's administerer

    /***
     * Constructor.
     * @param testJson test in form of JSON object
     */
    public CovidTest(JSONObject testJson) {
        type = CovidTestType.getTestType(testJson.get("type").toString());
        if (testJson.get("administerer") != null)
        administerer = new User((JSONObject) testJson.get("administerer"));
    }

    /**
     * Constructor.
     * @param bookingId covid test's booking ID
     * @param customerId covid test's customer ID
     * @param type covid test's type
     */
    public CovidTest(String bookingId, String customerId, CovidTestType type, User user) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.type = type;
        this.administerer = user;
    }

    /**
     * Update database in web service for covid test.
     */
    public void updateDatabase() throws IOException, InterruptedException {

        String createTestingUrl = "/covid-test";
        String testingString = "{\"patientId\":\"" + customerId + "\"," +
                "\"administererId\":\"" + administerer.getId() + "\"," +
                "\"bookingId\":\"" + bookingId + "\"," +
                "\"type\":\"" + type + "\"," +
                "\"status\":\"" + "tested" + "\"," +
                "\"notes\":\"" + "null" + "\"," +
                "\"additionalInfo\":" + "{}" + "}";

        Connection connection = new Connection();
        connection.postRequest(createTestingUrl,testingString);
    }

    /**
     * Get covid test kit type.
     * @return kit type
     */
    public CovidTestType getType(){
        return type;
    }
}
