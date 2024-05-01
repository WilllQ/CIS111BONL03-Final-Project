import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.*;

public class CurrencyConverterApp extends Application{
   public static void main(String[] args) {
      launch(args);
   }
   
   @Override
   public void start(Stage window) throws Exception {
      try {
         Parent root = FXMLLoader.load(getClass().getResource("CurrencyFXML.fxml"));    
         Scene scene = new Scene(root);      
         
         window.setTitle("Currency Converter");
         window.setScene(scene);
         window.show();
         
      } catch (Exception e) {
         e.printStackTrace();
         this.stop();
      }
   }
   
   @Override
   public void stop() {
      System.out.println("Stop is called in Application class");
   }
}
   
   