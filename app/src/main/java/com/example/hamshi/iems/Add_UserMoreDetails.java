package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.HashMap;

public class Add_UserMoreDetails extends Activity {
    JSONArray peoples = null;
    ArrayList<String> personList;
    final ArrayList<String> list = new ArrayList<String>();
    ListView list1;
    String myJSON;
    boolean ischeck =true;
    //  public static boolean check =false;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_DISEASE = "Disease";
    private static final String TAG_AGE ="Age";
    private static final String TAG_ANAME ="Aname";
    private static final String TAG_STATUS ="Status";
    private static final String TAG_AYEAR ="Ayear";
    private static final String TAG_UID ="Uid";
    private static final String TAG_ATYPE ="Atype";
    private static final String TAG_APROBLEM ="Aproblem";
    private static final String TAG_SNAME ="Sname";
    private static final String TAG_SYEAR ="Syear";
    private static final String TAG_STYPE ="Stype";
    private static final String TAG_SPROBLEM ="Sproblem";
    private static final String TAG_MNAME ="Mname";
    private static final String TAG_MVALUE ="Mvalue";
    SessionManager session;
    private String disease,Age,Status,Aname,Ayear,user_id,Atype,Aproblem,Sname,Syear,Stype,Sproblem,Mname,Mvalue;
    EditText ET_DISEASE,ET_AGE,ET_STATUS,ET_ANAME,ET_AYEAR,ET_ATYPE,ET_APROBLEM,ET_SNAME,ET_SYEAR,ET_STYPE,ET_SPROBLEM,ET_MNAME,ET_MVALUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__more_user);
        Button btn_doctor = (Button)findViewById(R.id.Doctor_list);
        getData();
        ET_DISEASE = (EditText)findViewById(R.id.Disease);
        ET_AGE= (EditText)findViewById(R.id.Age);
        ET_STATUS = (EditText)findViewById(R.id.Status);
        ET_ANAME = (EditText)findViewById(R.id.Aname);
        ET_AYEAR = (EditText)findViewById(R.id.Ayear);
        ET_ATYPE = (EditText)findViewById(R.id.Atype);
        ET_APROBLEM = (EditText)findViewById(R.id.Aproblem);
        ET_SNAME = (EditText)findViewById(R.id.Sname);
        ET_SYEAR = (EditText)findViewById(R.id.Syear);
        ET_STYPE = (EditText)findViewById(R.id.Stype);
        ET_SPROBLEM = (EditText)findViewById(R.id.Sproblem);
        ET_MNAME = (EditText)findViewById(R.id.Mname);
        ET_MVALUE = (EditText)findViewById(R.id.Mvalue);

        disease=ET_DISEASE.getText().toString();
        Age = ET_AGE.getText().toString();
        Status =ET_STATUS.getText().toString();
        Aname =ET_ANAME.getText().toString();
        Ayear =ET_AYEAR.getText().toString();
        Atype =ET_ATYPE.getText().toString();
        Aproblem =ET_APROBLEM.getText().toString();
        Sname =ET_SNAME.getText().toString();
        Syear =ET_SYEAR.getText().toString();
        Stype =ET_STYPE.getText().toString();
        Sproblem =ET_SPROBLEM.getText().toString();
        Mname =ET_MNAME.getText().toString();
        Mvalue =ET_MVALUE.getText().toString();

    }
    public void userReg(View view)
    {
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        user_id = getIntent().getExtras().getString("id");
        disease = ET_DISEASE.getText().toString();
        Age = ET_AGE.getText().toString();
        Status =ET_STATUS.getText().toString();
        Aname =ET_ANAME.getText().toString();
        Ayear =ET_AYEAR.getText().toString();
        Atype =ET_ATYPE.getText().toString();
        Aproblem =ET_APROBLEM.getText().toString();
        Sname =ET_SNAME.getText().toString();
        Syear =ET_SYEAR.getText().toString();
        Stype =ET_STYPE.getText().toString();
        Sproblem =ET_SPROBLEM.getText().toString();
        Mname =ET_MNAME.getText().toString();
        Mvalue =ET_MVALUE.getText().toString();

        if(ischeck==true) {
            String method = "register";
            BackgroundTask3 backgroundTask = new BackgroundTask3(this);
            backgroundTask.execute(method, disease, Age, Status, Aname, Ayear, user_id, Atype,Aproblem,Sname,Syear,Stype,Sproblem,Mname,Mvalue);
            StartActivity();
            finish();
        }
                else {
            Toast.makeText(Add_UserMoreDetails.this, "you can't change data"  , Toast.LENGTH_SHORT).show();
        }
    }

    public void StartActivity(){

        Intent startIntent = new Intent(getApplicationContext(), Carrier_Options.class);
        startActivity(startIntent);
        Toast.makeText(this,"User successfully added ", Toast.LENGTH_SHORT).show();
        // ET_DOCTOR.setText("");

    };
    protected void showList(){ user_id = getIntent().getExtras().getString("id");
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Person> temp=new ArrayList<Person>();
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String DIS = c.getString(TAG_DISEASE);
                String AGE = c.getString(TAG_AGE);
                String ANAME = c.getString(TAG_ANAME);
                String STATUS = c.getString(TAG_STATUS);
                String AYEAR = c.getString(TAG_AYEAR);
                String UID = c.getString(TAG_UID);
                String ATYPE = c.getString(TAG_ATYPE);
                String APROBLEM = c.getString(TAG_APROBLEM);
                String SNAME = c.getString(TAG_SNAME);
                String SYEAR = c.getString(TAG_SYEAR);
                String STYPE = c.getString(TAG_STYPE);
                String SPROBLEM = c.getString(TAG_SPROBLEM);
                String MNAME = c.getString(TAG_MNAME);
                String MVALUE = c.getString(TAG_MVALUE);

                if (user_id.equalsIgnoreCase(UID) ) {

                    ischeck=false;
                    ET_DISEASE.setText(DIS);
                    ET_AGE.setText(AGE);
                    ET_STATUS.setText(STATUS);
                    ET_ANAME.setText(ANAME);
                    ET_AYEAR.setText(AYEAR);
                    ET_ATYPE.setText(ATYPE);
                    ET_APROBLEM.setText(APROBLEM);
                    ET_SNAME.setText(SNAME);
                    ET_SYEAR.setText(SYEAR);
                    ET_STYPE.setText(STYPE);
                    ET_SPROBLEM.setText(SPROBLEM);
                    ET_MNAME.setText(MNAME);
                    ET_MVALUE.setText(MVALUE);
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/get_user_more.php");

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
}
