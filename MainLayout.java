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

/**
 * MainLayout is the entry point for the JavaFX application. It initializes the main 
 * layout of the application window, loads FXML files for UI components, sets up controllers, 
 * and displays the initial scene for viewing COVID-19 data.
 *
 *
 * @author Rojus Cesonis, William Costales, Ye Win, Ruijie Li
 * @version 03/2024
 */
public class MainLayout extends Application
{
    private BorderPane rootPane;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        rootPane = loader.load();
        rootPane.getStylesheets().add("styles.css");
        MainLayoutController mainLayoutController = loader.getController();
        mainLayoutController.setRootPane(rootPane);
        
        loader = new FXMLLoader(getClass().getResource("welcome_1.fxml"));
        Parent welcomePane = loader.load();
        rootPane.setCenter(welcomePane);
        WelcomePanelController welcomePanelController = loader.getController();
        
        primaryStage.setTitle("Covid 19 Data Viewer");
        primaryStage.setScene(new Scene(rootPane));
        primaryStage.show();
    }

}