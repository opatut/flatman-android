package de.opatut.flatman.main;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import de.opatut.flatman.R;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.Task;
import de.opatut.flatman.util.awesome.TextAwesome;

public class HomeFragment extends Fragment implements OnMenuItemClickListener, DataStorage.DataUpdateListener {
	ViewGroup mHomeCards;
	LayoutInflater mInflater;

	TextView mTitle;

	public HomeFragment() {
		DataStorage.getInstance().registerDataUpdateListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View root = inflater.inflate(R.layout.fragment_home, container, false);

		// Group title
		mTitle = (TextView) root.findViewById(R.id.title);

		// Image popup
		View image = root.findViewById(R.id.image);

		final PopupMenu popupMenu = new PopupMenu(getActivity(), image);
		popupMenu.inflate(R.menu.group_photo);
		popupMenu.setOnMenuItemClickListener(this);

		image.setClickable(true);
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupMenu.show();
			}
		});

		// Setup home cards
		mHomeCards = (ViewGroup) root.findViewById(R.id.home_cards);

		mInflater = inflater;

		createHomeList();

		return root;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if (item.getItemId() == R.id.action_show_in_gallery) {
			Toast.makeText(getActivity(), "Show in gallery", Toast.LENGTH_SHORT).show();
		} else if (item.getItemId() == R.id.action_take_new_photo) {
			Toast.makeText(getActivity(), "Take new photo", Toast.LENGTH_SHORT).show();
		} else if (item.getItemId() == R.id.action_remove_group_photo) {
			Toast.makeText(getActivity(), "Remove photo", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	void createHomeList() {
		if (mHomeCards == null)
			return;

		mHomeCards.removeAllViews();
		createTaskOverview();
	}

	void createErrorMessage(String msg) {

	}

	void createTaskOverview() {
		List<Task> tasks = DataStorage.getInstance().group.tasks;

		View view = mInflater.inflate(R.layout.fragment_home_simple, mHomeCards);
		TextAwesome icon = (TextAwesome) view.findViewById(R.id.icon);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView message = (TextView) view.findViewById(R.id.message);

		if (tasks.size() == 0) {
			icon.setText(R.string.fa_check);
			title.setText(R.string.tasks_done);
			message.setText(R.string.tasks_done_message);
		} else {
			icon.setText(R.string.fa_tasks);
			title.setText(getString(R.string.tasks_pending, tasks.size()));
			message.setText(R.string.tasks_click_here);

		}

		view.setClickable(true);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).switchTab(MainActivity.TAB_TASKS);
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		createHomeList();
	}

	@Override
	public void onDataUpdated(DataStorage storage) {
		if (isAdded()) {
			createHomeList();
			mTitle.setText(storage.group.name);
		}
	}
}
