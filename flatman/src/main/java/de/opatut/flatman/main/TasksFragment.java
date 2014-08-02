package de.opatut.flatman.main;

import info.evelio.drawable.RoundedAvatarDrawable;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.opatut.flatman.R;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.Task;
import de.opatut.flatman.data.User;
import de.opatut.flatman.util.DownloadImageTask;
import de.opatut.flatman.util.SimpleListAdapter;
import de.opatut.flatman.util.StaticListView;
import de.opatut.flatman.util.awesome.TextAwesome;

public class TasksFragment extends Fragment implements DataStorage.DataUpdateListener {
	View mRootView;
	StaticListView mListView;
	private SimpleListAdapter<Task> mListAdapter;

	public TasksFragment() {
		DataStorage.getInstance().registerDataUpdateListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.tasks, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mRootView = inflater.inflate(R.layout.fragment_tasks, container, false);
		mListView = (StaticListView) mRootView.findViewById(R.id.list_tasks_own);

		mListAdapter = new SimpleListAdapter<Task>(getActivity(), R.layout.listitem_task, DataStorage.getInstance().group.tasks) {
			@Override
			public void fillRow(Task item, View view) {
				((TextView) view.findViewById(R.id.title)).setText(item.title);
				((TextAwesome) view.findViewById(R.id.repeating)).setText(getString(item.getRepeatingIcon()));

				 new DownloadImageTask((ImageView) view.findViewById(R.id.avatar)).execute(User.getAvatarUrl(item.assignee_id, 64));
				((TextView) view.findViewById(R.id.title)).setText(item.title);
			}
		};
		mListView.setAdapter(mListAdapter);

		setHasOptionsMenu(true);

		return mRootView;
	}

	@Override
	public void onDataUpdated(DataStorage storage) {
		if (mListAdapter != null && isAdded()) {
			mListAdapter.setData(storage.group.tasks);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Toast.makeText(getActivity(), "Add dialog...", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_filter:
			Toast.makeText(getActivity(), "Filter dialog...", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
