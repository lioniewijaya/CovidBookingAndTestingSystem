package CovidBookingTestingSystem.Model;

import CovidBookingTestingSystem.Connection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Base64;

/**
 * JwtToken from login attempt which is  used for authentication and authorization.
 */
public class JwtToken {
    private String jwtString; // JWT string

    /***
     * Constructor.
     * @param jwtTokenJson JWT token in form of json object
     */
    public JwtToken(JSONObject jwtTokenJson) {
        jwtString = jwtTokenJson.get("jwt").toString();
    }

    /***
     * Verify integrity of JWT token.
     * @return boolean indicating integrity verification result
     */
    public boolean verifyJwt() throws IOException, InterruptedException {
        String usersVerifyTokenUrl = "/user/verify-token";
        String jsonJwtString = "{\"jwt\":\"" + jwtString + "\"}";
        Connection connection = new Connection();
        return connection.postRequest(usersVerifyTokenUrl,jsonJwtString).statusCode() == 200;
    }

    /***
     * Decode JWT token to obtain user's ID stored.
     * @return user ID
     */
    public String getUserIdFromJwt() throws ParseException {
        String[] chunks = jwtString.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        JSONParser parser = new JSONParser();
        JSONObject userJson = (JSONObject) parser.parse(payload);
        return userJson.get("sub").toString();
    }
}
