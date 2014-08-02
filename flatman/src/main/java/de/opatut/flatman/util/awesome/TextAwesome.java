package de.opatut.flatman.util.awesome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.TextView;

public class TextAwesome extends TextView {

	private final static String NAME = "FONTAWESOME";
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

	public TextAwesome(Context context) {
		super(context);
		init();

	}

	public TextAwesome(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		if (!isInEditMode()) {
			Typeface typeface = sTypefaceCache.get(NAME);
			if (typeface == null) {
				typeface = Typeface.createFromAsset(getContext().getAssets(), "fontawesome-webfont.ttf");
				sTypefaceCache.put(NAME, typeface);
			}
			setTypeface(typeface);
		}

		if (isInEditMode()) {
			setText("X");
		}
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		if (isInEditMode())
			text = "X";
		super.setText(text, type);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
