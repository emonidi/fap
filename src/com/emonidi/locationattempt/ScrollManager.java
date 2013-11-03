package com.emonidi.locationattempt;

import java.util.ArrayList;

import com.emonidi.locationattempt.R.menu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollManager {
	WeatherInfo wi;
	Context context;
	private GridLayout parent; 
	public Activity activity;
	ArrayList<LinearLayout> layouts = new ArrayList<LinearLayout>();
	ArrayList<WeatherInfo> infos = new ArrayList<WeatherInfo>();
	int[] originalPositions;
	ImagePicker iPicker;
	private int lWidth;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	int currentDayLayout;
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ScrollManager(Activity _activity){
		iPicker = new ImagePicker();
		this.activity = _activity;
		layouts.add((LinearLayout) activity.findViewById(R.id.temperatureLayout));
		layouts.add((LinearLayout) activity.findViewById(R.id.forecastDay1));
		layouts.add((LinearLayout) activity.findViewById(R.id.forecastDay2));
		layouts.add((LinearLayout) activity.findViewById(R.id.forecastDay3));
		layouts.add((LinearLayout) activity.findViewById(R.id.forecastDay4));
		
		
		parent = (GridLayout) activity.findViewById(R.id.grid);
		
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		lWidth = metrics.widthPixels;
		for(int i = 0; i < layouts.size(); i++){
			LinearLayout layout = layouts.get(i);
			Log.v("WIDTH",String.valueOf(metrics.widthPixels));
			layout.setX(lWidth*i);
			layout.setBackgroundColor(i);
			TextView t = new TextView(layout.getContext());
			t.setText(String.valueOf(i));
				
			
		}
		
		currentDayLayout = -1;
		
		SimpleOnGestureListener weatherGestureListener  = new SimpleOnGestureListener(){
			 @Override
			    public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,float velocityY) 
			    {
			      	

				 	if(start.getRawX() < finish.getRawX()){
				 		moveWeather("right");
				 	}else{
				 		moveWeather("left");
				 	}
				 	return super.onFling(start, finish, velocityX, velocityY);
			    }
			 	
			 @Override
			 public boolean onDown(MotionEvent e) {
				 
			     return true;
			 }
			 
			

		};
		
		
		gestureDetector =  new GestureDetector(context,weatherGestureListener);
		
		View.OnTouchListener weatherTouchListener = new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		
				 if(gestureDetector.onTouchEvent(event))
					 return true;
				 else
					 return gestureDetector.onTouchEvent(event);
			}
			
		};
		
		parent.setOnTouchListener(weatherTouchListener);
		
	}
	


	public void deployWeather(WeatherInfo wi, int layoutIndex){
		LinearLayout layout = layouts.get(layoutIndex);
		TextView tempText = (TextView) layout.findViewWithTag("temperatureTextViewTag");
		tempText.setText(String.valueOf(wi.temperature_high));
		layout.setBackgroundColor(Color.parseColor(wi.color));
		ImageView icon = (ImageView) layout.findViewWithTag("bigIconTag");
		icon.setImageResource(iPicker.getBigImage(wi.icon));
		icon.invalidate();
	}
	
	public void addWeatherInfo(WeatherInfo wi){
		infos.add(wi);
		
	}
	



	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void focusLayout(int layoutIndex,boolean fromSwipe){
		int duration;
		duration = fromSwipe ? 300 : 500;
		
		for(int i = 0; i < layouts.size(); i++){
			
			LinearLayout layout = layouts.get(i);
			//layout.setX((lWidth*i)-(layoutIndex+1)*lWidth);
			ObjectAnimator anim = ObjectAnimator.ofFloat(layout,"x",layout.getX(),(lWidth*i)-(layoutIndex+1)*lWidth);
			anim.setDuration(duration);
			anim.start();
			currentDayLayout = layoutIndex;
		}
	}
	
	public void moveWeather(String direction){
		Log.v("CURRENT LAYOUT",String.valueOf(currentDayLayout));
		if(direction == "left"){
			if(currentDayLayout >= -1 && currentDayLayout <= 2){
				currentDayLayout++;
			}
		}else{
			if(currentDayLayout >= 0 && currentDayLayout <= 3){
				currentDayLayout--;
				Log.v("currentDayLayout",String.valueOf(currentDayLayout));
			}
		}
		focusLayout(currentDayLayout,true);
	}
	

}
