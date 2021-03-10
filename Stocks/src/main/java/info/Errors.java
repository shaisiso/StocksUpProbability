package info;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * this is general form for errors
 * 
 * @author Shai Siso
 * @version March 2021
 */
public class Errors {
/**
 * text for the error message 
 */

    @FXML
    private Text txt;

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView errIm;

	/**
	 * method get string of error and set the text in the scene
	 * 
	 * @param error message to show
	 * @throws IOException
	 */
	public void setMsg(String error) throws IOException {
		txt.setText(error);
		System.out.println(error);
	}

	/**
	 * this method close this screen
	 * 
	 * @param event click on close button
	 */
	@FXML
	void closeScreen(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}
/**
 * this method upload the pop up of this page
 * @param root
 */
	public void start(Parent root) {
		Scene scene = new Scene(root);
		Stage primaryStage=new Stage();
		primaryStage.setTitle("Error");
		primaryStage.setScene(scene);
		primaryStage.show();		
	}
}
