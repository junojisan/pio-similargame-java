package com.gmail.jimaoka.sfdc.service;

import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * SalesforceのデータをPartner APIを利用して取得するサービスクラスです
 * @author junji.imaoka
 */
public class SfdcService {
	private static final String USER_NAME = "<YOUR USER NAME>";
	private static final String USER_PASSWORD = "<YOUR PASSWORD>";
	private static final String AUTH_END_POINT = "https://login.salesforce.com/services/Soap/u/36.0";
	private static final Integer QUERY_BATCH_SIZE = 2000;

	private PartnerConnection connection = null;

	/**
	 * コンストラクタ
	 * @param
	 * @return SfdcService
	 */
	private SfdcService(){
		login();
	}

	/**
	 * SfdcServiceを使用します
	 * @param
	 * @return SfdcService
	 */
	public static SfdcService use(){
		return new SfdcService();
	}

	/**
	 * Salesforceにログインします
	 * @param
	 * @return
	 */
	private void login(){
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(USER_NAME);
		config.setPassword(USER_PASSWORD);
		config.setAuthEndpoint(AUTH_END_POINT);
		try {
			connection = new PartnerConnection(config);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 顧客レコードを取得します
	 * @param
	 * @return
	 */
	public List<Customer> getCustomers(){
		List<Customer> customers = new ArrayList<Customer>();
		try{
			connection.setQueryOptions(QUERY_BATCH_SIZE);
			String soql = "Select Id, Name, jima__CustomerNumber__c From jima__Customer__c Order by jima__CustomerNumber__c";
			QueryResult qr = connection.query(soql);
			boolean done = false;
			while(!done){
				SObject[] records = qr.getRecords();
				for(int i = 0; i < records.length; i++){
					SObject sObj = records[i];
					Customer customer = new Customer();
					customer.setName(String.valueOf(sObj.getField("Name")));
					customer.setCustomerNumber(String.valueOf(sObj.getField("jima__CustomerNumber__c")));
					customers.add(customer);
				}
				if(qr.isDone()){
					done = true;
				}else{
					qr = connection.queryMore(qr.getQueryLocator());
				}
			}
		}catch(ConnectionException ce){
			ce.printStackTrace();
		}
		return customers;
	}

	/**
	 * ゲームレコードを取得します
	 * @param
	 * @return
	 */
	public List<Game> getGames(){
		List<Game> games = new ArrayList<Game>();
		try{
			String soql = "Select Id, Name, jima__GameNumber__c, jima__Genre__c, jima__Maker__c, jima__Image__c From jima__Game__c Order by jima__GameNumber__c";
			QueryResult qr = connection.query(soql);
			boolean done = false;
			while(!done){
				SObject[] records = qr.getRecords();
				for(int i = 0; i < records.length; i++){
					SObject sObj = records[i];
					Game game = new Game();
					game.setName(String.valueOf(sObj.getField("Name")));
					game.setGameNumber(String.valueOf(sObj.getField("jima__GameNumber__c")));
					game.setGenre(String.valueOf(sObj.getField("jima__Genre__c")));
					games.add(game);
				}
				if(qr.isDone()){
					done = true;
				}else{
					qr = connection.queryMore(qr.getQueryLocator());
				}
			}
		}catch(ConnectionException ce){
			ce.printStackTrace();
		}
		return games;
	}

	/**
	 * お気に入りレコードを取得します
	 * @param
	 * @return
	 */
	public List<Favorite> getFavorites(){
		List<Favorite> favorites = new ArrayList<Favorite>();
		try{
			String soql = "Select Id, Name, jima__Customer__c, jima__CustomerNumber__c, jima__Game__c, jima__GameNumber__c From jima__Favorite__c Order by Name";
			QueryResult qr = connection.query(soql);
			boolean done = false;
			while(!done){
				SObject[] records = qr.getRecords();
				for(int i = 0; i < records.length; i++){
					SObject sObj = records[i];
					Favorite favorite = new Favorite();
					favorite.setFavoriteNumber(String.valueOf(sObj.getField("Name")));
					favorite.setCustomerNumber(String.valueOf(sObj.getField("jima__CustomerNumber__c")));
					favorite.setGameNumber(String.valueOf(sObj.getField("jima__GameNumber__c")));
					favorites.add(favorite);
				}
				if(qr.isDone()){
					done = true;
				}else{
					qr = connection.queryMore(qr.getQueryLocator());
				}
			}
		}catch(ConnectionException ce){
			ce.printStackTrace();
		}
		return favorites;
	}

	/**
	 * 顧客インナークラス
	 */
	public class Customer{
		private String name;
		private String customerNumber;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCustomerNumber() {
			return customerNumber;
		}
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}
	}

	/**
	 * ゲームインナークラス
	 */
	public class Game{
		private String name;
		private String genre;
		private String gameNumber;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getGenre() {
			return genre;
		}
		public void setGenre(String genre) {
			this.genre = genre;
		}
		public String getGameNumber() {
			return gameNumber;
		}
		public void setGameNumber(String gameNumber) {
			this.gameNumber = gameNumber;
		}
	}

	/**
	 * お気に入りインナークラス
	 */
	public class Favorite{
		private String favoriteNumber;
		private String customerNumber;
		private String gameNumber;
		public String getFavoriteNumber() {
			return favoriteNumber;
		}
		public void setFavoriteNumber(String favoriteNumber) {
			this.favoriteNumber = favoriteNumber;
		}
		public String getCustomerNumber() {
			return customerNumber;
		}
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
		}
		public String getGameNumber() {
			return gameNumber;
		}
		public void setGameNumber(String gameNumber) {
			this.gameNumber = gameNumber;
		}
	}
}
