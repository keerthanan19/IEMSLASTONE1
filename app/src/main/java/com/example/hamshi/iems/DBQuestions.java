package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

public class DBQuestions extends Activity implements View.OnClickListener {
    private Button send;
    ArrayAdapter<String> adapter =null;
    //  final ArrayList<String> temp = new ArrayList<String>();
    JSONArray peoples = null;
    ArrayList<String> questionList;
    final ArrayList<String> list = new ArrayList<String>();

    ListView list1;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_SUBBODY = "subbody";
    private static final String TAG_SYMPTOM ="symptom";
    private static final String DISEASE ="disease";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbquestions);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(this);
        list1 = (ListView) findViewById(R.id.abc);


        //  Toast.makeText(SymptomlistActivity.this, pid, Toast.LENGTH_SHORT).show();

        questionList = new ArrayList<String>();
        getData();
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
                    questionList.add(sym);
                }
            }

            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, questionList);

            list1.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onClick(View v)
    {String pid = getIntent().getExtras().getString("sub_id");
        if (v == send)
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
