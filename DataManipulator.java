import java.util.ArrayList;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import com.google.common.annotations.VisibleForTesting;

/**
 * DataManipulator is a singleton class designed to load COVID-19 data records using 
 * CovidDataLoader class and to manage and manipulate COVID-19 data records.
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class DataManipulator
{
    //instance variable to hold the singleton class DataManipulator
    private static DataManipulator instance;
    
    //instance variable for an instance of CovidDataLoader class
    private CovidDataLoader loader;
    
    //Start Date
    private LocalDate toDate;
    
    //End Date
    private LocalDate fromDate;
    
    //Property wrapping a LocalDate object of StartDate
    private final ObjectProperty<LocalDate> toDateProperty = new SimpleObjectProperty<>();
    
    //Property wrapping a LocalDate object of EndDate
    private final ObjectProperty<LocalDate> fromDateProperty = new SimpleObjectProperty<>();
    
    @VisibleForTesting
    public ArrayList<CovidData> records;
    
    @VisibleForTesting
    public ArrayList<CovidData> recordsInRange;
    
    private ArrayList<CovidData> filteredBoroughRecords;
    
    /**
     * Constructor for DataManipulator
     */
    private DataManipulator() {
        loader = new CovidDataLoader();
        records = loader.load();
    }
    
    /**
     * universal access point
     * public method for accessing the Singleton DataManipulator object
     */
    public static DataManipulator getInstance() {
        if (instance == null) {
            instance = new DataManipulator();
        }
        
        return instance;
    }
    
    
    /**
     * Checking whether the LocalDate passed in as a parameter
     * is a valid date from the Covid Database.
     */
    public boolean checkValidDate(LocalDate date) {
        boolean valid = false;
        for (CovidData record: records) {
            LocalDate validDate = record.getLocalDate();
            if (date.equals(validDate)) {
                valid = true;
            }
        }
        return valid;
    }
    
    /**
     * Retrieves COVID-19 data records filtered by a specific borough.
     * 
     * @param boroughName   The name of the borough to filter the records.
     * @param selectedRange A boolean flag indicating whether a selected date range is applied.
     * @return              An ArrayList of CovidData objects filtered by the specified borough.
     */
    public ArrayList<CovidData> getFilterByBorough(String boroughName, boolean selectedRange) {
        ArrayList<CovidData> filteredBoroughRecords = new ArrayList<>(); 
        ArrayList<CovidData> chosenRecords;
        if (selectedRange == true) {
            chosenRecords = recordsInRange;
        }
        else {
            chosenRecords = records;
        }
        
        for (CovidData record: chosenRecords) {
            if (record.getBorough().equals(boroughName)) {
                filteredBoroughRecords.add(record);
            }
        }
        this.filteredBoroughRecords = filteredBoroughRecords;        
        return filteredBoroughRecords;
    }
    
    /**
     * Calculates the total number of deaths from the newest available COVID-19 data record within a filtered borough.
     * 
     * @param filteredBoroughRecords An ArrayList of CovidData objects filtered by a specific borough.
     * @return                       The total number of deaths from the newest available record within the filtered borough.
     */
    public int getNewestTotalDeath(ArrayList<CovidData> filteredBoroughRecords) {
        CovidData newestRecord = null;
        ArrayList<CovidData> emptyDeathRecords = new ArrayList<>(); //arraylist used to not include if the newest date has no value for total deaths
        boolean foundNewestDeathRecord = false;
        int totalDeaths = -1;
        
        //while loop until a non empty newest total death record is found
        while (foundNewestDeathRecord == false) {
            LocalDate newestDate = LocalDate.of(2000, 1, 1); //default date
            for (CovidData currentRecord : filteredBoroughRecords) {
               LocalDate currentDate = currentRecord.getLocalDate();
               
               //if the record has already been ruled out as missing total deaths skip it
               if (emptyDeathRecords.contains(currentRecord)) {
                       continue;
                   }
                   
               //update newest date if the current iteration is more recent
               if (newestDate.isBefore(currentDate)) {
                   newestRecord = currentRecord;
                   newestDate = newestRecord.getLocalDate();
               }
            }
            //check if the newest date is not empty if it is add it to the banned arraylist
            totalDeaths = newestRecord.getTotalDeaths();
            if (totalDeaths != -1) {
                foundNewestDeathRecord = true;
            }
            else {
                emptyDeathRecords.add(newestRecord);
            }
        }
        return totalDeaths;
    }
    
    
    /**
     * Returnts the End Date
     */
    public LocalDate getToDate() {
        return toDate;
    }
    
    /**
     * Returnts the Start Date
     */
    public LocalDate getFromDate() {
        return fromDate;
    }
    
    /**
     * Sets both the End Date and the property wrapped End Date to
     * the LocalDate that is passed as a parameter
     */
    public void setToDate(LocalDate date) {
        toDate = date;
        toDateProperty.set(date);
    }
    
    /**
     * Sets both the Start Date and the property wrapped Start Date to
     * the LocalDate that is passed as a parameter
     */
    public void setFromDate(LocalDate date) {
        fromDate = date;
        fromDateProperty.set(date);
    }
    
    /**
     * Returns the property wrapped End Date
     */
    public ObjectProperty<LocalDate> getToDateProperty() {
        return toDateProperty;
    }
    
    /**
     * Returns the property wrapped Start Date
     */
    public ObjectProperty<LocalDate> getFromDateProperty() {
        return fromDateProperty;
    }
    
    /**
     * Updates the list of COVID-19 data records within the specified date range.
     * It iterates through all records, adding those whose dates fall within or equal 
     * to the selected date range (inclusive) to the 'recordsInRange' ArrayList.
     */
    public void updateRecordsInRange() {
        recordsInRange = new ArrayList<>();

        for (CovidData record : records) {
            LocalDate recordDate = record.getLocalDate();
            if (recordDate.isEqual(getFromDate()) || recordDate.isEqual(getToDate()) ||
                (recordDate.isAfter(getFromDate()) && recordDate.isBefore(getToDate())))
                recordsInRange.add(record);
        }
    }
    
    public ArrayList<CovidData> getRecordsInRange() {
        return recordsInRange;
    }
    
    public ArrayList<CovidData> getFilteredRecords() {
        return filteredBoroughRecords;
    }
}
