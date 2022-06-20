package CovidBookingTestingSystem.Model.UserModel;

import CovidBookingTestingSystem.View.MainView.MenuTab;

/**
 * HealthcareWorker role: administer COVID test for a patient, maybe for other patient (given that user is a healthcare worker)
 * or himself/herself (given that user is a customer opting for home testing).
 */
public class HealthcareWorker implements Role {

    /***
     * HealthcareWorker role described in string.
     * @return administerer string
     */
    @Override
    public String roleString() {
        return "HealthcareWorker";
    }

    /***
     * Menu tab for administerer role, tab included is administer tab (for healthcare worker, when user is an administerer
     * then this menu tab won't be used as customer has their own tab, administer for home testing will have a specific entry
     * point from history tab for customer role).
     * @return menu tab customized for administerer role
     */
    @Override
    public MenuTab roleTab() {
        return MenuTab.HEALTHCARE_WORKER;
    }
}
