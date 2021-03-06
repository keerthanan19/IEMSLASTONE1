package com.example.hamshi.iems;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.hamshi.iems.WebService.User;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Doctor_login   extends Activity implements View.OnClickListener {
    String myJSON;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    JSONArray peoples = null;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_DNAME = "Dname";
    private static final String TAG_DPASSWORD ="Dpassword";
    private static final String TAG_DEMAIL ="Demail";

    public static String Did = "";

    EditText txtUsr, txtPwd;
    DatabaseHelper helper =new DatabaseHelper(this);
    SessionManager session;
    AlertDialogManager alert = new AlertDialogManager();
   // User aUser = new User();

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__login);
        session = new SessionManager(getApplicationContext());
        Button signIn = (Button) findViewById(R.id.btn_doctor_signin);
        signIn.setOnClickListener(this);

        Button forgotPassw = (Button) findViewById(R.id.btn_DforgotPassword);
        forgotPassw.setOnClickListener(this);

        txtUsr = (EditText) findViewById(R.id.txtDoctorname);
        txtPwd = (EditText) findViewById(R.id.txtDPassword);

    }

    @Override
    public void onClick(View V) {
        //SignIn bookStoreApp = new SignIn();


        if (V.getId() == R.id.btn_doctor_signin) {

            String username = txtUsr.getText().toString();
            String password = txtPwd.getText().toString();

            if (username.trim().length() > 0 && password.trim().length() > 0) {
                getData();

            } else {
                alert.showAlertDialog(Doctor_login.this, "Login failed..",
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
                Did = c.getString(TAG_ID);
                String user_name = c.getString(TAG_DNAME);
                String email = c.getString(TAG_DEMAIL);
                String passw = c.getString(TAG_DPASSWORD);


                if (password.equals(passw)&& username.equals(user_name)){
                   session.createLoginSession(user_name,email,Did,"D");

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
                    Intent intent = new Intent(this, PatientList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    this.startActivity(intent);
                 //   Toast.makeText(Doctor_login.this, user_name, Toast.LENGTH_SHORT).show();
                    intent.putExtra("doctor_id",Did);
                    break;
                }


            }
            if(j==0) {
                alert.showAlertDialog(Doctor_login.this, "Login failed..",
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
                HttpPost httppost = new HttpPost("http://192.168.1.9/doctor.php");

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
                    // Oops
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




 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__login);

        Button skipbutton_dd = (Button)findViewById(R.id.skipbutton_dd);

        skipbutton_dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setContentView(R.layout.patient_list);

            }
        });

    }*/
}
