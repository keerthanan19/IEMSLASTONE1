/*package com.example.hamshi.iems;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hamshi.iems.WebService.Hospital;
//import com.example.hamshi.iems.WebService.RestAPI;

public class EmergencyOption extends Activity implements OnClickListener {
	private LocationController locCont;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency_option);

		ImageButton sms = (ImageButton) findViewById(R.id.btn_sendSMS);
		sms.setOnClickListener(this);

		ImageButton phone = (ImageButton) findViewById(R.id.btn_callHospital);
		phone.setOnClickListener(this);
	}

	public void sendSms() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Title");

// Set up the input
		final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		input.setHint("mobile number");
		builder.setView(input);

// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String phoneNo = input.getText().toString();
				String message = "message to be send";
				try {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phoneNo, null, message, null, null);
					Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
				} catch (Exception e) {

				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.show();
	}

	@Override
	public void onClick(View V) {
		// TODO Auto-generated method stub
		if (V.getId() == R.id.btn_sendSMS) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			boolean isLogged = new SessionManager(getApplicationContext())
					.isLoggedIn();
			if (isLogged) {
				alertDialogBuilder.setTitle("Send Emergency Text Messages");

				alertDialogBuilder
						.setMessage(
								"Would you like to send emergency text messages to your emergency contacts?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										locCont = new LocationController(EmergencyOption.this);
										if (locCont.canGetLocation()) {
											double lat = locCont.getLatitude();
											double lng = locCont.getLongitude();

											String phoneNo = "8421881175";	//phone to which sms will be sent
											String message = "Lat : "+lat+"\nLon : "+lng;
											try {
												SmsManager smsManager = SmsManager.getDefault();
												smsManager.sendTextMessage(phoneNo, null, message, null, null);
												Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
											} catch (Exception e) {

											}


										/*	String userName = new SessionManager1(
													getApplicationContext())
													.getUsername();
											new AsyncSendMessages(lat, lng,
													userName).execute();
*/
								/*			dialog.cancel();
										} else {
											locCont.showSettingsAlert();
										}
										
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			} else {
				alertDialogBuilder.setTitle("Not signed in");

				alertDialogBuilder
						.setMessage(
								"You need to sign in / register in order to use this service. Would you like to log in now?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(EmergencyOption.this, SignIn.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
												| Intent.FLAG_ACTIVITY_NO_ANIMATION);
										startActivity(intent);
										finish();
										
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			}
		} else if (V.getId() == R.id.btn_callHospital) {
			locCont = new LocationController(
					EmergencyOption.this);
			if (locCont.canGetLocation()) {
				double lat = locCont.getLatitude();
				double lng = locCont.getLongitude();
				new AsyncLoadNearestHospital().execute(lat, lng);
			} else {
				locCont.showSettingsAlert();
			}
			
		}

	}

	protected class AsyncSendMessages extends
			AsyncTask<Void, JSONObject, Boolean> {
		private ProgressDialog pDialog;
		String longUrl, user;
		double lat, lon;

		public AsyncSendMessages(double lat, double lon, String user) {
			this.lat = lat;
			this.lon = lon;
			this.user = user;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EmergencyOption.this);
			pDialog.setTitle("Working");
			pDialog.setMessage("Sending emergency messages");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			longUrl = "http://maps.google.com/?q=" + lat + "," + lon;
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... args) {
			String shortURL = "";
		//	RestAPI api = new RestAPI();
		//	JSONObject json = api.getShortURL(longUrl);

		//	if (json != null) {
			//	shortURL = new JSONParser().parseURL(json);
		//	}
			JSONObject obj = null;
			boolean isSuccess = false;
			try {
			//	obj = api.sendMessages(user, shortURL);
				isSuccess = obj.getBoolean("Successful");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return isSuccess;
		}

		@Override
		protected void onPostExecute(Boolean success) {
			pDialog.dismiss();
			if (success) {
				Toast.makeText(EmergencyOption.this,
						"Emergency SMSs has been sent", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(EmergencyOption.this,
						"Connectivity error occured. Try again later!", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	protected class AsyncLoadNearestHospital extends
			AsyncTask<Double, JSONObject, Hospital> {
		private ProgressDialog pDialog;
		boolean isSuccess = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EmergencyOption.this);
			pDialog.setTitle("Working");
			pDialog.setMessage("Locating nearest hospital");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Hospital doInBackground(Double... params) {
			com.example.hamshi.iems.WebService.Hospital aHospital = new Hospital();
	//		RestAPI api = new RestAPI();
			JSONObject json = null;
			try {
		//		json = api.getNearestHospital(params[0], params[1]);
				isSuccess = json.getBoolean("Successful");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(isSuccess){
			//	aHospital = new JSONParser().parseHospitalDetail(json);
			}
			

			return aHospital;
		}

		@Override
		protected void onPostExecute(final Hospital aHospital) {
			pDialog.dismiss();
			if (isSuccess) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						EmergencyOption.this);

				alertDialogBuilder.setTitle("Call Emergency Hotline");

				alertDialogBuilder
						.setMessage(
								"Nearest hospital to your current location is "
										+ aHospital.getHospitalName()
										+ ". Would you like to make an emergency call?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											Intent callIntent = new Intent(
													Intent.ACTION_CALL);
											callIntent.setData(Uri.parse("tel:"
													+ aHospital.getContactNo()));
											//startActivity(callIntent);
										} catch (ActivityNotFoundException activityException) {
											Log.e("Calling a Phone Number",
													"Call failed",
													activityException);
										}
										dialog.cancel();
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			} else {
				Toast.makeText(EmergencyOption.this,
						"Connectivity error occured. Try again later!", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}
}
*/