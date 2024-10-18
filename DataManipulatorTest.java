import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * The test class DataManipulatorTest.
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class DataManipulatorTest
{
    /**
     * Default constructor for test class DataManipulatorTest
     */
    public DataManipulatorTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }
    
    /**
     * Test case to verify that the getInstance() method returns the same instance of the DataManipulator class.
     */
    @Test
    public void testGetInstance() {       
        DataManipulator instance1 = DataManipulator.getInstance();
        DataManipulator instance2 = DataManipulator.getInstance();
        
        // Check if the two instances are the same
        assertSame(instance1, instance2);
    }
    
    /**
     * Tests the 'checkValidDate' method of the DataManipulator class.
     * Checks if the method correctly validates dates against the stored CovidData records.
     */
    @Test
    public void testCheckValidDate() {
        // Sample CovidData records
        ArrayList<CovidData> sampleRecords = new ArrayList<>();
        sampleRecords.add(new CovidData("2022-01-01", "Borough1", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-02", "Borough2", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        
        // Set up DataManipulator instance with sample records
        DataManipulator dataManipulator = DataManipulator.getInstance();
        dataManipulator.records = sampleRecords;
        
        // Test with a valid date
        LocalDate validDate = LocalDate.of(2022, 1, 1);
        assertTrue(dataManipulator.checkValidDate(validDate));
        
        // Test with an invalid date
        LocalDate invalidDate = LocalDate.of(2022, 1, 3);
        assertFalse(dataManipulator.checkValidDate(invalidDate));
    }
    
    /**
     * Tests the 'getFilterByBorough' method of the DataManipulator class.
     * Checks if the method correctly filters CovidData records by borough name and selected range.
     */
    @Test
    public void testGetFilterByBorough() {
        // Sample CovidData records
        ArrayList<CovidData> sampleRecords = new ArrayList<>();
        sampleRecords.add(new CovidData("2022-01-01", "Borough1", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-02", "Borough2", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-03", "Borough1", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-04", "Borough2", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        
        // Set up DataManipulator instance with sample records
        DataManipulator dataManipulator = DataManipulator.getInstance();
        dataManipulator.records = sampleRecords;
        
        // Set up recordsInRange
        ArrayList<CovidData> recordsInRange = new ArrayList<>();
        recordsInRange.add(sampleRecords.get(0));
        recordsInRange.add(sampleRecords.get(1));
        
        
        // Set recordsInRange in DataManipulator
        dataManipulator.recordsInRange = recordsInRange;
        
        // Test filtering by Borough1 with selected range
        String boroughName = "Borough1";
        boolean selectedRange = true;
        ArrayList<CovidData> filteredRecords = dataManipulator.getFilterByBorough(boroughName, selectedRange);
        
        assertEquals(1, filteredRecords.size()); // Expecting one record in filteredRecords
        assertEquals(sampleRecords.get(0), filteredRecords.get(0)); // Expecting first record
        
        // Test filtering by Borough2 with selected range
        boroughName = "Borough2";
        selectedRange = true;
        filteredRecords = dataManipulator.getFilterByBorough(boroughName, selectedRange);
        
        assertEquals(1, filteredRecords.size()); // Expecting one record in filteredRecords
        assertEquals(sampleRecords.get(1), filteredRecords.get(0)); // Expecting first record
        
        // Test filtering by Borough2 without selected range
        boroughName = "Borough2";
        selectedRange = false;
        filteredRecords = dataManipulator.getFilterByBorough(boroughName, selectedRange);
        
        assertEquals(2, filteredRecords.size()); // Expecting two records in filteredRecords
        assertEquals(sampleRecords.get(1), filteredRecords.get(0)); // Expecting second record in sampel Records
        assertEquals(sampleRecords.get(3), filteredRecords.get(1)); // Expecting fourth record in sampel Records
    }
    
    /**
     * Tests the 'getNewestTotalDeath' method of the DataManipulator class.
     * Checks if the method correctly retrieves the total deaths from the newest CovidData record.
     */
    @Test
    public void testGetNewestTotalDeath() {
        // Sample CovidData records
        ArrayList<CovidData> sampleRecords = new ArrayList<>();
        sampleRecords.add(new CovidData("2022-01-03", "Borough1", -1, -1, -1, -1, -1, -1, -1, -1, -1, -1));
        sampleRecords.add(new CovidData("2022-01-01", "Borough1", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-02", "Borough1", 2, 2, 2, 2, 2, 2, 2, 2, 2, 2));
        sampleRecords.add(new CovidData("2022-01-04", "Borough1", 3, 3, 3, 3, 3, 3, 3, 3, 3, 3));
        
        // Set up DataManipulator instance with sample records
        DataManipulator dataManipulator = DataManipulator.getInstance();
        dataManipulator.records = sampleRecords;
        
        // Test with filteredBoroughRecords containing sampleRecords
        int newestTotalDeath = dataManipulator.getNewestTotalDeath(sampleRecords);
        assertEquals(3, newestTotalDeath); // Expecting total deaths from the newest record (3)
    }
    
    /**
     * Tests the 'updateRecordsInRange' method of the DataManipulator class.
     * Checks if the method correctly updates the 'recordsInRange' list based on the specified date range.
     */
    @Test
    public void testUpdateRecordsInRange() {
        // Sample CovidData records
        ArrayList<CovidData> sampleRecords = new ArrayList<>();
        sampleRecords.add(new CovidData("2022-01-01", "Borough1", 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        sampleRecords.add(new CovidData("2022-01-02", "Borough1", 2, 2, 2, 2, 2, 2, 2, 2, 2, 2));
        sampleRecords.add(new CovidData("2022-01-03", "Borough1", 3, 3, 3, 3, 3, 3, 3, 3, 3, 3));
        sampleRecords.add(new CovidData("2022-01-04", "Borough1", 4, 4, 4, 4, 4, 4, 4, 4, 4, 4));
        
        // Set up DataManipulator instance with sample records
        DataManipulator dataManipulator = DataManipulator.getInstance();
        dataManipulator.records = sampleRecords;
        
        // Set from and to dates
        LocalDate fromDate = LocalDate.of(2022, 1, 02);
        LocalDate toDate = LocalDate.of(2022, 1, 03);
        dataManipulator.setFromDate(fromDate);
        dataManipulator.setToDate(toDate);
        
        // Call updateRecordsInRange
        dataManipulator.updateRecordsInRange();
        
        // Check if recordsInRange is correctly updated
        ArrayList<CovidData> expectedRecordsInRange = new ArrayList<>();
        expectedRecordsInRange.add(sampleRecords.get(1)); // Expecting second record
        expectedRecordsInRange.add(sampleRecords.get(2)); // Expecting third record
        
        assertEquals(expectedRecordsInRange, dataManipulator.getRecordsInRange());
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
