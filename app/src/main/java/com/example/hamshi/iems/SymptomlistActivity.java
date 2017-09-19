package com.example.hamshi.iems;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SymptomlistActivity extends Activity implements View.OnClickListener{
    private Button Generate;

    ArrayAdapter<String> adapter =null;
    //  final ArrayList<String> temp = new ArrayList<String>();
    JSONArray peoples = null;
    ArrayList<String> personList;
    final ArrayList<String> list = new ArrayList<String>();

    ListView list1;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_SUBBODY = "subbody";
    private static final String TAG_SYMPTOM ="symptom";
    private static final String DISEASE ="disease";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptomlist);
        Generate = (Button) findViewById(R.id.Generate);
        Generate.setOnClickListener(this);
        list1 = (ListView) findViewById(R.id.listView);


        //  Toast.makeText(SymptomlistActivity.this, pid, Toast.LENGTH_SHORT).show();

        personList = new ArrayList<String>();
        getData();

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.iemstoast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Please select the Symptoms...");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                //    String item = ((TextView)view).getText().toString();
                if(list.size()==0){
                    view.setBackgroundColor(getResources().getColor(R.color.red) );
                    list.add(adapter.getItem(position));
                }
                else   {
                    String value=null;
                    int i=0,j=0;
                    for( i=0; i<list.size(); i++){
                        if (list.get(i).equalsIgnoreCase(adapter.getItem(position))) {
                            j=1;

                            value=(list.get(i));
                        }
                    }
                    if(j==0){
                        view.setBackgroundColor(getResources().getColor(R.color.red));
                        list.add(adapter.getItem(position));


                    }
                    else if(j==1){
                        view.setBackgroundColor(getResources().getColor(R.color.skyblue));
                        list.remove(value);

                    }
                }
            }
        });

    }
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            String pid = getIntent().getExtras().getString("sub_id");
            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String sub = c.getString(TAG_SUBBODY);
                String sym = c.getString(TAG_SYMPTOM);
                String dis = c.getString(DISEASE);

                if (sub.equalsIgnoreCase(pid)){

                    /*persons.put(TAG_ID,id);
                    persons.put(TAG_SUBBODY,sub);
                    persons.put(TAG_SYMPTOM,sym);
                    persons.put(DISEASE,dis);
                    */
                    personList.add(sym);
                }
            }

            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, personList);

            list1.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void onClick(View v)
    {String pid = getIntent().getExtras().getString("sub_id");
        if (v == Generate)
        {
            Intent startIntent = new Intent(getApplicationContext(), DeseaseListActivity.class);
            startIntent.putStringArrayListExtra("list",list);
            startIntent.putExtra("sub_id",pid);
            startActivity(startIntent);
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://192.168.1.9/symptom.php");

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
