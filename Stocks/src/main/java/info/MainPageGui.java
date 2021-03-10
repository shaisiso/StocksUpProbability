package info;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * this is the gui for the software
 * 
 * @author Shai Siso
 * @version March 2021
 */
public class MainPageGui {

	@FXML
	private TextField symbolTxt;

	@FXML
	private TextField daysTxt;

	@FXML
	private Button chackBtn;

	@FXML
	private Text priceTxt;

	@FXML
	private Text volumeTxt;

	@FXML
	private Button infoBtn;

	@FXML
	private Button prboBtn;


	public static float[] priceVol = new float[2];// [0]-price change, [1]-traded volume
	public static boolean isOpen = false;

	/**
	 * this function call to the methods in StockProb class and display the result
	 * on the screen
	 * 
	 * @param event - click on check button
	 * @throws IOException
	 */
	@FXML
	void showDetails(ActionEvent event) throws IOException {
		Main.exception = false;
		priceTxt.setText("load");
		String errMsg = "Enter valid number of trading days", stockSymbol = symbolTxt.getText();
		int numOfDays;
		float shsFloat;
		try {
			if (stockSymbol.isEmpty() || daysTxt.getText().isEmpty()) {
				errMsg = "Please enter stock symbol and number of trading days";
				throw new Exception();
			}
			numOfDays = Integer.parseInt(daysTxt.getText());
			if (numOfDays < 1)
				throw new Exception();
			errMsg = "Cant find details about the requested stock";

			Thread t = new MyThread(stockSymbol, numOfDays);
			t.start();
			shsFloat=StockProb.getSharesFloat(stockSymbol);
			synchronized (Main.monitor) {
				try {
					if (Main.first) {
						Main.first = false;
						Main.monitor.wait();
					}
				} catch (InterruptedException e) {
				}
			}
			if (Main.exception)
				throw new Exception();
			priceVol=StockProb.percents;
			priceTxt.setText("Change in price: " + priceVol[0] + "%");
			volumeTxt.setText("Traded volume: " + ((float) priceVol[1] / shsFloat) * 100 + "%");

		} catch (Exception e) {
			// error
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/info/Errors.fxml").openStream());
			Errors error = loader.getController();
			error.setMsg(errMsg);
			error.start(root);
		}
	}

	/**
	 * this method open pop up with information about using and copyrights
	 * 
	 * @param event - click on information button
	 */
	@FXML
	void showInfo(ActionEvent event) {

	}

	/**
	 * this method open pop up with the probability table
	 * 
	 * @param event - click on probability table button
	 * @throws IOException
	 */
	@FXML
	void showProbTable(ActionEvent event) throws IOException {
		if (!isOpen) {
			isOpen = true;
			Stage primaryStage = new Stage();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent event) {
					isOpen = false;
				}
			});
			Parent root = FXMLLoader.load(getClass().getResource("/info/probTable.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Probability table");
			primaryStage.setScene(scene);
			primaryStage.show();
		}

	}

	/**
	 * this method will start presenting the page
	 * 
	 * @param primaryStage
	 * @throws IOException
	 */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/info/MainPageGui.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Probability check");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
