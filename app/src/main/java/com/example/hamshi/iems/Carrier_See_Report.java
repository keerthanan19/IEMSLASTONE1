package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

public class Carrier_See_Report extends Activity {
    private Button Generate;

    ArrayAdapter<String> adapter = null;
    //  final ArrayList<String> temp = new ArrayList<String>();
    JSONArray peoples = null;
    ArrayList<String> personList;
    final ArrayList<String> list = new ArrayList<String>();
    ArrayList<HashMap<String, String>> mylist;
    ListAdapter adapter_title;
    HashMap<String, String> persons;

    SessionManager session;
    ListView list1;
    String myJSON;
    public static boolean check = false;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_SUB = "subbody";
    private static final String TAG_SYM = "symptom";
    private static final String TAG_DIS = "disease";
    private static final String TAG_DNAME = "Dname";
    private static final String TAG_CID = "Cid";
    private String carer_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier__report);

        list1 = (ListView) findViewById(R.id.Doctor_listView);
        // Toast.makeText(List_Doctor.this, name, Toast.LENGTH_SHORT).show();
        personList = new ArrayList<String>();
        getData();
       /* list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                Intent startIntent = new Intent(getApplicationContext(), Add_User.class);

              //  startIntent.putExtra("id",adapter.getItem(position));
                Toast.makeText(List_Doctor.this, "Type your Doctor ID", Toast.LENGTH_SHORT).show();
                startActivity(startIntent);
               // check=true;


            }
        });*/

    }

    protected void showList() {
        try {
            session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getUserDetails();

            carer_id = user.get(SessionManager.KEY_ID);

            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Person> temp = new ArrayList<Person>();
            mylist = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < peoples.length(); i++) {
                persons = new HashMap<String, String>();

                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String sub = c.getString(TAG_SUB);
                String sym = c.getString(TAG_SYM);
                String dis = c.getString(TAG_DIS);
                String dname = c.getString(TAG_DNAME);
                String Cid = c.getString(TAG_CID);

                if(carer_id.equals(Cid) ){
                    persons = new HashMap<String, String>();
                persons.put(TAG_NAME, name);
                persons.put(TAG_SUB, sub);
                persons.put(TAG_SYM, sym);
                persons.put(TAG_DIS, dis);
                persons.put(TAG_DNAME, dname);

                    mylist.add(persons);
                }


            }
            adapter_title = new SimpleAdapter(this, mylist, R.layout.report_userlist,
                    new String[]{TAG_NAME, TAG_SUB,TAG_SYM,TAG_DIS,TAG_DNAME}, new int[]{
                    R.id.name, R.id.subbody,R.id.symptom, R.id.disease, R.id.dname});

            list1.setAdapter(adapter_title);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData() {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/get_user_report.php");

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

