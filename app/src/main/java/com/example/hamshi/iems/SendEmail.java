package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
import java.util.HashMap;

public class SendEmail extends Activity implements View.OnClickListener{
    SessionManager session;
    Button buttonSend;
    String myJSON;
    JSONArray peoples = null;
    private static final String TAG_RESULTS="result";
    private static final String TAG_Q = "questions";
    EditText textTo;
    EditText textSubject;
    EditText textMessage;
    String Questions1;
    String Questions2;
    String Questions3;
    String Questions4;
    String Questions5;
    private static final String TAG_NAME = "name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        session = new SessionManager(getApplicationContext());
        buttonSend = (Button) findViewById(R.id.buttonSend);


        // textTo = (EditText) findViewById(R.id.editTextTo);
        //  textSubject = (EditText) findViewById(R.id.editTextSubject);
        //  textMessage = (EditText) findViewById(R.id.editTextMessage);

        // EditText editText = (EditText)findViewById(R.id.editTextMessage);


        Questions1="1are you ok? (yes/no)";
        Questions2="2are you ok? (yes/no)";
        Questions3="3are you ok? (yes/no)";
        Questions4="4are you ok? (yes/no)";
        Questions5="5are you ok? (yes/no)";

        //    textMessage.setText(  Questions1+"\n"+Questions2+"\n"+Questions3+"\n"+Questions4+"\n"+Questions5+"\n", TextView.BufferType.EDITABLE  );
        //textMessage.setText(Questions2, TextView.BufferType.EDITABLE);

        buttonSend.setOnClickListener(this);


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
            getData();

        }
    }protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            //personList=null;

            String[] Q = new String[peoples.length()];
            String message = null;
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                Q[i] = c.getString(TAG_Q);
                Toast.makeText(SendEmail.this,Q[i], Toast.LENGTH_SHORT).show();
                 message = message + Q[i];

            }//Toast.makeText(getApplicationContext(), name + emailfrom, Toast.LENGTH_LONG).show();
            String to = "s.chanuchan@gmail.com";//textTo.getText().toString();
            String subject = "About diesses questions"; //textSubject.getText().toString();
            //  String message = textMessage.getText().toString();



            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
            //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
            //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, message);



            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void getData() {

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/getquestion.php");

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