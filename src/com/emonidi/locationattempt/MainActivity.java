package com.emonidi.locationattempt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;



public class MainActivity extends FragmentActivity {
	

	
	
	public static final String PREFS_NAME = "weatherInfo";
	
	String lat;
	String lng;
	
	
	
	ArrayList<WeatherInfo> weatherInfo;
	
	private WeatherInfo conditions;
	
	private static AsyncHttpClient client  =  new AsyncHttpClient();
	
	//layouts
	FrameLayout splashLayout;
	LinearLayout weatherLayout;
	LinearLayout temperatureLayout;
	
	LinearLayout l1;
	LinearLayout l2;
	LinearLayout l3;
	LinearLayout l4;
	
	
	
	//textViews
	TextView cityTextView;
	TextView countryTextView;
	TextView dateTextView;
	TextView temperatureTextView;
	TextView conditionsTextView;
	ScrollManager sm;
	
	
	
		
	WeatherLocation weatherLocation = new WeatherLocation();
	ImagePicker iPicker = new ImagePicker();
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
         
        
         l1 = (LinearLayout) findViewById(R.id.foreCastDayLayout_1);
         l2 = (LinearLayout) findViewById(R.id.foreCastDayLayout_2);
         l3 = (LinearLayout) findViewById(R.id.foreCastDayLayout_3);
         l4 = (LinearLayout) findViewById(R.id.foreCastDayLayout_4);
        
         
        
         sm = new ScrollManager(this);
         	
        
       
      
        //initialize weatherLocation
        
        splashLayout = (FrameLayout) findViewById(R.id.splashLayout);
        weatherLayout = (LinearLayout) findViewById(R.id.weatherLayout);
        //hide weatherLayout
        
