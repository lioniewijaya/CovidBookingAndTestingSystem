package CovidBookingTestingSystem.Controller;

import javax.swing.*;

/**
 * Tab controller which control a tab pane
 */
public interface TabController {

    /**
     * Get a tab component containing tab pane view
     * @return tap component
     */
    JComponent getTabComponent();
}
