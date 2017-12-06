package com.android.demo.mainuidemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CareViewPager extends ViewPager {

	public boolean enabledSlide;

	public CareViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabledSlide = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabledSlide) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.enabledSlide) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabledSlide = enabled;
	}
}