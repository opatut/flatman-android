package de.opatut.flatman.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.OverScroller;
import android.widget.ScrollView;

public class ListenableScrollView extends ScrollView {
	private OnScrollChangedListener m_onScrollChangedListener;

	public ListenableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListenableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListenableScrollView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (m_onScrollChangedListener != null) {
			m_onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (m_onScrollChangedListener != null) {
				if (m_onScrollChangedListener.onFinishedScrolling()) {
					return true;
				}
			}
		}
		return super.onTouchEvent(ev);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		m_onScrollChangedListener = listener;
	}

	public OverScroller getScroller() {
		try {
			Field f = ScrollView.class.getDeclaredField("mScroller");
			f.setAccessible(true);
			return (OverScroller) f.get(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public interface OnScrollChangedListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);

		boolean onFinishedScrolling();
	}
}
