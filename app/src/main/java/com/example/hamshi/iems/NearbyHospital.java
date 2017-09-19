package com.example.hamshi.iems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.hamshi.iems.GoogleDirection.OnDirectionResponseListener;

public class NearbyHospital extends FragmentActivity implements	OnMarkerClickListener {
	private LocationController locCont;
	private GoogleMap mMap;
	private Marker userMarker;
	private Marker[] placeMarkers;
	private MarkerOptions[] places;
	private final int MAX_PLACES = 20;
	private static final LatLng COLOMBO = new LatLng(6.927079, 79.861244);
	private int userIcon, hospitalIcon;
	private double lat, lng;
	private Polyline poly = null;
	private ProgressDialog mDialog;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_hospitals);
		userIcon = R.drawable.yellowpoint;
		hospitalIcon = R.drawable.hospitalicon;
		
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
		mDialog.show();
		
		setUpMapIfNeeded();

	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMap();
				placeMarkers = new Marker[MAX_PLACES];
				updatePlaces();
			}
		}
	}

	private void setUpMap() {
		mMap.setOnMarkerClickListener(this);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(COLOMBO, 14));
	}

	private void updatePlaces() {
		locCont = new LocationController(NearbyHospital.this);
		if (locCont.canGetLocation()) {
			lat = locCont.getLatitude();
			lng = locCont.getLongitude();
			LatLng lastLatLng = new LatLng(lat, lng);
			if (userMarker != null)
				userMarker.remove();
			userMarker = mMap.addMarker(new MarkerOptions()
					.position(lastLatLng).title("You are here")
					.icon(BitmapDescriptorFactory.fromResource(userIcon))
					.snippet("Your last recorded location"));
			mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000,	null);

			String types = "hospital";
			try {
				types = URLEncoder.encode(types, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
					+ "json?location="	+ lat+ ","	+ lng
					+ "&radius=2000&sensor=true"
					+ "&types="	+ types
					+ "&key=AIzaSyCMiDT61_khExmkqcvvwawraUNPV_8Afq4";

			new GetPlaces().execute(placesSearchStr);
		} else {
			if(mDialog!=null)
				mDialog.dismiss();
			locCont.showSettingsAlert();
			// finish();
		}

		
	}

	private class GetPlaces extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... placesURL) {
			StringBuilder placesBuilder = new StringBuilder();
            try {
                URL requestUrl = new URL(placesURL[0]);
                HttpURLConnection connection = (HttpURLConnection)requestUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = null;

                    InputStream inputStream = connection.getInputStream();
                    if (inputStream == null) {
                        return "";
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        placesBuilder.append(line + "\n");
                    }

                    if (placesBuilder.length() == 0) {
                        return "";
                    }

                    Log.d("test", placesBuilder.toString());
                }
                else {
                    Log.i("test", "Unsuccessful HTTP Response Code: " + responseCode);
                }
            } catch (MalformedURLException e) {
                Log.e("test", "Error processing Places API URL", e);
            } catch (IOException e) {
                Log.e("test", "Error connecting to Places API", e);
            }
            
            return placesBuilder.toString();
		}

		protected void onPostExecute(String result) {
			if(mDialog!=null)
				mDialog.dismiss();
			if (placeMarkers != null) {
				for (int pm = 0; pm < placeMarkers.length; pm++) {
					if (placeMarkers[pm] != null)
						placeMarkers[pm].remove();
				}
			}

			try {
				JSONObject resultObject = new JSONObject(result);
				JSONArray placesArray = resultObject.getJSONArray("results");
				places = new MarkerOptions[placesArray.length()];
				for (int p = 0; p < placesArray.length(); p++) {
					boolean missingValue = false;
					LatLng placeLL = null;
					String placeName = "";
					String vicinity = "";
					// int currIcon = otherIcon;
					try {
						missingValue = false;
						JSONObject placeObject = placesArray.getJSONObject(p);
						JSONObject loc = placeObject.getJSONObject("geometry")
								.getJSONObject("location");
						placeLL = new LatLng(Double.valueOf(loc
								.getString("lat")), Double.valueOf(loc
								.getString("lng")));

						vicinity = placeObject.getString("vicinity");
						placeName = placeObject.getString("name");
					} catch (JSONException jse) {
						Log.v("PLACES", "missing value");
						missingValue = true;
						jse.printStackTrace();
					}
					if (missingValue)
						places[p] = null;
					else
						places[p] = new MarkerOptions()
								.position(placeLL)
								.title(placeName)
								.icon(BitmapDescriptorFactory
										.fromResource(hospitalIcon))
								.snippet(vicinity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (places != null && placeMarkers != null) {
				for (int p = 0; p < places.length && p < placeMarkers.length; p++) {
					if (places[p] != null)
						placeMarkers[p] = mMap.addMarker(places[p]);
				}
			}

		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		
		final LatLng position = marker.getPosition();
		final LatLng current = new LatLng(lat, lng);
		if(current!=position){
			if(poly != null ){
				poly.remove();
			}

			GoogleDirection direction = new GoogleDirection(this);
			direction.
					setOnDirectionResponseListener(new OnDirectionResponseListener() {
						public void onResponse(String status, Document doc,	GoogleDirection gd) {
							PolylineOptions opt = gd.getPolyline(doc, 3, Color.RED);
							poly = mMap.addPolyline(opt);

						}
					});
			direction.request(current, position,GoogleDirection.MODE_DRIVING);
		}
		if(!marker.isInfoWindowShown()){
			marker.showInfoWindow();
		} else {
			marker.hideInfoWindow();
		}
		
		return true;
	}
}
