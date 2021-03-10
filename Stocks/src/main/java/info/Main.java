package info;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static Object monitor = new Object();
	public static boolean first = true;
	public static boolean exception=false;
	public static void main(String[] args)  {
		 launch(args);
	} // end main

	/**
	 * this method upload to the screen the main page //
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainPageGui mainPage = new MainPageGui();
		mainPage.start(primaryStage);
	}
}
