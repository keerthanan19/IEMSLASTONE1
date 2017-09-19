package com.example.hamshi.iems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;

public class MainActivity extends Activity implements OnClickListener {
    DatabaseHelper myDb;
    String myJSON;
    public static  String carrier_phone_no=null;
    JSONArray peoples = null;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_Uname = "name";

    private static final String phone_no ="phone_no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);

        Log.d("patient id", SignIn.id);
        getData();

        Button symptomButton = (Button) findViewById(R.id.btn_symptomChecker);
        symptomButton.setOnClickListener(this);

        Button hospitalButton = (Button) findViewById(R.id.btn_nearbyHospitals);
        hospitalButton.setOnClickListener(this);

        Button emergencyButton = (Button) findViewById(R.id.btn_emergency);
        emergencyButton.setOnClickListener(this);

        Button preferencesButton = (Button) findViewById(R.id.btn_preferences);
        preferencesButton.setOnClickListener(this);

        emergencyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Send SMS");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
              //  input.setHint("contact");
                input.setText(carrier_phone_no);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        String phoneNo = input.getText().toString().trim();	//phone to which sms will be sent
                        String message = "Please contact emergency";
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
        });

    }
    protected void showList(){
        String pid = Doctor_login.Did;;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Person> temp=new ArrayList<Person>();
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_Uname);

                String P_no = c.getString(phone_no);

                if (id.equalsIgnoreCase(SignIn.id) ) {
                    carrier_phone_no=P_no;
                }
            }

            Toast.makeText(MainActivity.this, ""+carrier_phone_no, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/listcarrier.php");

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //menu.findItem(R.id.action_search).setVisible(false);
        return true;
    }

    @Override
    public void onClick(View V) {

        if (V.getId() == R.id.btn_symptomChecker) {
            boolean isLoggedIn = new SessionManager(getApplicationContext()).isLoggedIn();
//			if (isLoggedIn) {
//				Intent intent = new Intent(this, SymptomCheckerOption.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				this.startActivity(intent);
//			} else {
//				Intent intent = new Intent(this, SignIn.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
//				this.startActivity(intent);
//			}

            Vaidhya symList = (Vaidhya) this.getApplicationContext();
           // symList.clearList();
            SessionManager sm = new SessionManager(getApplicationContext());
            if(!sm.isLoggedIn()){
                Intent intent = new Intent(this, SymptomCheckerOption.class);//is should be UserDetail.class
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                this.startActivity(intent);
            }else{

                //Intent intent = new Intent(this, SymptomNavigator.class); //this right
                Intent intent = new Intent(this, SymptomCheckerOption.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                this.startActivity(intent);
            }
        }

        else if (V.getId() == R.id.btn_preferences) {
            boolean isLoggedIn = new SessionManager(getApplicationContext()).isLoggedIn();
            if (isLoggedIn) {
                Intent intent = new Intent(this, ConsultantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
                this.startActivity(intent);
            } else {
                Intent intent = new Intent(this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
                this.startActivity(intent);
            }
        }

        else if (V.getId() == R.id.btn_nearbyHospitals) {
            Intent intent = new Intent(this, NearbyHospital.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity(intent);
        }

       /* else if (V.getId() == R.id.btn_emergency) {
            Intent intent = new Intent(this, emergency.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity(intent);*/
        }

    }


