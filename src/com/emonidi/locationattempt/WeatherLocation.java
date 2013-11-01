package com.emonidi.locationattempt;

import android.text.format.Time;

public class WeatherLocation{
	String city;
	String country;
	Time sunrise;
	Time sunset;
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public void setCountry(String country){
		this.country = country;
	}
	
	public String getCountry(){
		return this.country;
	}
	
	public void setSunrise(Time sunrise){
		this.sunrise = sunrise;
	}
	
	public Time getSunrise(){
		return this.sunrise;
	}
	
	public void setSunset(Time sunset){
		this.sunset = sunset;
	}
	
	public Time getSunset(){
		return this.sunset;
	}
	
	
}