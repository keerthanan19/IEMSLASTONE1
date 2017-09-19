package com.example.hamshi.iems;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hamshi.iems.GoogleDirection.OnDirectionResponseListener;

public class HospitalDirection extends FragmentActivity {
	private GoogleMap mMap;
	private LocationController locCont;
	private int userIcon, hospitalIcon;
	private GoogleDirection direction;
	private Document mDoc;
	private String name;
	private LatLng lastLatLng, hospitalCoordinates;
	private double hlat, hlon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_direction);
		userIcon = R.drawable.yellowpoint;
		hospitalIcon = R.drawable.hospitalicon;
		Bundle extras = getIntent().getExtras();
		hlat = extras.getDouble("lat");
		hlon = extras.getDouble("lon");
		name = extras.getString("name");
		direction = new GoogleDirection(this);
		setUpMapIfNeeded();

	}

	private void setUpMapIfNeeded() {

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.hospitalDirectionMap)).getMap();
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		locCont = new LocationController(HospitalDirection.this);
		if (locCont.canGetLocation()) {
			double lat = locCont.getLatitude();
			double lng = locCont.getLongitude();
			lastLatLng = new LatLng(lat, lng);

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 14));
			hospitalCoordinates = new LatLng(hlat, hlon);
			direction
					.setOnDirectionResponseListener(new OnDirectionResponseListener() {
						public void onResponse(String status, Document doc,
								GoogleDirection gd) {
							mDoc = doc;
							mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
							mMap.addMarker(new MarkerOptions()
									.position(lastLatLng)
									.title("Your Current Position")
									.icon(BitmapDescriptorFactory
											.fromResource(userIcon))
									.snippet("Your Location"));

							mMap.addMarker(new MarkerOptions()
									.position(hospitalCoordinates)
									.title(name)
									.icon(BitmapDescriptorFactory
											.fromResource(hospitalIcon)));

						}
					});

			direction.request(lastLatLng, hospitalCoordinates,
					GoogleDirection.MODE_DRIVING);

		} else {
			locCont.showSettingsAlert();
		}

	}

}