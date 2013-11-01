package com.emonidi.locationattempt;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
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
	private int lWidth;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public ScrollManager(Activity _activity){
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
		
		
	}
	
	public void addWeatherInfo(WeatherInfo wi){
		infos.add(wi);
		
	}
	

	
		

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void focusLayout(int layoutIndex){
		
		for(int i = 0; i < layouts.size(); i++){
			
			LinearLayout layout = layouts.get(i);
			layout.setX((lWidth*i)-(layoutIndex+1)*lWidth);
			Log.v("INDEX:",String.valueOf(layout.getX()));
		}
	}
	

}
