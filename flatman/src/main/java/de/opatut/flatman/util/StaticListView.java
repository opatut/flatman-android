package de.opatut.flatman.util;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class StaticListView extends LinearLayout {

    private BaseAdapter mAdapter;

    public StaticListView(Context context) {
        super(context);
        init();
    }

    public StaticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StaticListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        transition.enableTransitionType(LayoutTransition.APPEARING);
        transition.enableTransitionType(LayoutTransition.CHANGING);
        transition.enableTransitionType(LayoutTransition.CHANGE_APPEARING);
        transition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        transition.enableTransitionType(LayoutTransition.DISAPPEARING);
        setLayoutTransition(transition);
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                update();
            }

            @Override
            public void onInvalidated() {
                update();
            }
        });
        update();
    }

    private void update() {
        removeAllViews();

        if(mAdapter == null) {
            System.out.println("No adapter");
            return;
        }

        for(int i = 0; i < mAdapter.getCount(); ++i) {
            View v = mAdapter.getView(i, null, this);
            v.setTag(mAdapter.getItemId(i));
            v.setClickable(true);
            addView(v);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
