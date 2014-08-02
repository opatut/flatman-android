package de.opatut.flatman.main;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.OverScroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.opatut.flatman.R;
import de.opatut.flatman.ShoppingItemActivity;
import de.opatut.flatman.data.DataStorage;
import de.opatut.flatman.data.DataStorage.DataUpdateErrorListener;
import de.opatut.flatman.data.DataStorage.DataUpdateListener;
import de.opatut.flatman.data.DataStorage.ReplyStatus;
import de.opatut.flatman.data.ShoppingCategory;
import de.opatut.flatman.data.ShoppingItem;
import de.opatut.flatman.util.ListenableScrollView;
import de.opatut.flatman.util.ListenableScrollView.OnScrollChangedListener;
import de.opatut.flatman.util.SimpleListAdapter;
import de.opatut.flatman.util.StaticListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class ShoppingFragment extends Fragment implements DataUpdateListener {

	private View mRootView;
	private StaticListView mListView;
	private SimpleListAdapter<ShoppingItem> mListAdapter;
	private Spinner mCategoryFilter;
	private ListenableScrollView mScrollView;
	private View mFilter;

	public ShoppingFragment() {
		DataStorage.getInstance().registerDataUpdateListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mRootView = inflater.inflate(R.layout.fragment_shopping, container,
				false);
		mListView = (StaticListView) mRootView
				.findViewById(R.id.list_shopping_items);
		mFilter = (View) mRootView.findViewById(R.id.filter);
		mScrollView = (ListenableScrollView) mRootView
				.findViewById(R.id.scroll);
		mCategoryFilter = (Spinner) mRootView
				.findViewById(R.id.filter_category);

		createAdapter();
		setHasOptionsMenu(true);

		final SimpleListAdapter<ShoppingCategory> simple = new SimpleListAdapter<ShoppingCategory>(
				getActivity(), android.R.layout.simple_spinner_item,
				DataStorage.getInstance().group.shopping_categories) {
			@Override
			public void fillRow(ShoppingCategory item, View view) {
				((TextView) view.findViewById(android.R.id.text1))
						.setText(item != null ? item.title : "None");
			}
		};
		simple.setDropdownLayoutResource(android.R.layout.simple_spinner_dropdown_item);
		simple.setNullItem(true);
		mCategoryFilter.setAdapter(simple);
		mCategoryFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View item,
					int pos, long id) {
				setFilterCategory(pos == 0 ? null : (ShoppingCategory) simple
						.getItem(pos));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}

		});

		mScrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				if (t < mFilter.getHeight()) {
					mFilter.setY(t);
					mScrollView.offsetTopAndBottom(0);
				}
			}

			@Override
			public boolean onFinishedScrolling() {
				OverScroller s = mScrollView.getScroller();
				int y = mScrollView.getScrollY(), h = mFilter.getHeight();

				if (y > h) {
					return false;
				}

				if (s.isFinished()) {
					if (y < h / 2) {
						mScrollView.smoothScrollTo(0, 0);
					} else if (y < h) {
						mScrollView.smoothScrollTo(0, h);
					}
				} else {
					mScrollView.smoothScrollTo(0, s.getCurrVelocity() < 0 ? 0 : h);
				}
				return true;
			}
		});

		mScrollView.post(new Runnable() {
			@Override
			public void run() {
				mScrollView.scrollTo(0, mFilter.getHeight());
			}
		});

		return mRootView;
	}

	void setFilterCategory(ShoppingCategory category) {
		// TODO
		System.out.println("Filtering category for: "
				+ (category != null ? category.title : "NONE"));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.shopping, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	public void showAddDialog() {
		startActivity(new Intent(getActivity(), ShoppingItemActivity.class));

		// final Dialog d = new Dialog(getActivity(),
		// android.R.style.Theme_Holo_Dialog);
		// d.setContentView(R.layout.dialog_shoppingitem_add);
		// d.setTitle(R.string.title_add_item);
		//
		// final TextView titleView = (TextView) d.findViewById(R.id.title);
		// final TextView amountView = (TextView) d.findViewById(R.id.amount);
		// final TextView descriptionView = (TextView)
		// d.findViewById(R.id.description);
		//
		// ((Button) d.findViewById(R.id.cancel)).setOnClickListener(new
		// OnClickListener() {
		// public void onClick(View v) {
		// d.dismiss();
		// }
		// });
		// ((Button) d.findViewById(R.id.add_more)).setOnClickListener(new
		// OnClickListener() {
		// public void onClick(View v) {
		// if (addShoppingItem(titleView.getText().toString(),
		// amountView.getText().toString(),
		// descriptionView.getText().toString())) {
		// titleView.setText("");
		// amountView.setText("");
		// descriptionView.setText("");
		// } else {
		// titleView.setError(getString(R.string.error_required));
		// }
		// titleView.requestFocus();
		// }
		// });
		// ((Button) d.findViewById(R.id.add_done)).setOnClickListener(new
		// OnClickListener() {
		// public void onClick(View v) {
		// if (addShoppingItem(titleView.getText().toString(),
		// amountView.getText().toString(),
		// descriptionView.getText().toString())) {
		// d.dismiss();
		// } else {
		// titleView.setError(getString(R.string.error_required));
		// }
		// }
		// });
		// d.show();
	}

	public void showCleanUpDialog() {
		new AlertDialog.Builder(getActivity(),
				android.R.style.Theme_Holo_Dialog)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Remove purchased items?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(getActivity(), "Clean",
										Toast.LENGTH_SHORT).show();
								cleanupList();
								dialog.dismiss();
							}
						})
				.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).show();
	}

	protected void cleanupList() {
		new Thread(new Runnable() {
			public void run() {
				DataStorage.getInstance().load("/shopping/cleanup", null,
						ReplyStatus.class, null);
				DataStorage.getInstance().reload();
			}
		}).start();
	}

	private boolean addShoppingItem(String title, String amount,
			String description) {
		if (title.length() == 0) {
			return false;
		}

		final Map<String, String> data = new HashMap<String, String>();
		data.put("title", title);
		data.put("amount", amount);
		data.put("description", description);

		new Thread(new Runnable() {
			public void run() {
				DataStorage.getInstance().load("/shopping/item/new", data,
						ReplyStatus.class, null);
				DataStorage.getInstance().reload();
			}
		}).start();

		// TODO
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			showAddDialog();
			return true;
		case R.id.action_clear_list:
			showCleanUpDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean createAdapter() {
		if (mListAdapter != null)
			return true;

		if (DataStorage.getInstance().group == null || mListView == null)
			return false;

		mListAdapter = new SimpleListAdapter<ShoppingItem>(getActivity(),
				R.layout.listitem_shopping_item,
				DataStorage.getInstance().group.shopping_items) {
			@Override
			public void fillRow(final ShoppingItem item, View view) {
				final TextView title = (TextView) view.findViewById(R.id.title);
				final TextView amount = (TextView) view
						.findViewById(R.id.amount);
				final TextView category = (TextView) view
						.findViewById(R.id.category);
				// final TextView description = (TextView)
				// view.findViewById(R.id.description);
				final CheckBox checkbox = (CheckBox) view
						.findViewById(R.id.checkbox);

				final int defaultPaintFlags = title.getPaintFlags()
						& ~Paint.STRIKE_THRU_TEXT_FLAG;

				title.setText(item.title);
				amount.setText(item.amount);
				amount.setVisibility(item.amount.equals("") ? View.GONE
						: View.VISIBLE);

				// strikethrough
				title.setPaintFlags(defaultPaintFlags
						| Paint.STRIKE_THRU_TEXT_FLAG
						* (item.purchased ? 1 : 0));
				amount.setPaintFlags(title.getPaintFlags());
				category.setPaintFlags(title.getPaintFlags());
				// description.setPaintFlags(title.getPaintFlags());

				// grey
				title.setEnabled(!item.purchased);
				amount.setEnabled(!item.purchased);
				category.setEnabled(!item.purchased);
				// description.setEnabled(!item.purchased);

				ShoppingCategory cat = DataStorage.getInstance().group
						.getShoppingCategory(item.category_id);
				if (cat != null) {
					category.setText(cat.title);
				}

				// if (item.description != null && item.description.length() >
				// 0) {
				// description.setVisibility(View.VISIBLE);
				// description.setText(item.description);
				// } else {
				// description.setVisibility(View.GONE);
				// }

				checkbox.setChecked(item.purchased);

				class CheckboxUpdater implements OnCheckedChangeListener,
						DataUpdateErrorListener {
					public void onDataUpdateError(DataStorage storage,
							Exception e) {
						Toast.makeText(getActivity(),
								"Failed to set status: " + e.getMessage(),
								Toast.LENGTH_SHORT).show();
						DataStorage.getInstance().reload();
					}

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						item.purchased = isChecked;
						item.sendStatus(this);
						notifyDataSetChanged();
					}
				}
				checkbox.setOnCheckedChangeListener(new CheckboxUpdater());
			}
		};
		mListView.setAdapter(mListAdapter);
		return true;
	}

	@Override
	public void onDataUpdated(DataStorage storage) {
		if (createAdapter()) {
			mListAdapter.setData(storage.group.shopping_items);
		}
	}

}
