package de.opatut.flatman;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.welcome.WelcomeWizardFragment;

public class WelcomeWizardActivity extends FragmentActivity implements
		LoginFragment.InteractionListener,
		ChooseLoginRegisterFragment.InteractionListener,
		RegisterFragment.InteractionListener,
		ChooseCreateJoinGroupFragment.InteractionListener,
		CreateGroupFragment.InteractionListener {

	public static final String ATTR_PAGE = "page_number";
	public static final int PAGE_IGNORE = -2;
	public static final int PAGE_CANCEL = -1;
	public static final int PAGE_CHOOSE_LOGIN_REGISTER = 0;
	public static final int PAGE_LOGIN = 1;
	public static final int PAGE_REGISTER = 2;
	public static final int PAGE_CHOOSE_CREATE_JOIN = 3;
	public static final int PAGE_CREATE = 4;
	public static final int PAGE_JOIN = 5;

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	public List<WelcomeWizardFragment> mFragments = new ArrayList<WelcomeWizardFragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_wizard);

		// create fragments
		mFragments.add(new ChooseLoginRegisterFragment());
		mFragments.add(new LoginFragment());
		mFragments.add(new RegisterFragment());
		mFragments.add(new ChooseCreateJoinGroupFragment());
		mFragments.add(new CreateGroupFragment());

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		int page = getIntent().getIntExtra(ATTR_PAGE, 0);
		mViewPager.setCurrentItem(page, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.welcome_wizard, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		int back_page = mFragments.get(mViewPager.getCurrentItem()).getBackPage();
		if (back_page == PAGE_CANCEL) {
			super.onBackPressed();
		} else if (back_page == PAGE_IGNORE) {
		} else {
			showPage(back_page);
		}
	}

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragments.get(position).getTitle();
		}
	}

	@Override
	public void onLoginSuccessful(String username, boolean hasGroup) {
		Toast.makeText(getApplicationContext(), "Welcome back, " + username, Toast.LENGTH_SHORT).show();
		DataStorage.getInstance().reload();
		if (hasGroup) {
			NavUtils.navigateUpFromSameTask(this);
		} else {
			showPage(PAGE_CHOOSE_CREATE_JOIN);
		}
	}

	public void showPage(int page) {
		mViewPager.setCurrentItem(page);
		getActionBar().setTitle(mFragments.get(page).getTitle());
	}

	@Override
	public void onChooseLogin() {
		showPage(PAGE_LOGIN);
	}

	@Override
	public void onChooseRegister() {
		showPage(PAGE_REGISTER);
	}

	@Override
	public void onRegistrationComplete(String username) {
		showPage(PAGE_CHOOSE_CREATE_JOIN);
	}

	@Override
	public void onChooseCreateGroup() {
		showPage(PAGE_CREATE);
	}

	@Override
	public void onChooseJoinGroup() {
		showPage(PAGE_JOIN);
	}

	@Override
	public void onGroupJoined() {
		DataStorage.getInstance().reload();
		Toast.makeText(getApplicationContext(), "You joined the group", Toast.LENGTH_SHORT).show();
		finish();
	}
}
