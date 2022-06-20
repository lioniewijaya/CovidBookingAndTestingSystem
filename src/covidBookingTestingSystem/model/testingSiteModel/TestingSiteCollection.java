package CovidBookingTestingSystem.Model.TestingSiteModel;

import CovidBookingTestingSystem.Connection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * A global Singleton manager that stores testing sites on the instances.
 */
public class TestingSiteCollection {
    /**
     * A list of testing sites instances will be stored in here
     */
    private ArrayList<TestingSite> testingSiteCollection;

    /**
     * A singleton testing site collection instance
     */
    private static TestingSiteCollection instance;

    /**
     * Get the singleton instance of reset manager
     * @return TestingSiteCollection singleton instance
     */
    public static TestingSiteCollection getInstance() throws InterruptedException, ParseException, IOException {
        if (instance == null) {
            instance = new TestingSiteCollection();
        }
        return instance;
    }

    /**
     * Constructor
     */
    private TestingSiteCollection() throws InterruptedException, ParseException, IOException {
        testingSiteCollection = new ArrayList<>();
        getTestingSitesFromDatabase();
    }

    /***
     * Get all testing sites
     */
    public ArrayList<TestingSite> getTestingSites() {
        try {
            getTestingSitesFromDatabase();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return this.testingSiteCollection;
    }

    /***
     * Find testing site by ID
     */
    public TestingSite findSiteById(String testingSiteId){
        for (TestingSite site: testingSiteCollection){
            if (site.getId().equals(testingSiteId)){
                return site;
            }
        }
        return null;
    }

    /***
     * Get latest testing sites from database.
     */
    private void getTestingSitesFromDatabase() throws IOException, InterruptedException, ParseException {
        // Get all testing sites
        String testingSiteUrl = "/testing-site?fields=bookings.covidTests";
        ArrayList<TestingSite> testingSites = new ArrayList<>();

        Connection connection = new Connection();
        HttpResponse response = connection.getRequest(testingSiteUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String sitesJsonString = response.body().toString();
            JSONArray sitesJson = (JSONArray) parser.parse(sitesJsonString);
            for (Object siteJson: sitesJson) {
                testingSites.add(new TestingSite((JSONObject) siteJson));
            }
        }
        this.testingSiteCollection = testingSites;
    }

    /***
     * Get testing sites located in the suburb with the provided facilities selected in active filter.
     * @return a list of testing sites in the suburb with the provided facilities
     */
    public ArrayList<TestingSite> searchTestingSite(String suburb, ArrayList<Facility> activeFilters) {
        return filterFacility(filterSuburb(suburb),activeFilters);
    }

    /***
     * Get testing sites located in the suburb inputted.
     * @return a list of testing sites in the suburb
     */
    private ArrayList<TestingSite> filterSuburb(String suburb) {
        if (suburb.equals("")) {
            return this.testingSiteCollection;
        }

        ArrayList<TestingSite> sitesInSuburb = new ArrayList<>();
        for (TestingSite site: testingSiteCollection) {
            if (site.getPartAddress("suburb").toLowerCase().equals(suburb.toLowerCase())) {
                sitesInSuburb.add(site);
            }
        }

        return sitesInSuburb;
    }

    /***
     * Get filtered testing sites based on filters selected by user.
     * @return a list of filtered testing sites
     */
    private ArrayList<TestingSite> filterFacility(ArrayList<TestingSite> unfilteredSites, ArrayList<Facility> activeFilters) {
        if (activeFilters.size() == 0) {
            return unfilteredSites;
        }

        ArrayList<TestingSite> filteredSites = new ArrayList<>();

        for (TestingSite site: unfilteredSites) {
            boolean satisfyFilter = true;
            for (Facility filter: activeFilters) {
                if (site.checkFacility(filter)) {
                    continue;
                }
                satisfyFilter = false;
                break;
            }
            if (satisfyFilter) {
                filteredSites.add(site);
            }
        }

        return filteredSites;
    }
}
