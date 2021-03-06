package info;

/**
 * this class is to get the information from investing.com in parallel to the main thread that search other information
 * @author Shai Siso
 *
 */
public class MyThread extends Thread {

	private String stockSymbol;
	private int numOfDays;

	public MyThread(String stockSymbol, int numOfDays) {
		this.numOfDays = numOfDays;
		this.stockSymbol = stockSymbol;
	}

	@Override
	public void run() {
		synchronized (Main.monitor) {
			Main.first = false;
			try {
				StockProb.getPriceChangeAndTradedVolume(stockSymbol, numOfDays);
				Main.monitor.notifyAll();
			} catch (Exception e) {
				Main.exception=true;
			}

		}
	}
}
