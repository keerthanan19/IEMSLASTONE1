package com.example.hamshi.iems;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.example.hamshi.iems.WebService.User;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SignIn extends Activity implements OnClickListener {
	private BroadcastReceiver mRegistrationBroadcastReceiver;
	String myJSON;

	JSONArray peoples = null;
	private static final String TAG_RESULTS="result";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_USER_NAME = "user_name";
	private static final String TAG_EMAIL ="email";
	private static final String TAG_PASSWORD ="user_pass";
	EditText txtUsr, txtPwd;
DatabaseHelper helper =new DatabaseHelper(this);
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();
	//User aUser = new User();

	public static String id = "";

	@Override


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);


		session = new SessionManager(getApplicationContext());
		Button signIn = (Button) findViewById(R.id.btn_signin);
		signIn.setOnClickListener(this);

		Button forgotPassw = (Button) findViewById(R.id.btn_forgotPassword);
		forgotPassw.setOnClickListener(this);

		txtUsr = (EditText) findViewById(R.id.txtUsername);
		txtPwd = (EditText) findViewById(R.id.txtPassword);

	}

	@Override
	public void onClick(View V) {
		SignIn bookStoreApp = new SignIn();
		 if (V.getId() == R.id.btn_signin) {

			String username = txtUsr.getText().toString();
			String password = txtPwd.getText().toString();

			if (username.trim().length() > 0 && password.trim().length() > 0) {
				try {
					getData();
				}catch (Exception e){
					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.iemstoast,
							(ViewGroup) findViewById(R.id.custom_toast_container));

					TextView text = (TextView) layout.findViewById(R.id.text);
					text.setText("Please check internet/database connection");

					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setView(layout);
					toast.show();

				}

			} else {
				alert.showAlertDialog(SignIn.this, "Login failed..",
						"Please enter username and password", false);
			}

		}
	}

	protected void showList(){
		try {
			String username = txtUsr.getText().toString();
			String password = txtPwd.getText().toString();

			JSONObject jsonObj = new JSONObject(myJSON);
			peoples = jsonObj.getJSONArray(TAG_RESULTS);
			//personList=null;
			int j=0;
			for(int i=0;i<peoples.length();i++){
				JSONObject c = peoples.getJSONObject(i);
				id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				String user_name = c.getString(TAG_USER_NAME);
				String email = c.getString(TAG_EMAIL);
				String passw = c.getString(TAG_PASSWORD);


				if (password.equals(passw)&& username.equals(user_name)){
					session.createLoginSession(name,email,id,"U");
					mRegistrationBroadcastReceiver = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {
							//Check type of intent filter
							if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
								//Registration success
								String token = intent.getStringExtra("token");
								Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
							} else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
								//Registration error
								Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
							} else {
								//Tobe define
							}
						}
					};

					//Check status of Google play service in device
					int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
					if(ConnectionResult.SUCCESS != resultCode) {
						//Check type of error
						if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
							Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
							//So notification
							GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
						} else {
							Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
						}
					} else {
						//Start service
						Intent itent = new Intent(this, GCMRegistrationIntentService.class);
						startService(itent);
					}

					j=1;
					Intent intent = new Intent(this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
					this.startActivity(intent);
					break;
				}


				}
			if(j==0) {
				alert.showAlertDialog(SignIn.this, "Login failed..",
						"Please check username and password", false);
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


		public void getData() {

			 class GetDataJSON extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
				HttpPost httppost = new HttpPost("http://192.168.1.9/user.php");

				// Depends on your web service
				httppost.setHeader("Content-type", "application/json");

				InputStream inputStream = null;
				String result = null;
				try {
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					inputStream = entity.getContent();
					// json is UTF-8 by default
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					StringBuilder sb = new StringBuilder();

					String line = null;
					while ((line = reader.readLine()) != null)
					{
						sb.append(line + "\n");
					}
					result = sb.toString();
				} catch (Exception e) {
					//String pid = "Please check Internet/DB connection";
					//Toast.makeText(SignIn.this, pid, Toast.LENGTH_SHORT).show();
				}
				finally {
					try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result){
				myJSON=result;
				showList();

			}
		}
		GetDataJSON g = new GetDataJSON();
		g.execute();
	}

	/*@Override
	protected void onResume() {
		super.onResume();
		Log.w("SignIn", "onResume");
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
		LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
				new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.w("SignIn", "onPause");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
	}*/
}
