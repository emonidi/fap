package com.emonidi.locationattempt;

import android.graphics.drawable.Drawable;


public class ImagePicker {
	int icon;
		
	public int getBigImage(String condition){
		if(condition.equals("mostlycloudy")){
			icon = R.drawable.mostlycloudy;
		}
		if(condition.equals("cloudy")){
			icon = R.drawable.cloudy;
		}
		if(condition.equals("clear")){
			icon = R.drawable.clear;
		}
		if(condition.equals("flurries")){
			icon = R.drawable.flurries;
		}
		if(condition.equals("hazy")){
			icon = R.drawable.hazy;
		}
		if(condition.equals("mostlysunny")){
			icon = R.drawable.mostlysunny;
		}
		if(condition.equals("partlycloudy")){
			icon = R.drawable.partlycloudy;
		}
		if(condition.equals("partlysunny")){
			icon = R.drawable.partlysunny;
		}
		if(condition.equals("sleet")){
			icon = R.drawable.sleet;
		}
		if(condition.equals("snow")){
			icon = R.drawable.snow;
		}
		if(condition.equals("tstorms")){
			icon = R.drawable.tstorms;
		}
				
		return icon;
		
	}
	
public int getSmallImage(String condition){
		
	if(condition.equals("mostlycloudy")){
		icon = R.drawable.mostlycloudy_64;
	}
	if(condition.equals("cloudy")){
		icon = R.drawable.cloudy_64;
	}
	if(condition.equals("clear")){
		icon = R.drawable.clear_64;
	}
	if(condition.equals("flurries")){
		icon = R.drawable.flurries_64;
	}
	if(condition.equals("hazy")){
		icon = R.drawable.hazy_64;
	}
	if(condition.equals("mostlysunny")){
		icon = R.drawable.mostlysunny_64;
	}
	if(condition.equals("partlycloudy")){
		icon = R.drawable.partlycloudy_64;
	}
	if(condition.equals("partlysunny")){
		icon = R.drawable.partlysunny_64;
	}
	if(condition.equals("sleet")){
		icon = R.drawable.sleet_64;
	}
	if(condition.equals("snow")){
		icon = R.drawable.snow_64;
	}
	if(condition.equals("tstorms")){
		icon = R.drawable.tstorms_64;
	}
			
	return icon;
		
	}
}
