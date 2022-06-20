package CovidBookingTestingSystem.Model.UserModel;

import CovidBookingTestingSystem.View.MainView.MenuTab;

/**
 * Customer role: handles online booking for onsite testing and home testing.
 */
public class Customer implements Role {

    /***
     * Customer role described in string.
     * @return customer string
     */
    @Override
    public String roleString() {
        return "Customer";
    }

    /***
     * Menu tab for customer role, tabs included are search tab and history tab.
     * @return menu tab customized for customer role
     */
    @Override
    public MenuTab roleTab() {
        return MenuTab.CUSTOMER;
    }
}
