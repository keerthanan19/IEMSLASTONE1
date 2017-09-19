package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.Vector;

/**
 * Created by hamshi on 19-07-2016.
 */
public class PatientList extends Activity {
    private Button Generate;

    ArrayAdapter<String> adapter =null;
    //  final ArrayList<String> temp = new ArrayList<String>();
    JSONArray peoples = null;
    ArrayList<String> personList;
    final ArrayList<String> list = new ArrayList<String>();
    ListView list1;
    String myJSON;
  //  public static boolean check =false;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_Uname = "name";
    private static final String TAG_Upassword ="user_pass";
    private static final String Uemail ="email";
    private static final String Did ="Did";

    Vector<String> vId = new Vector<>();
    Vector<String> vName = new Vector<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list);

        list1 = (ListView) findViewById(R.id.patient_listView);

        personList = new ArrayList<String>();

        // getIntent().getStringExtra("id");

        Log.d("doctor id", Doctor_login.Did);


        getData();
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* Intent startIntent = new Intent(getApplicationContext(), Patient.class);

                startIntent.putExtra("id",adapter.getItem(position));
                Toast.makeText(PatientList.this, "Type your Doctor ID", Toast.LENGTH_SHORT).show();
                startActivity(startIntent);*/
               // check=true;
                Toast.makeText(PatientList.this, ""+vId.elementAt(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ChatScreen.class);
                intent.putExtra("id", vId.elementAt(position));
                intent.putExtra("name", vName.elementAt(position));
                startActivity(intent);


            }
        });
        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int pos, long id) {

                Intent intent = new Intent(getApplicationContext(), Add_UserMoreDetails_doctor.class);
                intent.putExtra("id", vId.elementAt(pos));

                startActivity(intent);
                Toast.makeText(PatientList.this, ""+ vId.elementAt(pos), Toast.LENGTH_SHORT).show();


                return true;
            }
        });

    }
    protected void showList(){ String pid = Doctor_login.Did;;
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            ArrayList<Person> temp=new ArrayList<Person>();
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_Uname);
                String sym = c.getString(TAG_Upassword);
                String dis = c.getString(Uemail);
                String D_id = c.getString(Did);

                if (pid.equalsIgnoreCase(D_id) ) {
                      personList.add(name);
                     vId.add(id);
                      vName.add((name));
                 }
            }

            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, personList);

            list1.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/listuser.php");

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

