import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import java.io.IOException;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.text.DecimalFormat;

/** 
 *  StatsPanelController 
 *  
 *  Controls the display of the data on the stats panel, as well as calculate
 *  the actual statistics displayed
 *  
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class StatsPanelController {
    private DecimalFormat formattedDec = new DecimalFormat("0.00");
    private DataManipulator dataManipulator;
    
    @FXML
    private BorderPane rootPane;

    @FXML
    private Label infoLabel;

    @FXML
    private Label statLabel;

    private String[] information = {"Parks Mobility Average % Change:", "Transit Mobility Average % Change:", "Total Number of Total Deaths","Total Cases Average:" };
    private int currentInfoIndex = 0;

    private String[] stats = {"", "", "", ""}; // initially empty, leaving space for the calculated data to be placed in
    private int currentStatIndex = 0;
    
    
    
    private boolean initialised = false;

    
    /**
     *  Initialises the program when application runs.
     */
    public void initialize() {
        dataManipulator = DataManipulator.getInstance();
        statisticInRange();
        updateInfoLabel();
    }

    /**
     *  Method called when the forward stat button is pressed, 
     *  displaying the next page of data
     */
    @FXML
    void forwardStat(ActionEvent event) {
        if (!initialised) {
            statisticInRange();
            initialised = true;
        }
        currentInfoIndex = (currentInfoIndex + 1) % information.length;
        currentStatIndex = (currentStatIndex + 1) % stats.length;
        updateInfoLabel();
    }

    /**
     *  Method called when the back stat button is pressed,
     *  displaying the previous page of data
     */
    @FXML
    void prevStat(ActionEvent event) {
        if (!initialised) {
            statisticInRange();
            initialised = true;
        }
        currentInfoIndex = (currentInfoIndex - 1 + information.length) % information.length;
        currentStatIndex = (currentStatIndex - 1 + stats.length) % stats.length;
        updateInfoLabel();
    }
    
    /**
     *  Updates the label to show the appropriate data
     */
    private void updateInfoLabel() {
        infoLabel.setText(information[currentInfoIndex]);
        statLabel.setText(stats[currentStatIndex]);
    }
    
    /**
     *  Returns the records in range, calculates the statistics from that data and places
     *  calculated data into the respective array list.
     */
    private void statisticInRange() {
        LocalDate fromDate = dataManipulator.getFromDate();
        LocalDate toDate = dataManipulator.getToDate();
        
        if (fromDate != null && toDate != null) {
            ArrayList<CovidData> recordsInRange = dataManipulator.getRecordsInRange(); // returns all the records in time range
            if (!(recordsInRange.isEmpty())) {
                
                int totalDeaths = calculateTotalDeaths(recordsInRange);
                double newTotalCasesAvg = calculateTotalCasesAvg(recordsInRange);
                double newParksAvg = calculateParksGMRAvg(recordsInRange);
                double newTransitAvg = calculateTransitGMRAvg(recordsInRange);
                stats[0] = "" + formattedDec.format(newParksAvg);
                stats[1] = "" + formattedDec.format(newTransitAvg);
                stats[2] = "" + totalDeaths;
                stats[3] = "" + formattedDec.format(newTotalCasesAvg); //converts to 2 decimal places
                

            }
            else {
                infoLabel.setText("No data");
                statLabel.setText("");
            }
        }
    }
    
    /**
     * Returns the total of total deaths across all boroughs in that time period
     */
    private int calculateTotalDeaths(ArrayList<CovidData> recordsInRange) {
        int totalDeaths = 0;
        for (CovidData record : recordsInRange) { // iterates through the records, finding records in range
            totalDeaths += record.getTotalDeaths();
        }
        return totalDeaths;
    }
    

    /**
     * Calculates the average of total cases
     */
    private double calculateTotalCasesAvg(ArrayList<CovidData> recordsInRange) {
        int totalCases = 0;
        for (CovidData record : recordsInRange) { // iterates through the records, finding records in range
            totalCases += record.getTotalCases();
        }
        return (double) totalCases / recordsInRange.size();
    }
    
    
    /**
     * Calculates the change in parks mobility over that time period
     */
      private double calculateParksGMRAvg(ArrayList<CovidData> recordsInRange) {
        int sum = 0;
        int count = 0;
        for (CovidData record : recordsInRange) { // iterates through the records, finding records in range
            sum += record.getParksGMR();
            count++;
        }
        return (double) sum / count; 
    }
    
    /**
     * Calculates the change in transit over that time period
     */
      private double calculateTransitGMRAvg(ArrayList<CovidData> recordsInRange) {
        int sum = 0;
        int count = 0;
        for (CovidData record : recordsInRange) { // iterates through the records, finding records in range
            sum += record.getTransitGMR();
            count++;
        }
        return (double) sum / count; 
    }
    

}