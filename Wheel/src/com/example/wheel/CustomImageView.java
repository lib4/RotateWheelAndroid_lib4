package com.example.wheel;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public  class CustomImageView extends ImageView{

	public CustomImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    // TODO Auto-generated constructor stub
	}
	
	public CustomImageView(Context context) {
	    super(context);
	    // TODO Auto-generated constructor stub
	}
	
	
	 @Override
	    protected boolean setFrame(int l, int t, int r, int b)
	    {
	        Matrix matrix = getImageMatrix(); 
	        float scaleFactor = getWidth()/(float)getDrawable().getIntrinsicWidth();
	        //scaleFactor=scaleFactor*2;
	       // Log.e("Scale Factor","scale Factor"+scaleFactor);
	        matrix.setScale(scaleFactor, scaleFactor, 0, 0);
	        setImageMatrix(matrix);
	     
	     
	        return super.setFrame(l, t, r, b);
	    }
	
}