        rotateSplash();
        
    	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	LocationListener locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
    	      makeUseOfNewLocation(location);
    	    }
    	    
    	    private void makeUseOfNewLocation(Location location) {
    	    	if (location != null) {    	    		
    	    		Log.v("Location", location.toString());
    	    		lat = String.valueOf(location.getLatitude());
    	    		lng = String.valueOf(location.getLongitude());
    	    		
    	    		getCity(lat,lng);
    	    		try {
						getWeather(lat,lng);
						getDayLight(lat,lng);
						getConditions(lat,lng);
				         

					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	    	}else{
    	    		Log.v("Location","LOCATION IS NULL");
    	    	}
    	    	
    	    }

			public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	weatherLayout.setVisibility(View.GONE);
    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000000, 100, locationListener);
    	  
    		
    	    	
    }
    
	private void rotateSplash(){
	    	RotateAnimation rAnimation = new RotateAnimation(0f,350f,60f,60f);
	    	rAnimation.setInterpolator(new LinearInterpolator());
	    	rAnimation.setRepeatCount(Animation.INFINITE);
	    	rAnimation.setDuration(2000);
	    	
	    	ImageView splashLogoImageView = (ImageView) findViewById(R.id.splashLogoImageView);
	}
    
    private void getConditions(String lat, String lng){
    	client.get("http://api.wunderground.com/api/45449d3bedba48af/conditions/q/"+lat+","+lng+".json",new AsyncHttpResponseHandler(){
    			public void onSuccess(String response){
    				conditions = new WeatherInfo();
    				JsonParser cParser = new JsonParser();
    				JsonElement currentObservation = cParser.parse(response)
    						.getAsJsonObject().get("current_observation");
    				conditions.icon = currentObservation.getAsJsonObject().get("icon").getAsString();
    				conditions.temp_now = Double.parseDouble(currentObservation.getAsJsonObject().get("temp_c").getAsString());
    				String[] prettyDate = currentObservation.getAsJsonObject().get("local_time_rfc822").getAsString().split(" ");
    				conditions.prettyDate = prettyDate[0]+" "+prettyDate[1]+" "+prettyDate[2]+" "+prettyDate[3];
    				conditions.conditions = currentObservation.getAsJsonObject().get("weather").getAsString();
    				
    				
    				
    				String tempColor = getColor(Double.parseDouble(conditions.temp_now.toString()));
        	    	int color = Color.parseColor(tempColor);
        	    	cityTextView = (TextView) findViewById(R.id.cityTextView);
        	    	countryTextView = (TextView) findViewById(R.id.countryTextView);
        	    	dateTextView = (TextView) findViewById(R.id.dateTextView);
        	    	temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        	    	conditionsTextView = (TextView) findViewById(R.id.conditionsTextView);
        	    	
        	    	cityTextView.setText(weatherLocation.city);
        	    	countryTextView.setText(weatherLocation.country);
        	    	dateTextView.setText(conditions.prettyDate);
        	    	dateTextView.setTextColor(color);
        	    	temperatureTextView.setText(String.valueOf(conditions.temp_now)+"\u2103 ");
        	    	
        	    	ImageView icon = (ImageView) findViewById(R.id.icon);
        	    	
        	    	icon.setImageResource(iPicker.getBigImage(conditions.icon));
        	    	icon.invalidate();
        	    	
        	    	conditionsTextView.setText(conditions.conditions);
        	    	
        	    	temperatureLayout = (LinearLayout) findViewById(R.id.temperatureLayout);
        	    	temperatureLayout.setBackgroundColor(color);
        	    	
        	    	sm.addWeatherInfo(conditions);
    			}
    			
    			
    			
    			
    	    	
    	});
    	
    	
    }
    
    
    private void getDayLight(String lat, String lng){
    	client.get("http://api.wunderground.com/api/45449d3bedba48af/astronomy/q/"+lat+","+lng+".json",new AsyncHttpResponseHandler(){
    		 public void onSuccess(String response){
    			 JsonParser dlParser = new JsonParser();
    			 JsonElement sunPhase = dlParser.parse(response)
    					 .getAsJsonObject().get("sun_phase");
    			 JsonElement sunRise = sunPhase.getAsJsonObject().get("sunrise");
    			 
    			 String sunriseHour = sunRise.getAsJsonObject().get("hour").getAsString();
    			 String sunriseMinute = sunRise.getAsJsonObject().get("minute").getAsString();
    			 
    			 Time sunriseTime = new Time();
    			 sunriseTime.set(Integer.parseInt(sunriseHour), Integer.parseInt(sunriseMinute), 0);
    			 
    			 weatherLocation.setSunrise(sunriseTime);
    			 
    			 JsonElement sunSet = sunPhase.getAsJsonObject().get("sunset");
    			 
    			 String sunsetHour = sunSet.getAsJsonObject().get("hour").getAsString();
    			 String sunsetMinute = sunSet.getAsJsonObject().get("minute").getAsString();
    			 
    			 Time sunsetTime = new Time();
    			 sunsetTime.set(Integer.parseInt(sunriseHour), Integer.parseInt(sunriseMinute), 0);
    			 
    			 weatherLocation.setSunset(sunsetTime);
    			 
    		 }
    	});
    }
   
    
    private void getCity(String lat,String lng){
    	Geocoder geoCoder = new Geocoder(getApplicationContext());
    	List<Address> addresses = null;
    	try{
    		addresses = geoCoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng),1);
    		if(addresses != null && addresses.size() > 0){
    			
    			Log.v("Address",addresses.get(0).toString());
    			weatherLocation.city = addresses.get(0).getLocality();
    			weatherLocation.country = addresses.get(0).getCountryName();
    		}else{
    			Log.v("Adress","NULL");
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
 

    private void getWeather(String lat, String lng){
    
    	client.get("http://api.wunderground.com/api/45449d3bedba48af/forecast/q/"+lat+","+lng+".json", new AsyncHttpResponseHandler(){
    		 @Override
    		    public void onSuccess(String response) {
    			 	
    			 	new Gson();
    			 	JsonParser jsonParser = new JsonParser();
    			 	JsonArray weatherArray = (JsonArray) jsonParser.parse(response)
    			 			.getAsJsonObject().get("forecast")
    			 			.getAsJsonObject().get("simpleforecast")
    			 			.getAsJsonObject().get("forecastday").getAsJsonArray();
    			 		
    			 	weatherInfo = new ArrayList<WeatherInfo>();
    			 
    			 	for(int i = 0; i < weatherArray.size(); i++ ){
    			 			WeatherInfo wi = new WeatherInfo();
    	    			 	JsonObject mainWeather = (JsonObject) weatherArray.get(i);
    	    			 	wi.temperature_high = Integer.parseInt(mainWeather.getAsJsonObject().get("high").getAsJsonObject().get("celsius").getAsString());
    	    			 	wi.temperature_low =  Integer.parseInt(mainWeather.getAsJsonObject().get("low").getAsJsonObject().get("celsius").getAsString());
    	    			 	wi.conditions =  mainWeather.getAsJsonObject().get("conditions").getAsString();
    	    			 	wi.icon = mainWeather.getAsJsonObject().get("icon").getAsString();
    	    				JsonObject date =  (JsonObject) mainWeather.getAsJsonObject().get("date");
    	    				wi.weekDay = date.getAsJsonObject().get("weekday").getAsString();
    	    				wi.shortWeekDay = date.getAsJsonObject().get("weekday_short").getAsString();
    	    				wi.day = date.getAsJsonObject().get("day").getAsString();
    	    				wi.month = date.getAsJsonObject().get("monthname").getAsString();
    	    				wi.year = date.getAsJsonObject().get("year").getAsString();
    	    				
    	    				weatherInfo.add(wi);
    	    				sm.addWeatherInfo(wi);
    	    				
    			 	}
    			 		
    			 	 
    			 	
    			 	
    			 	
    				
    				
    				deployWeather(weatherInfo.get(0));
    				Log.e("DEPLOYED","TRUE");
    		 }
    	});
    }
    
    @SuppressWarnings("null")
	public String getColor(Double temperature){
		
    	
		if(temperature <= 10){
			return "#3498db";
		}
		if(temperature <= 20){
			return "#1abc9c";
		}
		if(temperature <= 30){
			return "#f1c40f";
		}
		if(temperature > 30){
			return "#e74c3c";
		}
		return null;
		
		
    	
    	
	}
    
    private void deployWeather(WeatherInfo weatherInfo){
    
    	
    	
    
    	setDayForecastLayouts(l1,0);
    	setDayForecastLayouts(l2,1);
    	setDayForecastLayouts(l3,2);
    	setDayForecastLayouts(l4,3);
    
	   	
	   	
    	splashLayout.setVisibility(View.GONE);
    	weatherLayout.setVisibility(1);
    	
    }
    
    private void setDayForecastLayouts(LinearLayout layout, final int weatherInfoIndex){
    	
    	int color = Color.parseColor(getColor(Double.parseDouble(String.valueOf(weatherInfo.get(weatherInfoIndex).temperature_high))));
    	TextView weekDay = (TextView) layout.findViewWithTag("weekDay");
    	Log.e("WeeKDay",weatherInfo.get(weatherInfoIndex).weekDay);
    	weekDay.setText(weatherInfo.get(weatherInfoIndex).shortWeekDay.toUpperCase());
    	weekDay.setTextColor(color);
    	
    	TextView weekDayTemperature = (TextView) layout.findViewWithTag("weekDayTemperature");
    	weekDayTemperature.setText(String.valueOf(weatherInfo.get(weatherInfoIndex).temperature_high)+"\u2103");
    	ImageView dailyForeCastIcon  = (ImageView) layout.findViewWithTag("dailyForeCastIcon");
    	dailyForeCastIcon.setImageResource(iPicker.getSmallImage(weatherInfo.get(weatherInfoIndex).icon));
    	Log.e("SMALL ICON",weatherInfo.get(weatherInfoIndex).icon.toString());
    	dailyForeCastIcon.invalidate();
    	
    	layout.setOnClickListener(new OnClickListener(){
    	
			@Override
			public void onClick(View v) {
				
				sm.focusLayout(weatherInfoIndex);
			}
    		
    	});
    }
    
    
    
    @Override
	protected void onStop(){
		super.onStop();
		SharedPreferences weatherInfoStorage = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = weatherInfoStorage.edit();
		
		
			editor.putInt("temperature", conditions.temperature_high);
			editor.putString("country", weatherLocation.country);
			editor.putString("city", weatherLocation.city);
			editor.putString("dateString", conditions.prettyDate);
			editor.putString("conditions",conditions.conditions);
			editor.putString("icon",conditions.icon);
			
			editor.putString("prettyDate",conditions.prettyDate);
			
		
			editor.commit();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
    }
    
}
