package info;

import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javafx.application.Application;
import javafx.stage.Stage;

public class StockProb extends Application {
	public static float percents[]=new float[2];
	public static void main(String[] args) {
		
//		final String stockSymbol;
//		Scanner scan;
//		final int numOfDays;
//		// get input
//		scan = new Scanner(System.in);
//		System.out.println("please provide the stock symbol");
//		stockSymbol = scan.next();
//		System.out.println("please provide the number of trading days to check");
//		numOfDays = scan.nextInt();
//		// search
//		int shsFloat;
//		try {
//	    	MyThread t = new MyThread("prob",stockSymbol, numOfDays);
//	    	t.start();
//			shsFloat = getSharesFloat(stockSymbol);
//			System.out.println(shsFloat);
//
//			while(percents[1]==0);
//			System.out.println("% of price change = " + percents[0] + "\n% traded volume = " + ((float)percents[1]/shsFloat)*100);
//
//		} catch (IOException e) {
//			if (!stockSymbol.equals("stop")) {
//				System.out.println("no such stock " + stockSymbol);
//			}
//		} catch (NumberFormatException e) {
//			System.out.println("can't find " + stockSymbol + " shares float");
//			// -----------try Yahoo------------------
//			e.printStackTrace();
//		}
//
//		scan.close();
		launch(args);
		} // end main

		/**
		 * this method upload to the screen the main page
		 */
		@Override
		public void start(Stage primaryStage) throws Exception {
			MainPageGui mainPage = new MainPageGui();
			mainPage.start(primaryStage);
		}


	/**
	 * This method calculate change in price(%) and traded volume for requested period
	 * of time
	 * 
	 * @param stockSymbol - symbol of the stock
	 * @param numOfDays   - number of candles to check
	 * @param shsFloat    - shares float of the stock
	 * @return percents - Float array, [0] - % change in price, [1] - % of volume
	 *         that was traded(stocks that switched hands)
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static float[] getPriceChangeAndTradedVolume(String stockSymbol, int numOfDays)
			throws IOException, NumberFormatException {
		float change, percents[] = new float[2];// percents[0]=% price change, percents[1]= % traded volume
		int volume = 0;
		// find stock - investing.com
		String url = "https://www.investing.com/search/?q=" + stockSymbol;
		Document doc;
		doc = Jsoup.connect(url).get();
		Element link = doc.select("a.js-inner-all-results-quote-item.row").first();
		String addition = link.attr("href"); // get url for stock page
		if (addition.contains("?cid")) // for some different url
			addition = addition.substring(0, addition.indexOf("?")) + "-historical-data"
					+ addition.substring(addition.indexOf("?"));
		else
			addition += "-historical-data";
		String stockHistoricalURL = "https://www.investing.com/" + addition;
		doc = Jsoup.connect(stockHistoricalURL).get();

		Elements table = doc.select("#curr_table > tbody");
		Elements rows = table.select("tr");
		int offset=0;
		if (dateIsToday(rows.get(0).children().get(0).text()))
				offset=1;
		float last = Float.parseFloat(rows.get(offset).children().get(1).text());
		System.out.println("last " + last);
		float from = Float.parseFloat(rows.get(numOfDays+offset).children().get(1).text());
		System.out.println("from " + from);
		change = (last / from - 1) * 100;// change percent for the period of time
		boolean first=true;
		for (Element row : rows) {
			if (numOfDays == 0)
				break;
			if (first==true && offset==1) {
				first=false;
				continue;
			}
			System.out.println(row.children().get(5).text());
			volume += getFloatFromTxt(row.children().get(5).text());
			System.out.println(row.children().get(0).text());
			numOfDays--;
		}

		System.out.println("traded volume " + volume);
		percents[0] = change;
		percents[1] = (volume);
		return percents;
	}

	private static boolean dateIsToday(String date) {
		String firstDay = date.substring(date.indexOf(' ') + 1, date.indexOf(' ') + 3);
		String thisDay = java.time.LocalDate.now().toString().substring(8);
		return firstDay.equals(thisDay);
	}

	/**
	 * this method get stock symbol and return the number of shares float by using
	 * api from Finviz
	 * 
	 * @param stockSymbol - symbol of the stock
	 * @return - shares float of the stock
	 * @throws IOException
	 */
	public static int getSharesFloat(String stockSymbol) throws IOException {
		String shsFloatTxt = "";
		int shsFloat;
		String url = "https://finviz.com/quote.ashx?t=" + stockSymbol;
		Document doc = Jsoup.connect(url).get();
		// get shares float
		Elements links = doc.select("td.snapshot-td2:nth-of-type(10)");
		shsFloatTxt = links.get(1).text();
		System.out.println("shs float=" + shsFloatTxt);
		shsFloat = getFloatFromTxt(shsFloatTxt);
		return shsFloat;
	}

	/**
	 * this method change number text in format [n.nK/M/B] to the value of it
	 * 
	 * @param numberTxt - the number to change from the mentioned format (ex. 3.03M)
	 * @return - integer of the number (ex. 3030000)
	 * @throws IOException
	 */
	private static int getFloatFromTxt(String numberTxt) throws IOException {
		float fl = Float.parseFloat(numberTxt.substring(0, numberTxt.length() - 1));
		switch (numberTxt.charAt(numberTxt.length() - 1)) {
		case 'K':
			return (int) (fl * 1000);
			// break;
		case 'M':
			return (int) (fl * 1000000);
			// break;
		case 'B':
			return (int) (fl * 1000000000);
			// break;
		default:
			throw new IOException();
		}

	}

}
