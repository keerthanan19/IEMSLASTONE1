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

public class List_Doctor extends Activity {
    private Button Generate;

    ArrayAdapter<String> adapter = null;
    //  final ArrayList<String> temp = new ArrayList<String>();
    JSONArray peoples = null;
    ArrayList<String> personList;
    final ArrayList<String> list = new ArrayList<String>();
    ArrayList<HashMap<String, String>> mylist;
    ListAdapter adapter_title;
    HashMap<String, String> persons;

    ListView list1;
    String myJSON;
    public static boolean check = false;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_Dname = "Dname";
    private static final String TAG_Dpassword = "Dpassword";
    private static final String Demail = "Demail";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__doctor);

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
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Person> temp = new ArrayList<Person>();
            mylist = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < peoples.length(); i++) {
                persons = new HashMap<String, String>();

                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String doct = c.getString(TAG_Dname);
                String sym = c.getString(TAG_Dpassword);
                String dis = c.getString(Demail);


                persons.put(TAG_ID, id);
                persons.put(TAG_Dname, doct);
                   /* persons.put(TAG_SYMPTOM,sym);
                    persons.put(DISEASE,dis);*/

                mylist.add(persons);

            }
            adapter_title = new SimpleAdapter(this, mylist, R.layout.mylist,
                    new String[]{TAG_ID, TAG_Dname}, new int[]{
                    R.id.one, R.id.two});

            list1.setAdapter(adapter_title);

            //       list1.setAdapter(new ArrayAdapter<String>(this, R.layout.mylist,R.id.Itemname,personList));

            //  adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, personList);

            //  list1.setAdapter(adapter);

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

