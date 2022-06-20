package CovidBookingTestingSystem.Model.UserModel;

import CovidBookingTestingSystem.View.MainView.MenuTab;

/**
 * Receptionist role: handles offline booking for onsite testing and scan QR code to grant RAT kit
 * for user opting for home testing.
 */
public class Receptionist implements Role {

    /***
     * Receptionist role described in string.
     * @return receptionist string
     */
    @Override
    public String roleString() {
        return "Receptionist";
    }

    /***
     * Menu tab for receptionist role, tabs included are search tab and scan QR code tab.
     * @return menu tab customized for receptionist role
     */
    @Override
    public MenuTab roleTab() {
        return MenuTab.RECEPTIONIST;
    }
}
