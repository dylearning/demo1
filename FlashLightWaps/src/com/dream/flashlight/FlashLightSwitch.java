package com.dream.flashlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class FlashLightSwitch extends View implements OnTouchListener {

	private Bitmap switch_on_Bkg, switch_off_Bkg, slip_Btn;
	private Rect on_Rect, off_Rect;

	private boolean isSlipping = false;

	private boolean isSwitchOn = true;

	private float previousy, currenty;

	private OnSwitchListener onSwitchListener;

	private boolean isSwitchListenerOn = false;

	public FlashLightSwitch(Context context) {
		super(context);
		init();
	}

	public FlashLightSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setOnTouchListener(this);
	}

	protected void setImageResource(int switchOnBkg, int switchOffBkg, int slipBtn) {
		switch_on_Bkg = BitmapFactory.decodeResource(getResources(), switchOnBkg);
		switch_off_Bkg = BitmapFactory.decodeResource(getResources(), switchOffBkg);
		slip_Btn = BitmapFactory.decodeResource(getResources(), slipBtn);

		off_Rect = new Rect(0, switch_off_Bkg.getHeight() - slip_Btn.getHeight(), slip_Btn.getWidth(), switch_off_Bkg.getHeight());

		on_Rect = new Rect(0, 0, slip_Btn.getWidth(), slip_Btn.getHeight());
	}

	protected void setSwitchState(boolean switchState) {
		isSwitchOn = switchState;
	}

	protected boolean getSwitchState() {
		return isSwitchOn;
	}

	protected void updateSwitchState(boolean switchState) {
		isSwitchOn = switchState;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Matrix matrix = new Matrix();
		Paint paint = new Paint();

		float top_SlipBtn;

		if (currenty < (switch_on_Bkg.getHeight() / 2)) {
			canvas.drawBitmap(switch_on_Bkg, matrix, paint);
		} else {
			canvas.drawBitmap(switch_off_Bkg, matrix, paint);
		}

		if (isSlipping) {
			if (currenty > switch_on_Bkg.getHeight()) {
				top_SlipBtn = switch_on_Bkg.getHeight() - slip_Btn.getHeight();
			} else {
				top_SlipBtn = currenty - slip_Btn.getHeight() / 2;
			}
		} else {
			if (isSwitchOn) {
				top_SlipBtn = on_Rect.top;
			} else {
				top_SlipBtn = off_Rect.top;
			}
		}

		if (top_SlipBtn < 0) {
			top_SlipBtn = 0;
		} else if (top_SlipBtn > switch_on_Bkg.getHeight() - slip_Btn.getHeight()) {
			top_SlipBtn = switch_on_Bkg.getHeight() - slip_Btn.getHeight();
		}

		canvas.drawBitmap(slip_Btn, 3, top_SlipBtn, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(switch_on_Bkg.getWidth(), switch_on_Bkg.getHeight());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			currenty = event.getY();
			break;

		case MotionEvent.ACTION_DOWN:
			if (event.getX() > switch_on_Bkg.getWidth() || event.getY() > switch_on_Bkg.getHeight()) {
				return false;
			}

			isSlipping = true;
			previousy = event.getY();
			currenty = previousy;
			break;

		case MotionEvent.ACTION_UP:
			isSlipping = false;
			boolean previousSwitchState = isSwitchOn;

			if (event.getY() >= (switch_on_Bkg.getHeight() / 2)) {
				isSwitchOn = false;
			} else {
				isSwitchOn = true;
			}

			if (isSwitchListenerOn && (previousSwitchState != isSwitchOn)) {
				onSwitchListener.onSwitched(isSwitchOn);
			}
			break;

		default:
			break;
		}

		invalidate();
		return true;
	}

	public void setOnSwitchListener(OnSwitchListener listener) {
		onSwitchListener = listener;
		isSwitchListenerOn = true;
	}

	public interface OnSwitchListener {
		abstract void onSwitched(boolean isSwitchOn);
	}
}
