package de.opatut.flatman;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.opatut.flatman.welcome.WelcomeWizardFragment;

public class ChooseLoginRegisterFragment extends WelcomeWizardFragment {
	private InteractionListener mListener;

	public ChooseLoginRegisterFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_choose_login_register, container, false);

		((Button) root.findViewById(R.id.choose_login_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mListener != null)
					mListener.onChooseLogin();
			}
		});

		((Button) root.findViewById(R.id.choose_register_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mListener != null)
					mListener.onChooseRegister();
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
		public void onChooseLogin();

		public void onChooseRegister();
	}

	@Override
	public String getTitle() {
		return getString(R.string.choose_login_register_title);
	}

}
