package de.opatut.flatman;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import de.opatut.flatman.welcome.WelcomeWizardFragment;

public class ChooseCreateJoinGroupFragment extends WelcomeWizardFragment {
	private InteractionListener mListener;

	public ChooseCreateJoinGroupFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_choose_create_join_group, container, false);

		((Button) root.findViewById(R.id.choose_join_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.onChooseJoinGroup();
			}
		});

		((Button) root.findViewById(R.id.choose_create_button)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mListener.onChooseCreateGroup();
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
		public void onChooseCreateGroup();
		public void onChooseJoinGroup();
	}

	@Override
	public String getTitle() {
		return getString(R.string.title_join);
	}
	
	@Override
	public int getBackPage() {
		return WelcomeWizardActivity.PAGE_IGNORE;
	}
}
