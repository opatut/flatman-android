package de.opatut.flatman.util.expandingcells;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleExpandableListAdapter<T> extends BaseAdapter {

    private Context mContext;
    private int mLayoutResourceId;
    private int mExpandableElementResourceId;
    private int mHeaderElementResourceId;
    private List<ExpandableListItem> mData;

    public SimpleExpandableListAdapter(Context context, int layoutResourceId, int expandableElementResourceId, int headerElementResourceId, List<T> data) {
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mExpandableElementResourceId = expandableElementResourceId;
        mHeaderElementResourceId = headerElementResourceId;

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
        final ExpandableListItem item = (ExpandableListItem) getItem(position);

        fillRow((T) item.getData(), convertView);

//        Resources r = mContext.getResources();
//        int collapsedHeightInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, convertView.findViewById(mHeaderElementResourceId).getHeight(), r.getDisplayMetrics());
//        item.setCollapsedHeight(collapsedHeightInPx);
//
//        // prepare outer layout
//        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.item_linear_layout);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, item.getCollapsedHeight());
//        linearLayout.setLayoutParams(layoutParams);

        // prepare expandable list layout
        ExpandingLayout expandingLayout = (ExpandingLayout) convertView.findViewById(mExpandableElementResourceId);
        expandingLayout.setExpandedHeight(item.getExpandedHeight());
        expandingLayout.setSizeChangedListener(item);

        if ( !item.isExpanded() ) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        final View innerConvertView = convertView;

        ViewTreeObserver viewTreeObserver = convertView.getViewTreeObserver();
        if ( viewTreeObserver.isAlive() ) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    innerConvertView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int collapsedHeightInPx = innerConvertView.findViewById(mHeaderElementResourceId).getHeight();
                    item.setCollapsedHeight(collapsedHeightInPx);

                    // prepare outer layout
                    LinearLayout linearLayout = (LinearLayout) innerConvertView.findViewById(mHeaderElementResourceId);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, item.getCollapsedHeight());
                    linearLayout.setLayoutParams(layoutParams);

                    innerConvertView.invalidate();
                }
            });
        }

        return convertView;
    }

    public void setData(List<T> data) {
        List<ExpandableListItem> expandableListItems = new ArrayList<ExpandableListItem>();
        for (T item : data) {
            expandableListItems.add(new ExpandableListItem(mExpandableElementResourceId, item));
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
