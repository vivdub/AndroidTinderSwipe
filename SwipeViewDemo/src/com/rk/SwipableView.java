package com.rk;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * @author vivek
 */
//==========================================
public class SwipableView extends RelativeLayout{

	protected float dox = 0, doy = 0, ix = 0, tx = 0; //== down at
	protected float lx = 0;
	private float deg = 0;

	//==================================
	public SwipableView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//==================================
	public SwipableView(Context ctx){
		super(ctx);
	}

	//==================================
	@Override
	public boolean onTouchEvent(MotionEvent me){
		switch(me.getAction()){
		case MotionEvent.ACTION_DOWN:
			dox = me.getX();
			doy = me.getY();
			if(ix==0) ix = dox;
			break;
		case MotionEvent.ACTION_MOVE:			
			tx = -(dox-me.getRawX());
			setX(tx);
			setY(-(doy-me.getRawY()));	
			//== if moving in right direction & not reversing back 
			if(getX()>getLeft() && me.getRawX()>lx)
				deg = deg+0.2f;
			else{
				//== if going left
				if(me.getRawX()<lx)
					deg = deg-0.2f;
				else //== coming from left
					deg = deg+0.2f;
			}
			setRotation(deg);
			lx = me.getRawX();
			break;
		case MotionEvent.ACTION_UP:
			deg = 0;
			bringBack();
			break;
		}
		return true;
	}

	//==================================
	private void bringBack(){
		ValueAnimator vax = ValueAnimator.ofFloat(getX(), getLeft());
		vax.addUpdateListener(new AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator va) {
				setX(Float.parseFloat(va.getAnimatedValue()+""));
			}
		});
		vax.start();
		ValueAnimator vay = ValueAnimator.ofFloat(getY(), getTop());
		vay.addUpdateListener(new AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator va) {
				setY(Float.parseFloat(va.getAnimatedValue()+""));
			}
		});
		vay.start();
		ValueAnimator ra = ValueAnimator.ofFloat(deg,0);
		ra.addUpdateListener(new AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator va) {
				setRotation(Float.parseFloat(va.getAnimatedValue()+""));
			}
		});
		ra.start();
	}

	//==================================
	public void swipe(int xg, int yg, int dur){

		int xf = 0, xt = 0, yf = 0, yt = 0, deg = 20;
		switch(xg){
		case Gravity.LEFT:
		case Gravity.START:
			xt = -(getLeft()+getWidth()*2);
			break;	
		case Gravity.RIGHT:
		case Gravity.END:
			xt = (getLeft()+getWidth()*2);
			break;
		}
		switch(yg){
		case Gravity.TOP:
			yt = -getTop();
			if(xg==Gravity.RIGHT || xg==Gravity.END)
				deg = -deg;
			break;	
		case Gravity.BOTTOM:
			yt = getTop();
			if(xg==Gravity.LEFT || xg==Gravity.START)
				deg = -deg;
			break;
		}

		TranslateAnimation ta = new TranslateAnimation(xf,xt,yf,yt);
		RotateAnimation ra = new RotateAnimation(0, deg);
		ra.setInterpolator(new DecelerateInterpolator(0.5f));
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ta);
		as.addAnimation(ra);
		as.setDuration(dur);
		as.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {}
			public void onAnimationRepeat(Animation arg0) {}
			public void onAnimationEnd(Animation arg0) { 
				SwipableView.this.deg=0;
				onSwipeAnimationEnd(); 
			}
		});
		startAnimation(as);
	}

	//==================================
	public void reverseSwipe(int xg, int yg, int dur){

		int xf = 0, xt = 0, yf = 0, yt = 0, deg = 20;
		switch(xg){
			case Gravity.LEFT:
			case Gravity.START:
				xt = -(getLeft()+getWidth()*2);
				break;	
			case Gravity.RIGHT:
			case Gravity.END:
				xt = (getLeft()+getWidth()*2);
				break;
		}
		switch(yg){
		case Gravity.TOP:
			yt = -getTop();
			if(xg==Gravity.RIGHT || xg==Gravity.END)
				deg = -deg;
			break;	
		case Gravity.BOTTOM:
			yt = getTop();
			if(xg==Gravity.LEFT || xg==Gravity.START)
				deg = -deg;
			break;
		}

		TranslateAnimation ta = new TranslateAnimation(xt,xf,yt,yf);
		RotateAnimation ra = new RotateAnimation(deg, 0);
		ra.setInterpolator(new AccelerateInterpolator(0.5f));
		AnimationSet as = new AnimationSet(false);
		as.addAnimation(ta);
		as.addAnimation(ra);
		as.setDuration(dur);

		startAnimation(as);
	}
	
	//==================================
	/** Invoked when swipe animation is complete */
	public void onSwipeAnimationEnd(){}
}
