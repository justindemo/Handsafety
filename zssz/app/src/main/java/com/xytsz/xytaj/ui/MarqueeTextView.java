package com.xytsz.xytaj.ui;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by admin on 2017/8/9.
 *
 */
public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        super.setSingleLine(true);
        super.setFocusable(true);
        super.setFocusableInTouchMode(true);
        super.setEllipsize(TextUtils.TruncateAt.MARQUEE);

    }


    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(true, direction, previouslyFocusedRect);
    }
}
