package com.example.hamshi.iems;

public class Doctor {
	private String doctorName;
	private String hospitalName;
	private String schedule;

	public Doctor(){
		super();
	}
	public Doctor(String doctorName, String hospitalName, String schedule) {

		super();
		this.doctorName = doctorName;
		this.hospitalName = hospitalName;
		this.schedule = schedule;
	}
//	private double distance;
//	private double latitude;
//	private double longitude;
	
	/*public void setLatitude(double lat){
		latitude = lat;
	}
	
	public void setLongitude(double lon){
		longitude = lon;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	public void setDistance(double distance){
		this.distance = distance;
	}

	public double getDistance(){
		return distance;
	}*/

	public void setDoctorName(String name) {
		doctorName = name;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setHospitalName(String name) {
		hospitalName = name;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getSchedule() {
		return schedule;
	}

	@Override
	public String toString() {
		return this.doctorName + ". " + this.hospitalName  +". " + this.schedule;
	}
}
