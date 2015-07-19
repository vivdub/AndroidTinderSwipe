package com.rk.swipeviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rk.TinderView;
import com.rk.TinderView.TinderListener;

/**
 * @author vivek
 */
//==========================================
public class SwipeActivity extends Activity implements TinderListener{
	
	private ViewGroup cont;
	private View refresh;
	private Object[][] data = {
			{R.drawable.f1},
			{R.drawable.f2},
			{R.drawable.f3},
			{R.drawable.f4},
			{R.drawable.f5},
			{R.drawable.f6},
			{R.drawable.f7},
			{R.drawable.f8},
			{R.drawable.f9}
	};
	private int cIndex = -1;
	private boolean lastLiked = true;
	
	//================================
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		setContentView(R.layout.dl);
		cont = (ViewGroup)findViewById(R.id.cont);
		refresh = findViewById(R.id.refresh);
		addLayout(1);
		addLayout(0);
		refresh.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if(cIndex>-1){
					cont.removeAllViews();
					addLayout(cIndex+1);
					addLayout(cIndex);					
					animateReverse(cIndex);
					cIndex--;
				}
			}
		});
	}
	
	//================================
	private void animateReverse(int index){
		final TinderView tv = (TinderView)cont.findViewById(index);
		tv.setVisibility(View.INVISIBLE);
		cont.postDelayed(new Runnable(){public void run(){
			tv.setVisibility(View.VISIBLE);
			if(lastLiked)
				tv.reverseSwipe(Gravity.RIGHT, Gravity.BOTTOM, 400);
			else
				tv.reverseSwipe(Gravity.LEFT, Gravity.BOTTOM, 400);
		}}, 300);		
	}
	
	//================================
	private void addLayout(int index){
		View v = LayoutInflater.from(this).inflate(R.layout.item_layout, null);
		((ImageView)v.findViewById(R.id.image)).setImageResource((int)data[index][0]);
		TinderView tv = (TinderView)v.findViewById(R.id.tinder);
		tv.setTag(index+"");
		tv.setId(index);
		tv.setTinderListener(this);	
		cont.addView(v);
	}

	//================================
	private void addNextView(int index){
		if(index<data.length-2){
			cont.removeAllViews();
			addLayout(index+2);
			addLayout(index+1);
		}
	}
	
	//================================
	@Override
	public void onLiked(View v) {
		lastLiked = true;
		cIndex = Integer.parseInt(v.getTag()+"");
		addNextView(cIndex);
	}

	//================================
	@Override
	public void onDisliked(View v) {
		lastLiked = false;
		cIndex = Integer.parseInt(v.getTag()+"");
		addNextView(cIndex);
	}
}
