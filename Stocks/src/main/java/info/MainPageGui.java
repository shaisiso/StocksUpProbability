package info;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    
    public static float[] priceVol=new float[2];//[0]-price change, [1]-traded volume
    @FXML
    void showDetails(ActionEvent event) throws InterruptedException {
		try {
			priceVol=StockProb.getPriceChangeAndTradedVolume(symbolTxt.getText(), Integer.parseInt(daysTxt.getText()));
			float shsFloat=StockProb.getSharesFloat(symbolTxt.getText());
			priceTxt.setText("Change in price: "+priceVol[0]+"%");
			volumeTxt.setText("Traded volume: "+((float)priceVol[1]/shsFloat)*100+"%");

		} catch (IOException e) {
			e.printStackTrace();
		}
    }


    @FXML
    void showInfo(ActionEvent event) {

    }

    @FXML
    void showProbTable(ActionEvent event) throws IOException {
    	Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/info/probTable.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Probability table");
		primaryStage.setScene(scene);
		primaryStage.show();
    }

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/info/MainPageGui.fxml"));
		Scene scene = new Scene(root);
	//	scene.getStylesheets().add(getClass().getResource("/gui/Buttons.css").toExternalForm());
		primaryStage.setTitle("Probability check");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}

}
