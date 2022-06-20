package CovidBookingTestingSystem.View.BookView;

import CovidBookingTestingSystem.Controller.BookController.OfflineOnSiteBook;
import CovidBookingTestingSystem.Controller.BookController.OnlineOnSiteBook;
import CovidBookingTestingSystem.Model.TestingSiteModel.Facility;
import CovidBookingTestingSystem.Model.TestingSiteModel.TestingSite;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.View.BookView.BookFrame;
import CovidBookingTestingSystem.View.MainView.MenuTab;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Search tab pane to be displayed in the main frame during the search process.
 */
public class BookingTabPane extends JPanel{
    private JTextField suburbTextField;
    private JPanel filterPane;
    private ArrayList<JCheckBox> filters = new ArrayList<>();
    private JPanel resultPane;
    private JButton searchButton = new JButton("Search");
    private JButton homeBooking = new JButton("Home booking");

    /***
     * Constructor.
     */
    public BookingTabPane(){
        setLayout(new BorderLayout());

        JPanel searchSuburbPane = new JPanel();
        suburbTextField = new JTextField(20);
        searchSuburbPane.add(suburbTextField);
        searchSuburbPane.add(searchButton);

        // Filter
        filterPane = new JPanel();
        filterPane.setLayout(new GridBagLayout());
        filterPane.setBorder(new TitledBorder("Filter"));

        for (Facility facility: Facility.values()) {
            filters.add(new JCheckBox(facility.label));
        }
        for (JCheckBox filter: filters) {
            filterPane.add(filter);
        }

        // CovidBookingTestingSystem.Controller.Search result
        resultPane = new JPanel();
        resultPane.setLayout(new BoxLayout(resultPane,BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(resultPane);
        scrollPane.setBorder(new TitledBorder("Result"));

        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        container.add(searchSuburbPane,constraints);
        constraints.gridy++;
        container.add(filterPane,constraints);

        add(container,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }

    /***
     * Get suburb inputted.
     * @return suburb string
     */
    public String getSuburbInput() {
        return suburbTextField.getText();
    }

    /***
     * Get search testing site's button.
     * @return search button
     */
    public JButton getSearchButton() {
        return searchButton;
    }

    /***
     * Get home booking's button
     * @return home booking button
     */
    public JButton getHomeBookingButton() {
        return homeBooking;
    }

    /***
     * Make individual site pane to be added to the result pane.
     * @param site site to be inserted
     * @param user user
     */
    private JPanel makeSitePane(TestingSite site, User user){
        // Create UI
        JPanel sitePanel = new JPanel();
        sitePanel.setLayout(new BoxLayout(sitePanel, BoxLayout.Y_AXIS));
        sitePanel.setBorder(new TitledBorder(""));

        // Adding Component
        sitePanel.add(new JLabel("Name: " + site.getName()));
        sitePanel.add(new JLabel("Description: " + site.getDescription()));
        sitePanel.add(new JLabel("Website URL: " + site.getWebsiteUrl()));
        sitePanel.add(new JLabel("Address: " + site.getAddress()));
        sitePanel.add(new JLabel("Facilities available: " + site.getFacilities()));
        sitePanel.add(new JLabel("Allow on site booking: " + site.checkAllowOnsiteBooking()));
        sitePanel.add(new JLabel("Opening hours: " + site.getOpeningHours() + " | Closing hours: " + site.getClosingHours() + " | Is open: " + site.isOpen()));
        sitePanel.add(new JLabel("Waiting time: " + site.getWaitingHours() + " hours"));
        sitePanel.add(new JLabel("Additional Info: " + site.getAdditionalInfo()));

        if (user.getRole().roleTab() == MenuTab.RECEPTIONIST && site.getAdditionalValue("allowOnSiteBooking")) {
            JButton onSiteBookingButton = new JButton("Book");
            sitePanel.add(onSiteBookingButton);
            onSiteBookingButton.addActionListener(e -> {
                OfflineOnSiteBook book = new OfflineOnSiteBook(user,site);
                new BookFrame().setPane(book.createBookPane());
            });
        }
        else if (user.getRole().roleTab() != MenuTab.RECEPTIONIST){
            JButton onlineBookingButton = new JButton("Book");
            sitePanel.add(onlineBookingButton);
            onlineBookingButton.addActionListener(e -> {
                OnlineOnSiteBook book = new OnlineOnSiteBook(user,site);
                new BookFrame().setPane(book.createBookPane());
            });
        }

        return sitePanel;
    }

    /***
     * Update result pane from search.
     * @param sites list of sites from search result
     * @param user user
     */
    public void updateResult(ArrayList<TestingSite> sites, User user) {
        // clear list and insert site
        resultPane.removeAll();
        if (user.getRole().roleTab() != MenuTab.RECEPTIONIST) {
            resultPane.add(homeBooking);
        }
        for (TestingSite site : sites){
            resultPane.add(makeSitePane(site, user));
        }
    }

    /***
     * Get filters applied by user.
     * @return list of active filters
     */
    public ArrayList<Facility> getFacilityFilters() {
        ArrayList<Facility> activeFilters = new ArrayList<>();
        for (JCheckBox filter: filters) {
            if (filter.isSelected()) {
                activeFilters.add(Facility.getFacility(filter.getText()));
            }
        }
        return activeFilters;
    }
}
