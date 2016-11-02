package com.gmail.jimaoka.main;

import com.gmail.jimaoka.pio.service.PIODataImportService;

/**
 * PredictionIOにSalesforceのデータをインンポートするアプリ
 * @author junji.imaoka
 */
public class PIOCmdLineSampleApp {
	/**
	 * mainメソッドです
	 * @param args
	 * @return
	 */
	public static void main(String[] args) {
		PIODataImportService service = new PIODataImportService();

		service.addCustomers();
		System.out.println("--- User import done ---");

		service.addGames();
		System.out.println("--- Item import done ---");

		service.addFavorites();
		System.out.println("--- UserActionItem import done ---");
	}
}
