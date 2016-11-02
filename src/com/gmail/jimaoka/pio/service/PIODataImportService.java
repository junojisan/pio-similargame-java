package com.gmail.jimaoka.pio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.jimaoka.sfdc.service.SfdcService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import io.prediction.APIResponse;
import io.prediction.EventClient;
import io.prediction.FutureAPIResponse;

/**
 * PredictionIOのEventServerにEventを送信するサービスクラスです
 * @author junji.imaoka
 */
public class PIODataImportService {
	private static final String APP_URL = "<YOUR EVENT SERVER URL>";
	private static final String ACCESS_KEY = "<YOUR ACCESS KEY>";

	/**
	 * 顧客レコードを追加します
	 * @param
	 * @return
	 */
	public void addCustomers(){
		EventClient client = new EventClient(ACCESS_KEY, APP_URL);
		List<FutureAPIResponse> futureAPIResponses = new ArrayList<>();

		try {
			for(SfdcService.Customer customer : SfdcService.use().getCustomers()){
				Map<String, Object> emptyUserProperties = new HashMap<String, Object>();
					FutureAPIResponse future = client.setUserAsFuture(customer.getCustomerNumber(), emptyUserProperties);
					futureAPIResponses.add(future);
					Futures.addCallback(future.getAPIResponse(), getFutureCallback("user " + customer.getCustomerNumber()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	/**
	 * ゲームレコードを追加します
	 * @param
	 * @return
	 */
	public void addGames(){
		EventClient client = new EventClient(ACCESS_KEY, APP_URL);
		List<FutureAPIResponse> futureAPIResponses = new ArrayList<>();

		try {
			for(SfdcService.Game game : SfdcService.use().getGames()){
				Map<String, Object> itemProperties = new HashMap<String, Object>();
					List<String> genres = new ArrayList<String>();
					genres.add(game.getGenre());
					itemProperties.put("genre", genres);
					FutureAPIResponse future = client.setItemAsFuture(game.getGameNumber(), itemProperties);
					futureAPIResponses.add(future);
					Futures.addCallback(future.getAPIResponse(), getFutureCallback("item " + game.getGameNumber()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	/**
	 * お気に入りレコードを追加します
	 * @param
	 * @return
	 */
	public void addFavorites(){
		EventClient client = new EventClient(ACCESS_KEY, APP_URL);
		List<FutureAPIResponse> futureAPIResponses = new ArrayList<>();
		try {
			for(SfdcService.Favorite favorite : SfdcService.use().getFavorites()){
				Map<String, Object> emptyActionItemProperties = new HashMap<String, Object>();
					FutureAPIResponse future = client.userActionItemAsFuture("view", favorite.getCustomerNumber(), favorite.getGameNumber(), emptyActionItemProperties);
					futureAPIResponses.add(future);
					Futures.addCallback(future.getAPIResponse(), getFutureCallback("actionItem customer " + favorite.getCustomerNumber() + " game " + favorite.getGameNumber()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}
	}

	/**
	 * EventServerとのコールバック処理です
	 * @param name
	 * @return FutureCallback<APIResponse>
	 */
	private FutureCallback<APIResponse> getFutureCallback(final String name) {
		return new FutureCallback<APIResponse>(){
			@Override
			public void onSuccess(APIResponse response) {
				System.out.println(name + " added: " + response.getMessage());
			}

			@Override
			public void onFailure(Throwable thrown) {
				System.out.println("failed to add " + name + ": " + thrown.getMessage());
			}
		};
	}
}
