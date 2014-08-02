package de.opatut.flatman.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.opatut.flatman.AccountAuthenticatorService;
import de.opatut.flatman.data.exceptions.AuthorizationRequiredException;
import de.opatut.flatman.data.exceptions.FormErrorsException;
import de.opatut.flatman.data.exceptions.InvalidCredentialsException;
import de.opatut.flatman.data.exceptions.LoadDataException;
import de.opatut.flatman.data.exceptions.NoGroupException;

public class DataStorage {
	public static final String API_URL = "http://opatut.de:5000/api";

	private static DataStorage sInstance = null;

	private List<DataUpdateListener> mDataUpdateListeners = new ArrayList<DataUpdateListener>();
	private List<DataUpdateErrorListener> mDataUpdateErrorListeners = new ArrayList<DataUpdateErrorListener>();

	public Group group = new Group();
	public User user = new User();

	private String mAuthToken = null;

	private Exception mUpdateError;

	private DataStorage() {
	}

    public void init(Context context) {
        Account account = getSavedAccount(context);
        if(account != null) {
            mAuthToken = AccountManager.get(context).getPassword(account);
        }
    }

    public Account getSavedAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticatorService.ACCOUNT_TYPE);
        if (accounts.length == 1) {
            return accounts[0];
        } else if (accounts.length > 1) {
            for (Account a : accounts) {
                accountManager.removeAccount(a, null, null);
            }
        }
        return null;
    }

	public static DataStorage getInstance() {
		if (sInstance == null) {
			sInstance = new DataStorage();
		}
		return sInstance;
	}

	public static Gson getGson() {
		return new GsonBuilder().serializeNulls()
				.setDateFormat(DateFormat.LONG).setPrettyPrinting()
				.setVersion(1.0).create();
	}

	public void registerDataUpdateListener(DataUpdateListener listener) {
		mDataUpdateListeners.add(listener);
	}

	private void dataUpdated() {
		for (DataUpdateListener listener : mDataUpdateListeners) {
			listener.onDataUpdated(this);
		}
	}

	public void registerDataUpdateErrorListener(DataUpdateErrorListener listener) {
		mDataUpdateErrorListeners.add(listener);
	}

	private void dataUpdateError(Exception e) {
		mUpdateError = e;
		for (DataUpdateErrorListener listener : mDataUpdateErrorListeners) {
			listener.onDataUpdateError(this, e);
		}
	}

	public <T> T load(String url, Map<String, String> postData,
			Class<T> classOfT, final DataUpdateErrorListener errorListener) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;

			HttpPost post = new HttpPost(API_URL + url);

			List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			if (postData != null) {
				for (String k : postData.keySet()) {
					nvpList.add(new BasicNameValuePair(k, postData.get(k)));
				}
			}

			if (mAuthToken != null) {
				nvpList.add(new BasicNameValuePair("auth", mAuthToken));
			}

			post.setEntity(new UrlEncodedFormEntity(nvpList));
			response = httpclient.execute(post);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String json = "", line = "";
			while ((line = br.readLine()) != null) {
				json += line + "\n";
			}

			System.out.println(json);

			JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
			if (!jo.get("success").getAsBoolean()) {
				String msg = jo.get("error_message").getAsString();
				if (msg.equals(AuthorizationRequiredException.IDENTIFIER)) {
					throw new AuthorizationRequiredException();
				} else if (msg.equals(FormErrorsException.IDENTIFIER)) {
					throw new FormErrorsException();
				} else if (msg.equals(InvalidCredentialsException.IDENTIFIER)) {
					throw new InvalidCredentialsException();
				} else if (msg.equals(NoGroupException.IDENTIFIER)) {
					throw new NoGroupException();
				} else {
					throw new LoadDataException(msg);
				}
			}

			return getGson().fromJson(json, classOfT);
		} catch (final Exception e) {
			e.printStackTrace();
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				public void run() {
					if (errorListener != null) {
						errorListener.onDataUpdateError(DataStorage.this, e);
					} else {
						dataUpdateError(e);
					}
				}
			});

			try {
				return classOfT.newInstance();
			} catch (IllegalAccessException e1) {
				return null;
			} catch (InstantiationException e1) {
				return null;
			}
		}
	}

	public void reload() {
		new Thread(new Runnable() {
			public void run() {
				mUpdateError = null;
				user = load("/user", null, User.class, null);
				group = load("/group", null, Group.class, null);

				if (mUpdateError == null) {
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						public void run() {
							dataUpdated();
						}
					});
				}
			}
		}).start();
	}

	public static interface DataUpdateListener {
		public void onDataUpdated(DataStorage storage);
	}

	public static interface DataUpdateErrorListener {
		public void onDataUpdateError(DataStorage storage, Exception e);
	}

	public static class ReplyStatus {
		boolean success;
		String errorMessage;
	}

	public void setAuthToken(String token) {
		mAuthToken = token;
	}

	public void unregisterDataUpdateListener(DataUpdateListener listener) {
		mDataUpdateListeners.remove(listener);
	}
}
