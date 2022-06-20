package CovidBookingTestingSystem.View.MainView;

import CovidBookingTestingSystem.Controller.BookingModificationController.AdminTab;
import CovidBookingTestingSystem.Controller.AdministerController.HomeAdministerTab;
import CovidBookingTestingSystem.Controller.AdministerController.OnsiteAdministerTab;
import CovidBookingTestingSystem.Controller.ScannerTab;
import CovidBookingTestingSystem.Controller.BookController.BookingTab;
import CovidBookingTestingSystem.Model.UserModel.User;
import CovidBookingTestingSystem.Controller.BookingModificationController.ProfileTab;
import CovidBookingTestingSystem.View.AdministerView.HomeAdministerTabPane;
import CovidBookingTestingSystem.View.AdministerView.OnsiteAdministerTabPane;
import CovidBookingTestingSystem.View.BookingModificationView.AdminTabPane;
import CovidBookingTestingSystem.View.BookingModificationView.ProfileTabPane;
import CovidBookingTestingSystem.View.ScannerTabPane;
import CovidBookingTestingSystem.View.BookView.BookingTabPane;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Enum to indicate different menu tabs available to different roles.
 */
public enum MenuTab {
    HEALTHCARE_WORKER,
    RECEPTIONIST,
    CUSTOMER;

    /***
     * Create a menu tab with functionalities relating to user's role
     * @return menu tab customized for different roles
     */
    public JTabbedPane getMenuTab(User user) throws InterruptedException, ParseException, IOException {
        JTabbedPane menuTab = new JTabbedPane();

        switch (this) {
            case HEALTHCARE_WORKER: {
                OnsiteAdministerTabPane administerTabPane = new OnsiteAdministerTabPane(user);
                OnsiteAdministerTab administerTab = new OnsiteAdministerTab(user, administerTabPane);
                JComponent onsiteAdministerTab = administerTab.getTabComponent();
                menuTab.addTab("Administer", null, onsiteAdministerTab);
                menuTab.setMnemonicAt(0, KeyEvent.VK_0);
                break;
            }
            case RECEPTIONIST: {
                BookingTabPane bookingTabPane = new BookingTabPane();
                BookingTab bookingTab = new BookingTab(bookingTabPane, user);
                JComponent searchTab = bookingTab.getTabComponent();
                menuTab.addTab("Search", null, searchTab);
                menuTab.setMnemonicAt(0, KeyEvent.VK_1);

                ScannerTabPane scannerPane = new ScannerTabPane();
                ScannerTab scanner = new ScannerTab(scannerPane);
                JComponent scannerTab = scanner.getTabComponent();
                menuTab.addTab("Scan", null, scannerTab);
                menuTab.setMnemonicAt(1, KeyEvent.VK_2);

                AdminTabPane adminPane = new AdminTabPane(user);
                AdminTab adminTabController = new AdminTab(adminPane, user);
                JComponent adminTab = adminTabController.getTabComponent();
                menuTab.addTab("Admin", null, adminTab);
                menuTab.setMnemonicAt(2, KeyEvent.VK_3);
                break;
            }
            case CUSTOMER: {
                BookingTabPane bookingTabPane = new BookingTabPane();
                BookingTab bookingTab = new BookingTab(bookingTabPane, user);
                JComponent searchTab = bookingTab.getTabComponent();
                menuTab.addTab("Search", null, searchTab);
                menuTab.setMnemonicAt(0, KeyEvent.VK_1);

                HomeAdministerTabPane administerTabPane = new HomeAdministerTabPane(user);
                HomeAdministerTab administerTab = new HomeAdministerTab(user,administerTabPane);
                JComponent homeAdministerTab = administerTab.getTabComponent();
                menuTab.addTab("Home Administer", null, homeAdministerTab);
                menuTab.setMnemonicAt(1, KeyEvent.VK_2);

                ProfileTabPane profilePane = new ProfileTabPane(user);
                ProfileTab profileTabController = new ProfileTab(profilePane, user);
                JComponent profileTab = profileTabController.getTabComponent();
                menuTab.addTab("Profile", null, profileTab);
                menuTab.setMnemonicAt(2, KeyEvent.VK_3);
                break;
            }
        }
        return menuTab;
    }
}