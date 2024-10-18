import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for the new window that opens when a specific borough is clicked on the map
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class BoroughDataController
{
    @FXML
    private ComboBox<String> comboBox;
    
    @FXML
    private TableView<CovidData> tableView;
    
    @FXML
    private TableColumn<CovidData, String> dateColumn;

    @FXML
    private TableColumn<CovidData, Integer> newCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> totalCasesColumn;

    @FXML
    private TableColumn<CovidData, Integer> newDeathsColumn;

    @FXML
    private TableColumn<CovidData, Integer> retailRecreationGMRColumn;

    @FXML
    private TableColumn<CovidData, Integer> groceryPharmacyGMRColumn;

    @FXML
    private TableColumn<CovidData, Integer> parksGMRColumn;

    @FXML
    private TableColumn<CovidData, Integer> transitStationsGMRColumn;

    @FXML
    private TableColumn<CovidData, Integer> workplacesGMRColumn;

    @FXML
    private TableColumn<CovidData, Integer> residentialGMRColumn;
    
    private ArrayList<CovidData> filteredBoroughRecords;
    
    private DataManipulator dataManipulator;
    
    /**
     * This method is called as soon as the FXML files are loaded.
     */
    @FXML
    private void initialize() {
        // Retrieve the instance of the data manipulator
        dataManipulator = DataManipulator.getInstance();
        
        // Get the filtered records
        this.filteredBoroughRecords = dataManipulator.getFilteredRecords();
        
        // Initialize table columns by setting cell value factories
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        newCasesColumn.setCellValueFactory(new PropertyValueFactory<>("newCases"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        newDeathsColumn.setCellValueFactory(new PropertyValueFactory<>("newDeaths"));
        retailRecreationGMRColumn.setCellValueFactory(new PropertyValueFactory<>("retailRecreationGMR"));
        groceryPharmacyGMRColumn.setCellValueFactory(new PropertyValueFactory<>("groceryPharmacyGMR"));
        parksGMRColumn.setCellValueFactory(new PropertyValueFactory<>("parksGMR"));
        transitStationsGMRColumn.setCellValueFactory(new PropertyValueFactory<>("transitGMR"));
        workplacesGMRColumn.setCellValueFactory(new PropertyValueFactory<>("workplacesGMR"));
        residentialGMRColumn.setCellValueFactory(new PropertyValueFactory<>("residentialGMR"));
        
        // Set items to the table view
        tableView.setItems(getData());
        
        // Add options to the combo box for filtering
        ObservableList<String> items = FXCollections.observableArrayList("Date", "New cases", "Total cases", "New deaths", "Google mobility data");
        comboBox.setItems(items);
    }
    
    /**
     * Get the value from the combo box for way of sorting and update the table accordingly
     */
    @FXML
    private void handleSorterSelection() { 
        String selectedSort = comboBox.getValue();
        tableView.setItems(getSortedData(selectedSort)); 
    }
    
    /**
     * Sorts the filtered borough records based on the selected sorting criteria.
     * @param selectedSort The selected sorting criteria.
     * @return An observable list of CovidData sorted according to the selected criteria.
     */
    private ObservableList<CovidData> getSortedData(String selectedSort) {
        // Create a new list to store sorted records
        ArrayList<CovidData> sortedList = new ArrayList<>(filteredBoroughRecords);
        // Check if a sorting criteria is selected
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Date":
                    // Sort by date in descending order
                    Collections.sort(sortedList, Comparator.comparing(CovidData::getLocalDate).reversed());
                    break;
                case "New cases":
                    // Sort by new cases in descending order
                    Collections.sort(sortedList, Comparator.comparingInt(CovidData::getNewCases).reversed());
                    break;
                case "Total cases":
                    // Sort by total cases in descending order
                    Collections.sort(sortedList, Comparator.comparingInt(CovidData::getTotalCases).reversed());
                    break;
                case "New deaths":
                    // Sort by new deaths in descending order
                    Collections.sort(sortedList, Comparator.comparingInt(CovidData::getNewDeaths).reversed());
                    break;
                case "Google mobility data":
                    // Sort by total average mobility data in descending order
                    Collections.sort(sortedList, Comparator.comparingInt(CovidData::getTotalAverageMobilityData).reversed());
                    break;
            }
        }
        // Convert the sorted list to an observable list
        ObservableList<CovidData> sortedObservableList = FXCollections.observableArrayList();
        for (CovidData records : sortedList) {
            sortedObservableList.add(records);
        }
        return sortedObservableList;
    }
    
    /**
     * Retrieves the filtered borough data and returns it as an observable list.
     * @return An observable list of CovidData representing the filtered borough data.
     */
    private ObservableList<CovidData> getData() {
        ObservableList<CovidData> filteredBoroughData = FXCollections.observableArrayList();
        for (CovidData records : filteredBoroughRecords) {
            filteredBoroughData.add(records);
        }
        return filteredBoroughData;
    }
}