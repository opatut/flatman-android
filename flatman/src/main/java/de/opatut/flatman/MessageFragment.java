package de.opatut.flatman;

import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.DataStorage.DataUpdateListener;
import de.opatut.flatman.main.MainActivity;
import de.opatut.flatman.util.awesome.TextAwesome;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MessageFragment extends Fragment implements DataUpdateListener {
	public static final int TYPE_PROGRESS = 1;
	public static final int TYPE_ICON = 2;
	public static final int TYPE_AWESOME = 3;

	private static final String ARG_TYPE = "type";
	private static final String ARG_TITLE = "title";
	private static final String ARG_MESSAGE = "message";
	private static final String ARG_ICON = "icon";

	private int mType;
	private String mTitle;
	private String mMessage;
	private int mIcon;

	public static MessageFragment newInstance(int type, String title, String message, int icon) {
		MessageFragment fragment = new MessageFragment();
		Bundle args = new Bundle();
		args.putString(ARG_MESSAGE, message);
		args.putString(ARG_TITLE, title);
		args.putInt(ARG_TYPE, type);
		args.putInt(ARG_ICON, icon);
		fragment.setArguments(args);
		return fragment;
	}
	
	public MessageFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		DataStorage.getInstance().registerDataUpdateListener(this);
		super.onAttach(activity);
	}
	
	@Override
	public void onDestroy() {
		DataStorage.getInstance().unregisterDataUpdateListener(this);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mType = getArguments().getInt(ARG_TYPE);
			mMessage = getArguments().getString(ARG_MESSAGE);
			mTitle = getArguments().getString(ARG_TITLE);
			mIcon = getArguments().getInt(ARG_ICON);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_message, container, false);

		ProgressBar progress = (ProgressBar) root.findViewById(R.id.progress);
		ImageView icon = (ImageView) root.findViewById(R.id.icon);
		TextView message = (TextView) root.findViewById(R.id.message);
		TextView title = (TextView) root.findViewById(R.id.title);
		TextAwesome awesome = (TextAwesome) root.findViewById(R.id.awesome);
		;

		if (mType == TYPE_ICON) {
			icon.setVisibility(View.VISIBLE);
			icon.setImageResource(mIcon);
		} else if (mType == TYPE_AWESOME) {
			awesome.setVisibility(View.VISIBLE);
			awesome.setText(mIcon);
		} else if (mType == TYPE_PROGRESS) {
			progress.setVisibility(View.VISIBLE);
		}

		if (mMessage != null && !mMessage.equals("")) {
			message.setVisibility(View.VISIBLE);
			message.setText(mMessage);
		}

		if (mTitle != null && !mTitle.equals("")) {
			title.setVisibility(View.VISIBLE);
			title.setText(mTitle);
		}

		return root;
	}

	@Override
	public void onDataUpdated(DataStorage storage) {
		// message is useless now, go home
		if (isAdded() && getActivity() != null) {
			((MainActivity) getActivity()).switchTab(MainActivity.TAB_HOME);
		}
	}

}
