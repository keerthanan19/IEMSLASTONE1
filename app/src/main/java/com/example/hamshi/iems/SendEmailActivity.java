package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import java.util.HashMap;

public class SendEmailActivity extends Activity implements OnClickListener{
    SessionManager session;
    Button buttonSend;
    EditText textTo;
    EditText textSubject;
    EditText textMessage;
    String hedding;
    ArrayList<String> listtempanswer = new ArrayList<>();
    String myJSON;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> mylist;
    HashMap<String, String> persons;
    public String getemail;
    private static final String TAG_NAME = "name";
    private static final String TAG_RESULTS = "result";
    private static final String TAG_DEMAIL = "Demail";
    private static final String TAG_UID = "id";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        session = new SessionManager(getApplicationContext());
        buttonSend = (Button) findViewById(R.id.buttonSend);
        // textTo = (EditText) findViewById(R.id.editTextTo);
        //  textSubject = (EditText) findViewById(R.id.editTextSubject);
        textMessage = (EditText) findViewById(R.id.editTextMessage);

        listtempanswer = getIntent().getStringArrayListExtra("list");
        hedding="Dear sir"+ "\n" +" my Desease are";
        int j=0;String value=null; String value1=null;String dis=null; String dis1=null;
        for( j=0; j<listtempanswer.size(); j++) {
            value = (listtempanswer.get(j));

            if(value1==null){
                value1=value;
            }
            else {
                value1 = value + "\n" + value1;
            }
        }
        for( j=0; j<DeseaseListActivity.diesease.length; j++) {
            dis = (DeseaseListActivity.diesease[j]);
            Toast.makeText(SendEmailActivity.this, ""+dis, Toast.LENGTH_SHORT).show();
        if(dis!=null){
            if(dis1==null){
                dis1=dis;
            }
            else {
                dis1 = dis + "\n" + dis1;
            }
        }
        }
        textMessage.setText(hedding+"\n"+dis1+"\n"+"My questions are below"+"\n"+value1);

        buttonSend.setOnClickListener(this);
        Button btnback = (Button) findViewById(R.id.btn_backhome);
        btnback.setOnClickListener(this);
        getData();
    }
    @Override

    public void onClick(View v) {
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        // email
        String emailfrom = user.get(SessionManager.KEY_EMAIL);


        if (v.getId() == R.id.buttonSend) {
            Toast.makeText(getApplicationContext(),  getemail, Toast.LENGTH_LONG).show();

            String to = getemail;//textTo.getText().toString();
            String subject = "About diesses questions"; //textSubject.getText().toString();
            String message =textMessage.getText().toString();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
            //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);


            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        if (v.getId() == R.id.btn_backhome) {
            Intent intent = new Intent(this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity(intent);
        }

    }
    protected void showList() {
        try {
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();



            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            //user.id
            String uid = user.get(SessionManager.KEY_ID);

            for (int i = 0; i < peoples.length(); i++) {
                persons = new HashMap<String, String>();

                JSONObject c = peoples.getJSONObject(i);
                String Uid = c.getString(TAG_UID);
                String demail = c.getString(TAG_DEMAIL);

                if(uid.equals(Uid) ){
                    getemail= demail;
                }
                   /* persons.put(TAG_SYMPTOM,sym);
                    persons.put(DISEASE,dis);*/



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
                HttpPost httppost = new HttpPost("http://10.0.2.2/getdoctoremail.php");

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
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                } finally {
                    try {
                        if (inputStream != null) inputStream.close();
                    } catch (Exception squish) {
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }
}