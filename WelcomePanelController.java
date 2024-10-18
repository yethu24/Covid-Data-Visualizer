import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

/**
 * Controller class for the Welcome Panel
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class WelcomePanelController
{
    //Instance variable for Singleton Class Data Manipulator 
    private DataManipulator dataManipulator;

    //Label for the Date range in the Welcome Panel
    @FXML
    private Label dateRangeLabel;
    
    /**
     * This method is called as soon as the FXML files are loaded.
     * 
     * Gets access to the singleton DataManipulator instance
     * Adds listeners of property wrapped Start Date and End Date from Data Manipulator
     * updates the Date Range Label
     */
    @FXML
    private void initialize() {
        dataManipulator = DataManipulator.getInstance();
        //Adds listener of property wrapped start date and end date
        //whenever, changes are made to the selceted dates, updateDateRangeLabel() method is called.
        dataManipulator.getFromDateProperty().addListener((observable, oldValue, newValue) -> updateDateRangeLabel());
        dataManipulator.getToDateProperty().addListener((observable, oldValue, newValue) -> updateDateRangeLabel());

        updateDateRangeLabel();
    }

    /**
     * Updates the Date Range Label
     */
    private void updateDateRangeLabel() {
        LocalDate fromDate = dataManipulator.getFromDate();
        LocalDate toDate = dataManipulator.getToDate();
        
        if (fromDate != null && toDate != null) {
            if (!fromDate.isAfter(toDate) && dataManipulator.checkValidDate(toDate) && dataManipulator.checkValidDate(fromDate)) {
                dateRangeLabel.setText(fromDate + " - " + toDate);
            }
            else {
                dateRangeLabel.setText("Date Range: [Invalid range selected]");
            }
        } else {
            dateRangeLabel.setText("Date Range: [Not Selected]");
        }
    }
}