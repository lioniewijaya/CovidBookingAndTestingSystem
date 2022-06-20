package CovidBookingTestingSystem.Controller;

import CovidBookingTestingSystem.Connection;
import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.View.ScannerTabPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;

/**
 * Scanner to scan QR code.
 */
public class ScannerTab implements TabController {
    private ScannerTabPane scannerPane;                         // Scanner display
    private ArrayList<Booking> bookings = new ArrayList<>();    // List of HomeBookings
    private Booking homeBooking = null;                         // HomeBook

    private Connection connection = new Connection();           // Connection to web service

    /**
     * Constructor.
     * @param scannerPane scanner display
     */
    public ScannerTab(ScannerTabPane scannerPane) throws IOException, InterruptedException, ParseException {
        this.scannerPane = scannerPane;

        String bookingUrl = "/booking";

        HttpResponse response = connection.getRequest(bookingUrl);

        if (response.statusCode() == 200) {
            JSONParser parser = new JSONParser();
            String bookingsJsonString = response.body().toString();
            JSONArray bookingsJson = (JSONArray) parser.parse(bookingsJsonString);
            for (Object bookingJson : bookingsJson) {
                Booking booking = new Booking((JSONObject) bookingJson);
                if (booking.getAdditionalInfo().get("hasKit")!=null && booking.getAdditionalInfo().get("hasKit").equals("true")) {
                    booking.setHasKit(true);
                }
                else {
                    booking.setHasKit(false);
                }
                bookings.add(booking);
            }
        }

        // BookingTab button logic
        this.scannerPane.getSearchButton().addActionListener(e -> {
            try {
                checkQrCodeExist();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });

        // Scan button logic
        this.scannerPane.getScanButton().addActionListener(e -> {
            try {
                updateDatabase();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }

    /**
     * Check if QrCode exist by looping through all the booking list. Adds the booking with existing QrCode
     */
    private void checkQrCodeExist() throws IOException, InterruptedException {
        String qrCode = scannerPane.getQrCodeInput();
        // Loop through all booking to find the booking with the existing qr code
        boolean found = false;
        this.homeBooking = null;

        for (Booking homeBooking : bookings) {
            String qrCodeInBooking = (String) homeBooking.getAdditionalInfo().get("qrCode");
            if (qrCodeInBooking != null && qrCodeInBooking.equals(qrCode)) {
                found = true;
                this.homeBooking = homeBooking;
                break;
            }
        }

        boolean finalFound = found;
        Booking finalBookingWithQr = this.homeBooking;
        scannerPane.updateResult(finalFound,finalBookingWithQr);
    }

    /**
     * Update database based on the booking's credentials (QRCode, URL, and hasKit bool)
     */
    private void updateDatabase() throws IOException, InterruptedException {
        String patchBookingUrl = "/booking/" + homeBooking.getId();
        String bookingString = "{\"additionalInfo\":" + "{" +
                "\"qrCode\":\"" + homeBooking.getId() + "\"," +
                "\"url\":\"" + "dummyUrlHere" + "\"," +
                "\"hasKit\":\"" + "true" + "\"}}";

        connection.patchRequest(patchBookingUrl,bookingString);

        this.homeBooking.setHasKit(true);
        this.scannerPane.getScanButton().setEnabled(false);
        this.scannerPane.getScanButton().setText("RAT kit acquired.");
    }

    /**
     * Get a tab component containing tab pane view
     * @return tap component
     */
    @Override
    public JComponent getTabComponent() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout());
        panel.add(scannerPane);
        return panel;
    }
}
