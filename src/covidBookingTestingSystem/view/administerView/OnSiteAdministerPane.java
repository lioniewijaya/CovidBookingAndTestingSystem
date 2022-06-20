package CovidBookingTestingSystem.View.AdministerView;

import CovidBookingTestingSystem.Model.TestModel.CovidTestType;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Onsite administer pane for onsite testing for the COVID Testing Registration System.
 */
public class OnSiteAdministerPane extends JPanel{

    private JButton recommendation;
    private JButton submit;
    private CovidTestType type;

    /**
     * Constructor.
     */
    public OnSiteAdministerPane(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Questions panel
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBorder(new TitledBorder("Questions"));

        // Questions
        JLabel questionOneLabel = new JLabel("1. Is the patient fully vaccinated? (1st Dose, 2nd Dose, and Booster)");
        JLabel questionTwoLabel = new JLabel("2. Have you had close contact with any confirmed or suspected COVID-19 cases within the last 14 days?");
        JLabel questionThreeLabel = new JLabel("3. Do you exhibit 2 or more symptoms as listed below? \n" +
                "   - Fever \n" +
                "   - Chills \n" +
                "   - Shivering (figor) \n" +
                "   - Body ache \n" +
                "   - Headache \n" +
                "   - Sore throat \n" +
                "   - Nausea or vomiting \n" +
                "   - Diarrhea \n");

        // Radio buttons
        ButtonGroup buttonGroup1 = new ButtonGroup();
        JRadioButton yes1 = new JRadioButton("Yes");
        JRadioButton no1 = new JRadioButton("No",true);
        ButtonGroup buttonGroup2 = new ButtonGroup();
        JRadioButton yes2 = new JRadioButton("Yes");
        JRadioButton no2 = new JRadioButton("No",true);
        ButtonGroup buttonGroup3 = new ButtonGroup();
        JRadioButton yes3 = new JRadioButton("Yes");
        JRadioButton no3 = new JRadioButton("No",true);
        recommendation = new JButton("Recommend test for me");
        JLabel recommendationTextLabel = new JLabel("Your recommended test is: ");
        submit = new JButton("Submit");
        submit.setEnabled(false);

        buttonGroup1.add(yes1);
        buttonGroup1.add(no1);
        buttonGroup2.add(yes2);
        buttonGroup2.add(no2);
        buttonGroup3.add(yes3);
        buttonGroup3.add(no3);

        // Adding all components to panel
        questionPanel.add(questionOneLabel);
        questionPanel.add(yes1);
        questionPanel.add(no1);
        questionPanel.add(questionTwoLabel);
        questionPanel.add(yes2);
        questionPanel.add(no2);
        questionPanel.add(questionThreeLabel);
        questionPanel.add(yes3);
        questionPanel.add(no3);
        questionPanel.add(recommendation);
        questionPanel.add(recommendationTextLabel);
        questionPanel.add(submit);

        // Button logic for test recommendations
        recommendation.addActionListener(e -> {
            if (yes1.isSelected() && yes2.isSelected() ||
                    yes1.isSelected() && yes3.isSelected() ||
                    yes3.isSelected() && yes2.isSelected() ||
                    yes1.isSelected() && yes2.isSelected() && yes3.isSelected())
            {
                type = CovidTestType.PCR;
                recommendationTextLabel.setText("Your recommended test is: PCR");
            }
            else
            {
                type = CovidTestType.RAT;
                recommendationTextLabel.setText("Your recommended test is: RAT");
            }
            recommendation.setEnabled(false);
            submit.setEnabled(true);
        });

        add(questionPanel);
    }


    /**
     * Get recommendation button
     * @return recommendation button
     */
    public JButton getButton() {
        return recommendation;
    }

    /**
     * Get submit button
     * @return submit button
     */
    public JButton getSubmitButton(){
        return submit;
    }

    /**
     * Get covid test type
     * @return covid test type
     */
    public CovidTestType getType() {
        return type;
    }
}
