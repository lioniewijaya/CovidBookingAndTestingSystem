package CovidBookingTestingSystem.Model.UserModel;

import CovidBookingTestingSystem.View.MainView.MenuTab;

/**
 * User's role.
 */
public interface Role {
    /***
     * Role described in string.
     * @return role string
     */
    public String roleString();

    /***
     * Menu tab for a specific role
     * @return menu tab customized for current role
     */
    public MenuTab roleTab();
}
