package de.opatut.flatman.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SimpleListAdapter<T> extends BaseAdapter {
	private Context m_context;
	private int m_layoutResourceId;
	private int m_dropdownLayoutResourceId;
	List<T> m_data;
	int[] m_ids;
	boolean m_nullItem;

	public SimpleListAdapter(Context context, int layoutResourceId, List<T> data) {
		super();
		m_context = context;
		m_layoutResourceId = layoutResourceId;
		m_data = data;
		m_nullItem = false;
	}

	public void setNullItem(boolean nullItem) {
		m_nullItem = nullItem;
	}

	public List<T> getData() {
		return m_data;
	}

	public void setData(List<T> data) {
		m_data = data;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) m_context)
					.getLayoutInflater();
			convertView = inflater.inflate(m_layoutResourceId, parent, false);
		}

		// object item based on the position
		T item = (T) getItem(position);

		fillRow(item, convertView);

		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// inflate the layout
			LayoutInflater inflater = ((Activity) m_context)
					.getLayoutInflater();
			convertView = inflater.inflate(m_dropdownLayoutResourceId, parent,
					false);
		}

		// object item based on the position
		T item = (T) getItem(position);

		fillRow(item, convertView);

		return convertView;
	}

	public abstract void fillRow(T item, View view);

	public void setDropdownLayoutResource(int resid) {
		m_dropdownLayoutResourceId = resid;
	}

	@Override
	public int getCount() {
		return m_data.size() + (m_nullItem ? 1 : 0);
	}

	@Override
	public Object getItem(int position) {
		if (m_nullItem) {
			return position == 0 ? null : m_data.get(position - 1);
		} else {
			return m_data.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}