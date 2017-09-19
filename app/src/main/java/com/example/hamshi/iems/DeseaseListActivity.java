package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class DeseaseListActivity extends Activity implements View.OnClickListener {
	String myJSON;
	SessionManager session;
	private static final String TAG_RESULTS="result";
	private static final String  TAG_ID = "id";
	private static final String TAG_SUBBODY = "subbody";
	private static final String TAG_SYMPTOM ="symptom";
	private static final String DISEASE ="disease";
	private Button viwe_consultant;
	private Button question;
	private ListView lv;
	public static int d=0;
	//ArrayAdapter<String> adapter;
	ArrayList<String> listtemp = new ArrayList<>();
	JSONArray peoples = null;
	ListView list;
	public  static String[] diesease =new String[10];
	private String user_id,d_id,email;
	ArrayList<HashMap<String, String>> personList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_desease);
		viwe_consultant = (Button) findViewById(R.id.viwe_consultant);
		question = (Button) findViewById(R.id.question);


		//Toast.makeText(DeseaseListActivity.this, uid, Toast.LENGTH_SHORT).show();


		viwe_consultant.setOnClickListener(this);
		question.setOnClickListener(this);
		list = (ListView) findViewById(R.id.list);
		personList = new ArrayList<HashMap<String,String>>();
		getData();


	}
	protected void showList(){
		try {
			JSONObject jsonObj = new JSONObject(myJSON);
			peoples = jsonObj.getJSONArray(TAG_RESULTS);
			//personList=null;
			listtemp = getIntent().getStringArrayListExtra("list");

			String pid = getIntent().getExtras().getString("sub_id");
			//Toast.makeText(DeseaseListActivity.this, pid, Toast.LENGTH_SHORT).show();

			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.iemstoast,
					(ViewGroup) findViewById(R.id.custom_toast_container));

			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText("Click Question: mail body condition\n Click View Consultant: Chat with Doctor ");

			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();


			session = new SessionManager(getApplicationContext());
			HashMap<String, String> user = session.getUserDetails();
			String uid = user.get(SessionManager.KEY_ID);
			for(int i=0;i<peoples.length();i++){
				JSONObject c = peoples.getJSONObject(i);
				String id = c.getString(TAG_ID);
				String sub = c.getString(TAG_SUBBODY);
				String sym = c.getString(TAG_SYMPTOM);
				String dis = c.getString(DISEASE);
				int j=0;String value=null;


				for( j=0; j<listtemp.size(); j++){
					value=(listtemp.get(j));

					//	Toast.makeText(DeseaseListActivity.this, id, Toast.LENGTH_SHORT).show();

					//int d=0;
					HashMap<String,String> persons = new HashMap<String,String>();
					if (sym.equalsIgnoreCase(value)&& sub.equalsIgnoreCase(pid) ){
						//	Toast.makeText(DeseaseListActivity.this, id, Toast.LENGTH_SHORT).show();

						//	Toast.makeText(DeseaseListActivity.this, uid, Toast.LENGTH_SHORT).show();
						String method = "report";
						BackgroundTask2 backgroundTask2 = new BackgroundTask2(this);
						backgroundTask2.execute(method,uid,id);

						persons.put(TAG_ID,id);
						persons.put(TAG_SUBBODY,sub);
						persons.put(TAG_SYMPTOM,sym);
						persons.put(DISEASE,dis);

						personList.add(persons);

						diesease[d]=dis;

						Toast.makeText(DeseaseListActivity.this, ""+diesease[d], Toast.LENGTH_SHORT).show();
						d++;
					}
				}
			}

			ListAdapter adapter = new SimpleAdapter(
					DeseaseListActivity.this, personList, R.layout.list_row,
					new String[]{DISEASE} ,
					new int[]{ R.id.name}
			);

			list.setAdapter(adapter);


		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.viwe_consultant) {
			Intent intent = new Intent(this, ConsultantList.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.startActivity(intent);
			//Toast.makeText(DeseaseListActivity.this,TAG_ID,Toast.LENGTH_LONG).show();
		}
		else if (v.getId() == R.id.question) {
			Intent intent = new Intent(this, Question.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.startActivity(intent);
			//Toast.makeText(DeseaseListActivity.this,"dfg",Toast.LENGTH_LONG).show();
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
