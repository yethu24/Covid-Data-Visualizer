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

/**
 * MainLayoutController manages the user interface behavior for the main layout of 
 * the Covid 19 Data Viewer application. It handles loading and displaying different 
 * FXML files, analyzing date ranges selected by the user, and updating the data 
 * manipulation process accordingly.
 * 
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class MainLayoutController
{
    private BorderPane rootPane;
    private int currentFileIndex = 1;
    private static final int MAX_FILES = 4;
    
    //Instance variable for Singleton Class Data Manipulator 
    private DataManipulator dataManipulator;
    
    //date
    @FXML
    private DatePicker fromDatePicker;
    
    @FXML
    private DatePicker toDatePicker;
    
    @FXML
    private Button forwardButton;
    
    @FXML
    private Button backwardButton;
    
    /**
     * Sets the root pane for the controller to manage.
     */
    public void setRootPane(BorderPane rootPane) {
        this.rootPane = rootPane;
    }
    
    /**
     * Handles the action event for moving forward to the next FXML file.
     */
    @FXML
    void goForward(ActionEvent event) {
        if (currentFileIndex < MAX_FILES) {
            currentFileIndex++;
        }
        else {
            currentFileIndex = 1;
        }
        String foundFileName = findFXMLFile(currentFileIndex);
        loadFile(foundFileName);
    }
    
    /**
     * Handles the action event for moving backward to the next FXML file.
     */
    @FXML
    void gobackwards(ActionEvent event) {
        if (currentFileIndex > 1) {
            currentFileIndex--;
        }
        else {
            currentFileIndex = 4;
        }
        String foundFileName = findFXMLFile(currentFileIndex);
        loadFile(foundFileName);
    }
    
    /**
     * Finds the FXML file with the specified index in the current directory.
     * 
     * @param index The index of the FXML file to find.
     * @return      The filename of the found FXML file, or null if not found.
     */
    private String findFXMLFile(int index){
        String currentDirectory = System.getProperty("user.dir");
        File directory = new File(currentDirectory);
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".fxml") && file.getName().contains(Integer.toString(index))) {
                return file.getName();
            }
        }
        return null;
    }
    
    /**
     * Loads the specified FXML file and sets it as the center content of the root pane.
     * 
     * @param fxmlFileName The filename of the FXML file to load.
     */
    private void loadFile(String fxmlFileName) {
        if (fxmlFileName != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
                Parent newCenter = loader.load();
                rootPane.setCenter(newCenter);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * This method is called as soon as the FXML files are loaded.
     * 
     * Gets access to the singleton DataManipulator instance
     * Disables the forward and backward Buttons before selecting the dates
     */
    @FXML 
    private void initialize() {
        dataManipulator = DataManipulator.getInstance();
        
        forwardButton.setDisable(true);
        backwardButton.setDisable(true);
    }
    
    /**
     * Analyses the dates selected and displays an appropriate response
     */
    @FXML
    private void analyseDateRange(ActionEvent event) {
        setButtonDisabled(true);
        
        //obtains the source of the Action Event
        DatePicker datePicker = (DatePicker) event.getSource();
        LocalDate selectedDate = datePicker.getValue();
        
        
        LocalDate fromDate = fromDatePicker.getValue();
        //changes the date in Data Manipulator to the date selected
        dataManipulator.setFromDate(fromDate);
        
        LocalDate toDate = toDatePicker.getValue();
        //changes the date in Data Manipulator to the date selected
        dataManipulator.setToDate(toDate);
    
        if (selectedDate != null) {
            if (!dataManipulator.checkValidDate(selectedDate)) {
                if (datePicker == fromDatePicker) {
                    showAlert("Please select a valid start date.");
                } 
                else if (datePicker == toDatePicker) {
                    showAlert("Please select a valid end date.");
                }
                return;
            }
            else if (fromDate != null && !(dataManipulator.checkValidDate(fromDate))) {
                showAlert("Reminder: start date is still invalid.");
                return;
            }
            else if (toDate != null && !(dataManipulator.checkValidDate(toDate))) {
                showAlert("Reminder: End date is still invalid.");
                return;
            }
        }
    
        
    
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            showAlert("Start date cannot be after End date.");
            return;
        }
        
        if (fromDate == null || toDate == null) {
            return;
        }
        
        dataManipulator.updateRecordsInRange();
        setButtonDisabled(false);
    }
    
    /**
     * Error message when selecting date
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Date Range");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Disables both the forward button and the backward button
     */
    private void setButtonDisabled(boolean disabled) {
        forwardButton.setDisable(disabled);
        backwardButton.setDisable(disabled);
    }
}