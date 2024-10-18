import javafx.fxml.*;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
import javafx.scene.Parent;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

/**
 * Controller for the map page
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class MapController
{   
    @FXML
    private AnchorPane root;
    
    private ArrayList<String> boroughNames;
    
    // Hash map to associate the abbreviation of the boroughs to the full name of them as stored in the data
    private HashMap<String, String> boroughDictionary = new HashMap<String, String>() {{
        put("ENFI", "Enfield");
        put("WALT", "Waltham Forest");
        put("HRGY", "Haringey");
        put("BARN", "Barnet");
        put("BREN", "Brent");
        put("CAMD", "Camden");
        put("ISLI", "Islington");
        put("HACK", "Hackney");
        put("REDB", "Redbridge");
        put("HRRW", "Harrow");
        put("HAVE", "Havering");
        put("HILL", "Hillingdon");
        put("EALI", "Ealing");
        put("KENS", "Kensington And Chelsea");
        put("WSTM", "Westminster");
        put("TOWH", "Tower Hamlets");
        put("NEWH", "Newham");
        put("BARK", "Barking And Dagenham");
        put("HOUN", "Hounslow");
        put("HAMM", "Hammersmith And Fulham");
        put("WAND", "Wandsworth");
        put("CITY", "City Of London");
        put("GWCH", "Greenwich");
        put("BEXL", "Bexley");
        put("LEWS", "Lewisham");
        put("STHW", "Southwark");
        put("RICH", "Richmond Upon Thames");
        put("MERT", "Merton");
        put("LAMB", "Lambeth");
        put("KING", "Kingston Upon Thames");
        put("SUTT", "Sutton");
        put("CROY", "Croydon");
        put("BROM", "Bromley");
    }};
    
    //Instance variable for Singleton Class Data Manipulator 
    private DataManipulator dataManipulator;
    
    /**
     * This method is called as soon as the FXML files are loaded.
     */
    @FXML
    private void initialize() {
        // Retrieve the instance of the data manipulator
        dataManipulator = DataManipulator.getInstance();
        
        boroughNames = new ArrayList<>();
        
        // Iterate over all the buttons of root element
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                String buttonText = button.getText();
                
                // Retrieve the corresponding borough name from the dictionary
                String boroughName = boroughDictionary.get(buttonText);
                
                boroughNames.add(boroughName);
                
                // Retrieve filtered borough records
                ArrayList<CovidData> filteredBoroughRecords = dataManipulator.getFilterByBorough(boroughName, false);
                
                // Get the total deaths for the borough
                int totalDeaths = dataManipulator.getNewestTotalDeath(filteredBoroughRecords);
                
                // Determine the color for the button based on total deaths
                String buttonColour = determineButtonColour(totalDeaths);
                
                // Update the button's color
                updateButtonColour(button, buttonColour);
            }
        }
    }
    
    /**
     * Update the buttons colour
     */
    private void updateButtonColour(Button button, String colour) {
        button.setStyle(button.getStyle() + "-fx-background-color: " + colour + ";");
    }
    
    /**
     * Determine the colour based on amount of total deaths
     */
    private String determineButtonColour(int totalDeaths) {
        if (totalDeaths >= 343 && totalDeaths <= 552) {
            return "#66FF99";
        } else if (totalDeaths >= 553 && totalDeaths <= 762) {
            return "#FFFF66";
        } else if (totalDeaths >= 763 && totalDeaths <= 972) {
            return "#FFCC66";
        } else if (totalDeaths >= 973 && totalDeaths <= 1182) {
            return "#FF6666";
        } else {
            return "#CCCCCC";
        }

    }
    
    /**
     * Open new window if the buttons are clicked providing more info about the specifc borough
     */
    @FXML
    private void openNewWindow(ActionEvent event){
        Button button = (Button) event.getSource();
        String shortName = button.getText();
        String name = boroughDictionary.get(shortName);
        
        // Now only get records within the time period not general
        ArrayList<CovidData> filteredRecords = dataManipulator.getFilterByBorough(name, true);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("borough_data.fxml"));
            Parent root = loader.load();
            
            BoroughDataController controller = loader.getController();            
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(name + " Data");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}