package com.android.topbardemo.widget;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class MagcommTextView extends TextView {

    public MagcommTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MagcommTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MagcommTextView(Context context) {
        super(context);
        init();
    }

    private void init(){
        setSingleLine(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEllipsize(TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setHorizontallyScrolling(true);
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        if (focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused){
            super.onWindowFocusChanged(focused);
        }
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
