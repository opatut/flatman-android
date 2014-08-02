package de.opatut.flatman;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.User;
import de.opatut.flatman.util.DownloadImageTask;

public class MemberDetailsActivity extends FragmentActivity {
	public static final String EXTRA_MEMBER_INDEX = "member_index";

	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_details);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Bind the title indicator to the adapter
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);

		// show current member
		int index = getIntent().getIntExtra(EXTRA_MEMBER_INDEX, 0);
		mViewPager.setCurrentItem(index, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.member_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return MemberDetailsFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return DataStorage.getInstance().group.members.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return DataStorage.getInstance().group.members.get(position).displayname;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class MemberDetailsFragment extends Fragment {
		private static final String ARG_MEMBER_INDEX = "member_index";

		public static MemberDetailsFragment newInstance(int memberIndex) {
			MemberDetailsFragment fragment = new MemberDetailsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_MEMBER_INDEX, memberIndex);
			fragment.setArguments(args);
			return fragment;
		}

		public MemberDetailsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			User member = DataStorage.getInstance().group.members.get(getArguments().getInt(ARG_MEMBER_INDEX));
			View root = inflater.inflate(R.layout.fragment_member_details, container, false);

			TextView displayname = (TextView) root.findViewById(R.id.displayname);
			displayname.setText(member.displayname);

			ImageView avatar = (ImageView) root.findViewById(R.id.avatar);
			new DownloadImageTask(avatar).execute(member.getAvatarUrl(512));

			return root;
		}
	}

}
