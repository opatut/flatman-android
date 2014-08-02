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
import android.widget.TextView;
import de.opatut.flatman.welcome.WelcomeWizardFragment;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.DataStorage.DataUpdateErrorListener;
import de.opatut.flatman.data.DataStorage.ReplyStatus;

public class CreateGroupFragment extends WelcomeWizardFragment {
	private InteractionListener mListener;

	public CreateGroupFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_create_group, container, false);
		
		final TextView groupTitleView = (TextView) root.findViewById(R.id.name);

		((Button) root.findViewById(R.id.button_create)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new CreateGroupTask().execute(groupTitleView.getText().toString());
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
		public void onGroupJoined();
	}

	@Override
	public String getTitle() {
		return getString(R.string.title_create);
	}
	
	@Override
	public int getBackPage() {
		return WelcomeWizardActivity.PAGE_CHOOSE_CREATE_JOIN;
	}


	public class CreateGroupTask extends AsyncTask<String, Void, Boolean> implements DataUpdateErrorListener {
		Exception mDataUpdateException;

		protected Boolean doInBackground(String... params) {
			Map<String, String> args = new HashMap<String, String>();
			args.put("name", params[0]);
			DataStorage.getInstance().load("/group/create", args, ReplyStatus.class, this);
			return (mDataUpdateException == null);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				mListener.onGroupJoined();
			} else {
				new AlertDialog.Builder(getActivity())
						.setTitle("Creating group failed")
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

}
