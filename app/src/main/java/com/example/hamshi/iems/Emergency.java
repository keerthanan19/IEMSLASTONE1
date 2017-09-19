/*package com.example.hamshi.iems;

import org.json.JSONObject;

import com.example.hamshi.iems.WebService.Hospital;
//import com.example.hamshi.iems.WebService.RestAPI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Emergency extends Activity {
	private TextView tvName;
	private TextView tvContact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emergency);


		/*tvName = (TextView) findViewById(R.id.txtNearbyHospitalName);
		tvContact = (TextView) findViewById(R.id.txtNearbyHospitalContact);
		Toast.makeText(this, "Loading Please Wait..", Toast.LENGTH_SHORT)
				.show();
		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// get last location
		Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		double lat = lastLoc.getLatitude();
		double lon = lastLoc.getLongitude();
		new AsyncLoadHospitalDetails().execute(lat, lon);*/

	/*}

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

	protected class AsyncLoadHospitalDetails extends
			AsyncTask<Double, JSONObject, Hospital> {
		@Override
		protected Hospital doInBackground(Double... params) {

	//		RestAPI api = new RestAPI();
			Hospital aHospital = null;
			try {

				// Call the User Authentication Method in API
		//		JSONObject jsonObj = api.getNearestHospital(params[0],
		//				params[1]);
		//		Log.d("received object", jsonObj.toString());
				// Parse the JSON Object to Hospital
			//	JSONParser parser = new JSONParser();
			//	aHospital = parser.parseHospitalDetail(jsonObj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("AsyncLoadHospitalDetails", e.getMessage());

			}
			return aHospital;
		}

		@Override
		protected void onPostExecute(Hospital result) {
			// TODO Auto-generated method stub

			if (result != null) {
				tvName.setText(result.getHospitalName());
				tvContact.setText(result.getContactNo());
				Toast.makeText(Emergency.this, "Loading Completed",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Emergency.this,
						"Something is wrong", Toast.LENGTH_SHORT)
						.show();
			}
			
		}
	}

}*/
