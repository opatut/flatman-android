package de.opatut.flatman;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import de.opatut.flatman.welcome.WelcomeWizardFragment;
import de.opatut.flatman.data.AccessToken;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.DataStorage.DataUpdateErrorListener;
import de.opatut.flatman.data.User;
import de.opatut.flatman.data.exceptions.InvalidCredentialsException;

public class LoginFragment extends WelcomeWizardFragment {
	private InteractionListener mListener;

	private EditText mUsernameView;
	private EditText mPasswordView;
	private Button mLoginButton;

	public LoginFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_login, container, false);

		mLoginButton = (Button) root.findViewById(R.id.button_login);
		mUsernameView = (EditText) root.findViewById(R.id.username);
		mPasswordView = (EditText) root.findViewById(R.id.password);

		mLoginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new LoginTask().execute(mUsernameView.getText().toString(), mPasswordView.getText().toString());
			}
		});

		return root;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (InteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement InteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface InteractionListener {
		public void onLoginSuccessful(String mUsername, boolean hasGroup);
	}

	@Override
	public String getTitle() {
		return getString(R.string.title_login);
	}

	@Override
	public int getBackPage() {
		return WelcomeWizardActivity.PAGE_CHOOSE_LOGIN_REGISTER;
	}

	public class LoginTask extends AsyncTask<String, Void, Boolean> implements DataUpdateErrorListener {
		Exception mDataUpdateException;
		private String mUsername;
		private boolean mHasGroup;

		protected Boolean doInBackground(String... params) {
			mUsername = params[0];

			Map<String, String> args = new HashMap<String, String>();
			args.put("username", params[0]);
			args.put("password", params[1]);
			LoginResult result = DataStorage.getInstance().load("/login", args, LoginResult.class, this);

			if (result.success) {
				DataStorage.getInstance().setAuthToken(result.token.token);
			} else {
				return false;
			}
			mHasGroup = (result != null && result.user != null && result.user.group_id > 0);

			return (mDataUpdateException == null);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				mListener.onLoginSuccessful(mUsername, mHasGroup);
			} else if (mDataUpdateException instanceof InvalidCredentialsException) {
				mPasswordView.setError("Wrong password");
			} else {
				new AlertDialog.Builder(getActivity())
						.setTitle("Login failed")
						.setMessage(mDataUpdateException.getMessage())
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						})
						.setIcon(android.R.drawable.ic_dialog_alert)
						.show();
			}
			super.onPostExecute(result);
		}

		@Override
		public void onDataUpdateError(DataStorage storage, Exception e) {
			mDataUpdateException = e;
		}

	}

	public static class LoginResult {
		public boolean success;
		public AccessToken token;
		public User user;
	}
}
