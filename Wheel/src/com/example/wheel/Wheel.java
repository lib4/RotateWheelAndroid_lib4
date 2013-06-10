package com.example.wheel;

/**
 * @author AnasAbubacker
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

public class Wheel extends View {

    private Bitmap indicatorBitmap;
    private AnimateDrawable mDrawable;
    Context context;
    Drawable movingDot;
    rotateAnimation an;
    int previousCalculatedAngle = 0;
    int previousRotatedAngle = 0;
    int NUMBEROFSECTOR = 8;
    int ANGLE_PER_SECTOR = 360 / NUMBEROFSECTOR;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    public Wheel(Context context) {
	super(context);
	this.context = context;
	initialize(context);

    }

    public void rotate(float startAngle, float endAngle, int duration) {
	an = new rotateAnimation(startAngle, endAngle, Animation.ABSOLUTE,
		indicatorBitmap.getWidth() / 2, Animation.ABSOLUTE,
		indicatorBitmap.getHeight() / 2);
	// an.setInterpolator(new AC());
	an.setDuration(duration);
	an.setRepeatCount(0);
	an.initialize(0, 0, indicatorBitmap.getWidth(),
		indicatorBitmap.getHeight());
	setFocusable(true);
	setFocusableInTouchMode(true);
	mDrawable = new AnimateDrawable(movingDot, an);
	an.startNow();

    }

    public Wheel(Context context, AttributeSet attrs) {
	super(context, attrs);
	this.context = context;
	initialize(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
	// Log.e("I m here", "I m here");
	mDrawable.draw(canvas);
	invalidate();
    }

    private void initialize(Context context) {
	indicatorBitmap = BitmapFactory.decodeResource(context.getResources(),
		R.drawable.wheel);
	setFocusable(true);
	setFocusableInTouchMode(true);
	gestureDetector = new GestureDetector(context,
		new CustomGestureDetector());
	gestureListener = new View.OnTouchListener() {
	    public boolean onTouch(View v, MotionEvent event) {

		return gestureDetector.onTouchEvent(event);
	    }
	};

	setOnTouchListener(gestureListener);

	this.movingDot = context.getResources().getDrawable(R.drawable.wheel);
	this.movingDot.setBounds(0, 0, indicatorBitmap.getWidth(),
		indicatorBitmap.getHeight());
	rotate(0, 0, 50);
	this.setBackgroundResource(R.drawable.ic_launcher);
    }




    private int calculateAngle(float GETX, float GETY) {

	double tx;
	double ty;
	int x = (int) GETX;
	int y = (int) GETY;
	tx = x - (indicatorBitmap.getWidth() / 2);
	ty = y - (indicatorBitmap.getHeight() / 2);
	double angleInDegrees = Math.atan2(ty, tx) * 180 / Math.PI;
	int ACTUAL_ANGLE = 270;
	if (angleInDegrees < 0 && angleInDegrees < -90) {// Need to add
							 // 270+angle degrees)
	    ACTUAL_ANGLE += (int) (180 + angleInDegrees);

	} else if (angleInDegrees < 0 && angleInDegrees > -90) {// Need to add
								// 90+angle
								// degrees)
	    ACTUAL_ANGLE = (int) (90 + angleInDegrees);

	} else if (angleInDegrees > 0)
	    ACTUAL_ANGLE = 90 + (int) angleInDegrees;

	return ACTUAL_ANGLE;

    }

 
    private static class rotateAnimation extends RotateAnimation {

	public rotateAnimation(float fromDegrees, float toDegrees,
		int pivotXType, float pivotXValue, int pivotYType,
		float pivotYValue) {
	    super(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType,
		    pivotYValue);
	    // TODO Auto-generated constructor stub
	}

	public rotateAnimation(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    // TODO Auto-generated constructor stub
	}

	@Override
	public void cancel() {
	    Log.e("Cancel ", "Cacel ");
	    super.cancel();
	}

    }

    /**
     * 
     * 
     * @author Anas Abubacker
     *         Right Left Gesture Detection
     * 
     */
    class CustomGestureDetector extends SimpleOnGestureListener {


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
		float velocityX, float velocityY) {
	    try {

		int angle = calculateAngle(e2.getX(), e2.getY());
		mDrawable.getAnimation().cancel();

		if (previousRotatedAngle == 0) {

		    rotate(0,
			    previousRotatedAngle
				    + Math.abs((previousCalculatedAngle - angle)),
			    500);
		} else {

		    rotate(previousRotatedAngle,
			    previousRotatedAngle
				    + Math.abs((previousCalculatedAngle - angle)),
			    500);
		}
		previousRotatedAngle = previousRotatedAngle
			+ Math.abs((previousCalculatedAngle - angle));
		previousCalculatedAngle = angle;

		Log.e("PRevi ", "" + angle);

	    } catch (Exception e) {
		// nothing
	    }
	    return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {

	  

	    int angle = calculateAngle(event.getX(), event.getY());
	    Log.e("ANGLE ", " ANGLE " + angle);
	    if (previousRotatedAngle == 0) {
		int proposedRotation = 360 - angle;
		proposedRotation = proposedRotation
			+ (ANGLE_PER_SECTOR - (proposedRotation % ANGLE_PER_SECTOR));
		Log.e("proposedRotation 111 ", "" + proposedRotation + "%% "
			+ proposedRotation);
		rotate(previousRotatedAngle, proposedRotation, 1000);
		previousRotatedAngle = proposedRotation;
	    } else {
		int proposedRotation = previousRotatedAngle + (360 - angle);
		Log.e("proposedRotation 1 ", "" + proposedRotation + "%% "
			+ proposedRotation % ANGLE_PER_SECTOR);
		proposedRotation = proposedRotation
			+ (ANGLE_PER_SECTOR - (proposedRotation % ANGLE_PER_SECTOR));
		Log.e("proposedRotation 2 ", "" + proposedRotation);
		rotate(previousRotatedAngle, proposedRotation, 1000);
		previousRotatedAngle = proposedRotation;
		Log.e("previousAngle", "" + previousRotatedAngle);
		if (previousRotatedAngle > 360) {
		    previousRotatedAngle = previousRotatedAngle - 360;
		}
	    }
	    
	    Log.e("Single Tap up", "Single Tap Up previousRotatedAngle "+previousRotatedAngle);

	    return true;
	}

	@Override
	public boolean onDown(MotionEvent event) {
	    Log.e("OnDown", "OnDown");

	    return true;
	}

    }

  

}
