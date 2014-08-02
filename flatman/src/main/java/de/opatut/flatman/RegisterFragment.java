package de.opatut.flatman;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import de.opatut.flatman.welcome.WelcomeWizardFragment;

public class RegisterFragment extends WelcomeWizardFragment {
	private InteractionListener mListener;
	
	private EditText mUsernameView;
	private EditText mDisplaynameView;
	private EditText mEmailView;
	private EditText mPasswordView;

	public RegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_register, container, false);
		
		mUsernameView = (EditText) root.findViewById(R.id.username);
		mDisplaynameView = (EditText) root.findViewById(R.id.displayname);
		mEmailView = (EditText) root.findViewById(R.id.email);
		mPasswordView = (EditText) root.findViewById(R.id.password);
		
		mDisplaynameView.setError("This is how you appear to others", getResources().getDrawable(android.R.drawable.ic_input_get));
		
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
		public void onRegistrationComplete(String username);
	}

	@Override
	public String getTitle() {
		return getString(R.string.title_register);
	}
}
