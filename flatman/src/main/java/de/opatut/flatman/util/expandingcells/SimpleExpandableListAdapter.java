package de.opatut.flatman.util.expandingcells;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.opatut.flatman.R;

public abstract class SimpleExpandableListAdapter<T> extends BaseAdapter {

    private Context mContext;
    private int mLayoutResourceId;
    private int mExpandableElementResourceId;
    private List<ExpandableListItem> mData;
    private int mCollapsedHeightInPx;

    public SimpleExpandableListAdapter(Context context, int layoutResourceId, int expandableElementResourceId, int collapsedHeightInDp, List<T> data) {
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mExpandableElementResourceId = expandableElementResourceId;

        Resources r = mContext.getResources();
        mCollapsedHeightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, collapsedHeightInDp, r.getDisplayMetrics());

        setData(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext)
                    .getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }

        // object item based on the position
        ExpandableListItem item = (ExpandableListItem) getItem(position);

        fillRow((T) item.getData(), convertView);

        // prepare outer layout
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.item_linear_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, item.getCollapsedHeight());
        linearLayout.setLayoutParams(layoutParams);

        // prepare expandable list layout
        ExpandingLayout expandingLayout = (ExpandingLayout) convertView.findViewById(mExpandableElementResourceId);
        expandingLayout.setExpandedHeight(item.getExpandedHeight());
        expandingLayout.setSizeChangedListener(item);

        if ( !item.isExpanded() ) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setData(List<T> data) {
        List<ExpandableListItem> expandableListItems = new ArrayList<ExpandableListItem>();
        for (T item : data) {
            expandableListItems.add(new ExpandableListItem(mExpandableElementResourceId, mCollapsedHeightInPx, item));
        }

        mData = expandableListItems;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public abstract void fillRow(T item, View view);
}
