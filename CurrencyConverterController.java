import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.util.ResourceBundle;

import java.util.Scanner;

import com.google.gson.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URL;

public class CurrencyConverterController implements Initializable {
   //FXML variables
   @FXML
   private TextField amount1;

   @FXML
   private TextField amount2;

   @FXML
   private ChoiceBox<String> currency1;

   @FXML
   private ChoiceBox<String> currency2;
   
   @FXML
   private Button convertButton;
   
   //Variables required to calculate and process data
   private HttpClient client;
   
   private double entryAmount;
   private String entryCurrency;
   private String neededCurrency;
   private double usdToEurRate;
   private double usdToJpyRate;
   private double usdToPhpRate; 
   
   //Enters amount entered into textfield into entryAmount for calculating
   @FXML
   protected void handleAmount1TextFieldAction(ActionEvent event) {
      String amount1Text = amount1.getText();
      
      try {
         this.entryAmount = Double.parseDouble(amount1Text);
      } catch (NumberFormatException e) {
         System.out.println("Invalid Amount");
      }
   }
   
   //Enters currency selected into entryCurrency for calculating
   @FXML
   protected void handleCurrency1ChoiceBoxAction(ActionEvent event) {
      entryCurrency = currency1.getValue();
   }
   
   //Enters currency selected into neededCurrency for calculating
   @FXML
   protected void handleCurrency2ChoiceBoxAction(ActionEvent event) {
      neededCurrency = currency2.getValue();
   }
   
   //Does conversions when button is pressed
   @FXML
   protected void handleConvertButtonAction(ActionEvent event) {
      if (entryCurrency == "USD")
         amount2.setText(this.convertFromUSD(entryAmount, neededCurrency) + "");
      else if (neededCurrency == "USD")
         amount2.setText(this.convertToUSD(entryAmount, entryCurrency) + "");
      else
         amount2.setText(this.convertCurrency(entryAmount, entryCurrency, neededCurrency) + "");
   }   
   
   //Calculates currency if entryCurrency is USD
   private double convertFromUSD(double amount, String targetCurrency) {
      switch (targetCurrency) {
         case "EUR":
            return amount * usdToEurRate;
         case "JPY":
            return amount * usdToJpyRate;
         case "PHP":
            return amount * usdToPhpRate;
         default:
            System.out.println("Unsupported target currency.");
            return 0.0f;
      }
   }
   
   //Calculates currency if neededCurrency is USD
   private double convertToUSD(double amount, String sourceCurrency) {
      switch (sourceCurrency) {
         case "EUR":
            return amount / usdToEurRate;
         case "JPY":
            return amount / usdToJpyRate;
         case "PHP":
            return amount / usdToPhpRate;
         default:
            System.out.println("Unsupported source currency.");
            return 0.0f;
      }
   }
   
   //Calculates currency if neither is USD
   protected double convertCurrency(double amount, String sourceCurrency, String targetCurrency) {
      if (sourceCurrency.equals("USD")) {
         return convertFromUSD(amount, targetCurrency);
      } else if (targetCurrency.equals("USD")) {
      return convertToUSD(amount, sourceCurrency);
      } else {
         // Convert to USD first, then to the target currency
         double usdAmount = convertToUSD(amount, sourceCurrency);
            return convertFromUSD(usdAmount, targetCurrency);
        }
    }

    // Method to update conversion rates
    protected void updateConversions() {
      if (this.client == null) {
         this.client = HttpClient.newHttpClient();
      }
      
      try {
         HttpClient client = HttpClient.newHttpClient();               
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.currencybeacon.com/v1/latest?api_key=tCjgnc7yZnLQSdre2FIlOUpjhGgGiMIU"))
                                                       .build();
      
         client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
               .thenApply(HttpResponse::body)
               .thenAccept(this::processRates)
               .join();
      }
      catch (Exception e) {
         System.out.println("Issue with API request");
      }
   }

    // Method to get conversion rates for currencies
    private void processRates(String data) {
      Gson gson = new Gson();
      Response response = gson.fromJson(data, Response.class);
      
      this.usdToEurRate = response.getRates().getEUR();
      this.usdToJpyRate = response.getRates().getJPY();
      this.usdToPhpRate = response.getRates().getPHP();
   }
   
   //Initialization that will call for updateConversions to access conversion factors
   @Override
   public void initialize(URL location, ResourceBundle resources) {
   
      updateConversions();
      
      currency1.getItems().addAll("USD", "EUR", "JPY", "PHP");
      currency2.getItems().addAll("USD", "EUR", "JPY", "PHP");
      
   }
}