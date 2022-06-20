package CovidBookingTestingSystem.Controller.BookingModificationController;

import CovidBookingTestingSystem.Model.BookingModel.Booking;
import CovidBookingTestingSystem.Model.BookingModel.BookingMemento;
import CovidBookingTestingSystem.Model.BookingModel.BookingStatus;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookingModificationView.BookingModificationFrame;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.time.Instant;

/**
 * Booking caretaker to modify booking.
 */
public class BookingCaretaker {
    private Booking booking;                                                                    // Booking to modify
    private int maximumUndo = 3;                                                                // Maximum undo allowed
    private BookingMemento[] mementos = new BookingMemento[maximumUndo];                        // Mementos stored
    private BookingModificationFrame bookingModificationFrame = new BookingModificationFrame(); // Booking modification frame to be displayed
    private int latestBookingPointer = -1;                                                      // Pointer to the latest booking pointer
    private User user;                                                                          // Current user

    /***
     * Constructor.
     */
    public BookingCaretaker(Booking booking, User user) {
        this.booking = booking;
        this.user = user;
        bookingModificationFrame.updateStateView(booking.getId(),booking.getTestingSiteId(),booking.getStartTime());
        bookingModificationFrame.setSiteIdInput(booking.getTestingSiteId());
        bookingModificationFrame.setCalendarInput(booking.getStartTime());

        bookingModificationFrame.getModifyButton().addActionListener(e -> {
            try {
                if (modifyBooking()) {
                    bookingModificationFrame.setLatestStatus("Successfully modified booking");
                    bookingModificationFrame.updateStateView(this.booking.getId(), this.booking.getTestingSiteId(), this.booking.getStartTime());
                }
                else {
                    bookingModificationFrame.setLatestStatus("Fail to modify booking - make sure site and date are valid ");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
        });

        bookingModificationFrame.getUndoButton().addActionListener(e -> {
            try {
                if (undoBooking()) {
                    bookingModificationFrame.setLatestStatus("Successfully undid booking");
                    bookingModificationFrame.updateStateView(this.booking.getId(), this.booking.getTestingSiteId(), this.booking.getStartTime());
                }
                else {
                    bookingModificationFrame.setLatestStatus("No data to undo or maximum(" + maximumUndo + ") undos reached");
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    /***
     * Undo a booking to the latest state stored in memento, at most 3 undo(s) are possible.
     * @return boolean indicating if undo is successful
     */
    private boolean undoBooking() throws InterruptedException, ParseException, IOException {
        if (latestBookingPointer != -1 && mementos[latestBookingPointer] != null) {
            String currentSiteId = booking.getTestingSiteId();
            booking.restoreMemento(mementos[latestBookingPointer]);
            if (!currentSiteId.equals(booking.getTestingSiteId())) {
                booking.notify(BookingStatus.MODIFIED, currentSiteId, user);
            } else {
                booking.notify(BookingStatus.MODIFIED,null,user);
            }
            mementos[latestBookingPointer] = null;
            if (latestBookingPointer != 0) {
                latestBookingPointer -= 1;
            } else {
                latestBookingPointer = mementos.length-1;
            }
            return true;
        }
        return false;
    }

    /***
     * Modify a booking and store state into mementos, at most 3 mementos are stored.
     * @return boolean indicating if modification is successful
     */
    private boolean modifyBooking() throws IOException, InterruptedException, ParseException {
        BookingMemento newMemento = booking.saveMemento();

        if (bookingModificationFrame.getDateSelected().isAfter(Instant.now()) && !(bookingModificationFrame.getDateSelected().equals(booking.getStartTime()) && bookingModificationFrame.getTestingSiteId().equals(booking.getTestingSiteId()))) {
            JSONParser parser = new JSONParser();
            String bookingPatchString = "{\"customerId\":\"" + booking.getCustomerID() + "\"," +
                    "\"testingSiteId\":\"" + bookingModificationFrame.getTestingSiteId() + "\"," +
                    "\"startTime\":\"" + bookingModificationFrame.getDateSelected() + "\"," +
                    "\"status\":\"" + "modified" + "\"," +
                    "\"notes\":\"" + "null" + "\"," +
                    "\"additionalInfo\":" + "{}" + "}";

            HttpResponse response = booking.updateDatabase(bookingPatchString);
            if (response.statusCode() == 200) {
                booking.notify(BookingStatus.MODIFIED,bookingModificationFrame.getTestingSiteId(),user);

                String bookingJsonString = response.body().toString();
                JSONObject bookingJson = (JSONObject) parser.parse(bookingJsonString);
                booking.setStatus(BookingStatus.MODIFIED);
                booking.setTestingSite((JSONObject) bookingJson.get("testingSite"));
                booking.setStartTime(bookingModificationFrame.getDateSelected());

                latestBookingPointer = (latestBookingPointer + 1) % 3;
                mementos[latestBookingPointer] = newMemento;
                return true;
            }
        }
        return false;
    }
}
