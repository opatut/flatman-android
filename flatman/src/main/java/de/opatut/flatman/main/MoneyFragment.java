package de.opatut.flatman.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import de.opatut.flatman.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class MoneyFragment extends Fragment {

	public MoneyFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.money, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_money, container, false);
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
