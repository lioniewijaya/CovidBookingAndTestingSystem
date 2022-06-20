package CovidBookingTestingSystem.Controller.BookController;

import CovidBookingTestingSystem.Controller.TabController;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSiteCollection;
import CovidBookingTestingSystem.View.BookView.BookFrame;
import CovidBookingTestingSystem.Model.TestingSiteModel.Facility;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookView.BookingTabPane;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Search testing site to book a COVID test.
 */
public class BookingTab implements TabController {
    private BookingTabPane bookingTabPane;                           // Search tab pane in main frame to be displayed to user

    /***
     * Constructor.
     * @param searchPane search pane to be displayed
     * @param user user
     */
    public BookingTab(BookingTabPane searchPane, User user) throws IOException, InterruptedException, ParseException {
        this.bookingTabPane = searchPane;

        this.bookingTabPane.getSearchButton().addActionListener(e -> {
            String suburb = this.bookingTabPane.getSuburbInput();
            ArrayList<Facility> activeFilters = bookingTabPane.getFacilityFilters();
            ArrayList<TestingSite> sitesSearched = null;
            try {
                sitesSearched = TestingSiteCollection.getInstance().searchTestingSite(suburb,activeFilters);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            this.bookingTabPane.updateResult(sitesSearched,user);
        });

        this.bookingTabPane.getHomeBookingButton().addActionListener(e -> {
            HomeBook book = new HomeBook(user);
            new BookFrame().setPane(book.createBookPane());
        });
    }

    /**
     * Get a tab component containing tab pane view
     * @return tap component
     */
    @Override
    public JComponent getTabComponent() {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout());
        panel.add(bookingTabPane);
        return panel;
    }
}