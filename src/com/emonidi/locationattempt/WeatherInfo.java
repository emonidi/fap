package com.emonidi.locationattempt;

import android.os.Parcel;
import android.os.Parcelable;



class WeatherInfo {
	public int temperature_high;
	public int temperature_low;
	public Double temp_now;
	
	
	public String conditions;
	public String weekDay;
	public String day;
	public String month;
	public String year;
	public String prettyDate;
	public String icon;
	public String shortWeekDay;
	public String type;
	
	public String prettyDate(){
		prettyDate = weekDay+" "+day+" "+month+" "+year;
		return prettyDate;
	}
	
	

	
}
