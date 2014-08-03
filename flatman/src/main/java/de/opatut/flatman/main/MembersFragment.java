package de.opatut.flatman.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import de.opatut.flatman.MemberDetailsActivity;
import de.opatut.flatman.R;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.User;
import de.opatut.flatman.util.DownloadImageTask;
import de.opatut.flatman.util.SimpleListAdapter;
import de.opatut.flatman.util.awesome.TextAwesome;

public class MembersFragment extends Fragment implements DataStorage.DataUpdateListener {

	private View mRootView;
	private GridView mGridView;
	private SimpleListAdapter<User> mListAdapter;

	public MembersFragment() {
		DataStorage.getInstance().registerDataUpdateListener(this);

		User u1 = new User();
		u1.id = 1;
		u1.displayname = "Max Mustermann";
		DataStorage.getInstance().group.members.add(u1);

		User u2 = new User();
		u2.id = 2;
		u2.displayname = "Martina Musterfrau";
		DataStorage.getInstance().group.members.add(u2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mRootView = inflater.inflate(R.layout.fragment_members, container, false);
		mGridView = (GridView) mRootView.findViewById(R.id.grid_members);

		mListAdapter = new SimpleListAdapter<User>(getActivity(), R.layout.listitem_member, DataStorage.getInstance().group.members) {
			@Override
			public void fillRow(final User item, View view) {
				((TextView) view.findViewById(R.id.name)).setText(item.displayname);

				boolean home = (item.id == 1);

				((TextAwesome) view.findViewById(R.id.status_icon)).setText(home ? R.string.fa_home : R.string.fa_rocket);
				((TextView) view.findViewById(R.id.status_text)).setText(home ? R.string.status_home : R.string.status_away);
				
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent memberDetailsIntent = new Intent(getActivity(), MemberDetailsActivity.class);

						memberDetailsIntent.putExtra(MemberDetailsActivity.EXTRA_MEMBER_INDEX, DataStorage.getInstance().group.members.indexOf(item));
						startActivity(memberDetailsIntent);
					}
				});

				new DownloadImageTask((ImageView) view.findViewById(R.id.avatar)).execute(item.getAvatarUrl(512));
			}
		};
		mGridView.setAdapter(mListAdapter);

		return mRootView;
	}

	@Override
	public void onDataUpdated(DataStorage storage) {
		if (mListAdapter != null) {
			mListAdapter.setData(storage.group.members);
		}
	}
}
