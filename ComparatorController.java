import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import java.util.ArrayList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Controller for the comparator page (challenge task)
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class ComparatorController
{
    @FXML
    private ComboBox<String> boroughComboBox1;
    
    @FXML
    private ComboBox<String> boroughComboBox2;
    
    @FXML
    private BarChart<String, Integer> barChart;
    
    @FXML
    private CheckBox checkBoxCases;

    @FXML
    private CheckBox checkBoxDeaths;
    
    private DataManipulator dataManipulator;
    
    /**
     * This method is called as soon as the FXML files are loaded.
     */
    @FXML
    private void initialize() {
        // Singleton instance of data manipulator
        dataManipulator = DataManipulator.getInstance();
        // Define a list of borough names
        ObservableList<String> boroughs = FXCollections.observableArrayList(
            "Kingston Upon Thames",
            "Hammersmith And Fulham",
            "Hackney",
            "Barking And Dagenham",
            "Tower Hamlets",
            "Lewisham",
            "Islington",
            "Bexley",
            "Wandsworth",
            "Haringey",
            "Enfield",
            "Sutton",
            "Merton",
            "Havering",
            "Brent",
            "Hounslow",
            "Camden",
            "Greenwich",
            "Harrow",
            "Ealing",
            "Southwark",
            "Newham",
            "Kensington And Chelsea",
            "City Of London",
            "Bromley",
            "Hillingdon",
            "Redbridge",
            "Croydon",
            "Richmond Upon Thames",
            "Barnet",
            "Westminster",
            "Waltham Forest",
            "Lambeth"
        );

        // Populate the ComboBox with the list of borough names
        boroughComboBox1.setItems(boroughs);
        boroughComboBox2.setItems(boroughs);
    }
    
    /**
     * Retrieves the total data value for the specified borough and data type.
     * 
     * @param selectedBorough The name of the borough for which data is retrieved.
     * @param neededDataType The column of data needed, such as "New Cases" or "New Deaths".
     * @return The total data value for the specified borough and data type.
     */
    private int getBarChartData(String selectedBorough, String neededDataType) {
        int totalDataValue = 0;
        int dataValue = -1;
        
        // Filter records for the specified borough
        ArrayList<CovidData> filteredBoroughRecords = dataManipulator.getFilterByBorough(selectedBorough, true);
        
        for (CovidData record : filteredBoroughRecords) {
            // Retrieve data value based on the needed data type
            if (neededDataType.equals("New Cases")) {
                dataValue = record.getNewCases();
            } else if (neededDataType.equals("New Deaths")) {
                // Adjust new deaths by a scale of 100 for better visualization on the bar chart
                dataValue = (record.getNewDeaths() * 100);
            }
            
            // Accumulate valid data values
            if (dataValue != -1) {
                totalDataValue += dataValue;
            }
        }
        
        return totalDataValue;
    }
    
    /**
     * Updates the bar chart with data based on the selected boroughs and checkboxes.
     */
    private void updateBarChart() {
        // Clear existing data in the bar chart
        barChart.getData().clear();
        
        String selectedBorough1 = boroughComboBox1.getValue();
        String selectedBorough2 = boroughComboBox2.getValue();
        boolean newCasesSelected = checkBoxCases.isSelected();
        boolean newDeathsSelected = checkBoxDeaths.isSelected();
        
        // Proceed only if both boroughs are selected and at least one checkbox is selected
        if (selectedBorough1 != null && selectedBorough2 != null && (newCasesSelected || newDeathsSelected)) {
            // Add series for new cases if selected
            if (newCasesSelected) {
                int newCases1 = getBarChartData(selectedBorough1, checkBoxCases.getText());
                int newCases2 = getBarChartData(selectedBorough2, checkBoxCases.getText());
                XYChart.Series<String, Integer> series1 = new XYChart.Series<>();
                series1.setName("New Cases");
                series1.getData().add(new XYChart.Data<>(selectedBorough1, newCases1));
                series1.getData().add(new XYChart.Data<>(selectedBorough2, newCases2));
                barChart.getData().add(series1);
            }
            
            // Add series for new deaths if selected
            if (newDeathsSelected) {
                int newDeaths1 = getBarChartData(selectedBorough1, checkBoxDeaths.getText());
                int newDeaths2 = getBarChartData(selectedBorough2, checkBoxDeaths.getText());
                XYChart.Series<String, Integer> series2 = new XYChart.Series<>();
                series2.setName("New Deaths");
                series2.getData().add(new XYChart.Data<>(selectedBorough1, newDeaths1));
                series2.getData().add(new XYChart.Data<>(selectedBorough2, newDeaths2));
                barChart.getData().add(series2);
            }
        }
    }
    
    /**
     * Handles the selection of boroughs from the combo boxes.
     */
    @FXML
    private void handleBoroughSelection() { 
        // Get the selected boroughs from the combo boxes
        String selectedBorough1 = boroughComboBox1.getValue();
        String selectedBorough2 = boroughComboBox2.getValue();
        
        // Check if both combo boxes have selections
        if (selectedBorough1 != null && selectedBorough2 != null) {
            // Check if the selected boroughs are different
            if (!selectedBorough1.equals(selectedBorough2)) {
                // Set the title of the bar chart to display the selected boroughs for comparison
                barChart.setTitle(selectedBorough1 + "\nvs\n" + selectedBorough2);
                // Update the bar chart with new data
                updateBarChart();
            } else {
                // Boroughs cannot be the same, show alert
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Selected boroughs cannot be the same");
                alert.setContentText("Please select different boroughs for comparison.");
                alert.showAndWait();
                
                // Clear the selection to prompt the user to choose again
                boroughComboBox1.getSelectionModel().clearSelection();
                boroughComboBox2.getSelectionModel().clearSelection();
            }
        }
    }

    
    @FXML
    private void handleDataSelection() { 
        updateBarChart();
    }
}
