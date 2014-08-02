package de.opatut.flatman.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StringListAdapter<T> extends BaseAdapter {
	private Context m_context;
	private int m_layoutResourceId;
	List<T> m_data;
	StringListItemConverter<T> m_converter;
	int[] m_ids;

	public StringListAdapter(Context context, int layoutResourceId, int[] ids, List<T> data, StringListItemConverter<T> converter) {
		super();
		m_context = context;
		m_layoutResourceId = layoutResourceId;
		m_ids = ids;
		m_data = data;
		m_converter = converter;
		converter.adapter = this;
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
			LayoutInflater inflater = ((Activity) m_context).getLayoutInflater();
			convertView = inflater.inflate(m_layoutResourceId, parent, false);
		}

		// object item based on the position
		T item = m_data.get(position);

		// get the TextView and then set the text (item name) and tag (item ID)
		// values
		for (int id : m_ids) {
			TextView textViewItem = (TextView) convertView.findViewById(id);
			textViewItem.setText(m_converter.getText(item, id));
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return m_data.size();
	}

	@Override
	public Object getItem(int position) {
		return m_data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return m_converter.getItemId(m_data.get(position));
	}
}
