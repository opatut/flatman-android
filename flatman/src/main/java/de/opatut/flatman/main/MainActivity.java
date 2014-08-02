package de.opatut.flatman.main;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.opatut.flatman.MessageFragment;
import de.opatut.flatman.R;
import de.opatut.flatman.SettingsActivity;
import de.opatut.flatman.WelcomeWizardActivity;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.exceptions.AuthorizationRequiredException;
import de.opatut.flatman.data.exceptions.NoGroupException;

public class MainActivity extends Activity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks, DataStorage.DataUpdateErrorListener {

	private NavigationDrawerFragment mNavigationDrawerFragment;

	private CharSequence mTitle;

	private List<Fragment> mMainFragments = new ArrayList<Fragment>();

	private MessageFragment mMessageFragment;

	public static final int TAB_HOME = 0;
	public static final int TAB_MEMBERS = 1;
	public static final int TAB_SHOPPING = 2;
	public static final int TAB_MONEY = 3;
	public static final int TAB_TASKS = 4;

	public MainActivity() {
		mMainFragments.add(new HomeFragment());
		mMainFragments.add(new MembersFragment());
		mMainFragments.add(new ShoppingFragment());
		mMainFragments.add(new MoneyFragment());
		mMainFragments.add(new TasksFragment());

		DataStorage.getInstance().registerDataUpdateErrorListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		DataStorage.getInstance().reload();
		showMessage(MessageFragment.TYPE_PROGRESS, "Loading", null, 0);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, mMainFragments.get(position)).commit();
		onSectionAttached(position);
	}

	public void switchTab(int position) {
		// TODO: this is a switch from within the app, push current screen to back button stack
		mNavigationDrawerFragment.selectItem(position);
	}

	public void showMessage(int type, String title, String message, int icon) {
		mMessageFragment = MessageFragment.newInstance(type, title, message, icon);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, mMessageFragment).commit();
	}

	public void onSectionAttached(int number) {
		mTitle = getString(new int[] {
				R.string.home,
				R.string.members,
				R.string.shopping,
				R.string.money,
				R.string.tasks
		}[number]);
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(getApplication(), SettingsActivity.class));
			return true;
		case R.id.action_reload:
			Toast.makeText(getApplicationContext(), "Refreshing.", Toast.LENGTH_SHORT).show();
			DataStorage.getInstance().reload();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDataUpdateError(DataStorage storage, Exception e) {
		if (e instanceof AuthorizationRequiredException) {
			Intent i = new Intent(getApplicationContext(), WelcomeWizardActivity.class);
			i.putExtra(WelcomeWizardActivity.ATTR_PAGE, WelcomeWizardActivity.PAGE_CHOOSE_LOGIN_REGISTER);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		} else if (e instanceof NoGroupException) {
			Intent i = new Intent(getApplicationContext(), WelcomeWizardActivity.class);
			i.putExtra(WelcomeWizardActivity.ATTR_PAGE, WelcomeWizardActivity.PAGE_CHOOSE_CREATE_JOIN);
			i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		} else {
			// Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			showMessage(MessageFragment.TYPE_AWESOME, "Network error", e.getMessage(), R.string.fa_exclamation_triangle);
		}
	}
}
