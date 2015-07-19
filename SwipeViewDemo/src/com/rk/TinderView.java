package com.rk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.rk.swipeviewdemo.R;

/**
 * @author vivek
 */
//==========================================
public class TinderView extends SwipableView{

	private Bitmap like, dislike;
	private Paint bpl = new Paint();
	private Paint bpdl = new Paint();
	private TinderListener list;
	private boolean liked = false;
	
	//==================================
	public TinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		like = BitmapFactory.decodeResource(getResources(), R.drawable.img_swipe_like);
		dislike = BitmapFactory.decodeResource(getResources(), R.drawable.img_swipe_nope);
		bpl.setAlpha(0);
		bpdl.setAlpha(0);
	}

	//==================================
	public TinderView(Context ctx){
		super(ctx);
		bpl.setAlpha(0);
		bpdl.setAlpha(0);
	}
	
	//==================================
	public void setTinderListener(TinderListener list){this.list=list;}
	
	//==================================
	@Override
	protected void dispatchDraw(Canvas c) {
		super.dispatchDraw(c);
		c.drawBitmap(like, getRight()-getLeft()-like.getWidth()-50, (getBottom()-getTop()-like.getHeight())/2, bpl);
		c.drawBitmap(dislike, getLeft()+50, (getBottom()-getTop()-dislike.getHeight())/2, bpdl);
	}
	
	//==================================
	@Override
	public boolean onTouchEvent(MotionEvent me){		
		switch(me.getAction()){
			case MotionEvent.ACTION_MOVE:				
				if(getX()>getLeft())
					bpl.setAlpha((int) (((getX()-getLeft())/(float)(getRight()-getLeft()))*1000) );
				else
					bpdl.setAlpha((int) (((getLeft()-getX())/(float)(getRight()-getLeft()))*1000) );				
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				if(bpl.getAlpha()>60){
					bpl.setAlpha(0);
					liked=true;
					swipe(Gravity.RIGHT, Gravity.BOTTOM, 600);
					return true;
				}else{
					if(bpdl.getAlpha()>60){
						bpdl.setAlpha(0);
						liked=false;
						swipe(Gravity.LEFT, Gravity.BOTTOM, 600);
						return true;
					}
				}
				super.onTouchEvent(me);
				bpl.setAlpha(0);
				bpdl.setAlpha(0);
				invalidate();
				break;
		}
		super.onTouchEvent(me);
		return true;
	}
	
	//==================================
	@Override
	public void onSwipeAnimationEnd(){
		if(list!=null){
			if(liked) list.onLiked(this); else list.onDisliked(this);
			return;
		}
		setX(getLeft());
		setY(getTop());
		setRotation(0);
	}
	
	//==========================================
	public interface TinderListener{
		public void onLiked(View v);
		public void onDisliked(View v);
	}
}