package com.example.hamshi.iems;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class ConsultantList extends Activity{
	SessionManager session;
	private Button Generate;
	ArrayAdapter<String> adapter = null;
	JSONArray peoples = null;
	JSONArray doctors = null;
	ArrayList<String> doctorList;
	final ArrayList<String> list = new ArrayList<String>();
	ArrayList<HashMap<String, String>> mylist;
	Vector<String> vId = new Vector<>();
	Vector<String> vName = new Vector<>();
	ListAdapter adapter_title;
	HashMap<String, String> persons;
	Handler handler = new Handler();
	ListView listView1;
	String myJSON;

	public static  String doctorId=null;
	public static boolean check = false;
	private static final String TAG_RESULTS = "result";
	private static final String TAG_ID = "id";
	private static final String TAG_Dname = "Dname";
	private static final String TAG_Dpassword = "Dpassword";
	private static final String Demail = "Demail";

	private static final String TAG_Uname = "name";
	private static final String TAG_Upassword ="user_pass";
	private static final String Uemail ="email";
	private static final String Did ="Did";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consultant_list);

		listView1 = (ListView) findViewById(R.id.consultantList);
		doctorList = new ArrayList<String>();
		getDid();

		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), ChatScreen.class);
				intent.putExtra("id", vId.elementAt(position));
				intent.putExtra("name", vName.elementAt(position));
				//intent.putExtra("id", (parent.getItemIdAtPosition(position)));
				//intent.putExtra("name",(String) parent.getItemAtPosition(position));
				startActivity(intent);
			}
		});
	}

	protected void showList(){ String pid = Doctor_login.Did;;
		try {
			session = new SessionManager(getApplicationContext());
			HashMap<String, String> user = session.getUserDetails();

			String uid = user.get(SessionManager.KEY_ID);

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

				if (id.equalsIgnoreCase(uid) ) {
					doctorId=D_id;
				}
			}
			loadData();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void getDid(){
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

	public void loadData() {

		final ProgressDialog progressDialog = ProgressDialog.show(this,
				"Retriving Results", "Please Wait..", true);
		progressDialog.setCancelable(true);

		new Thread(new Runnable() {
			public void run() {

				try {



					//httpget = new HttpGet(IPSettings.getBaseUrl()+"getchatmsgs.php?id1="+user_id+"&id2="+id+"&ts="+lastTs+"");
					HttpGet httpget = new HttpGet(IPSettings.getBaseUrl()+"getalldoctors.php");
					//httpget=new HttpGet("http://d4academy.com/yontech/mad/getdata.php");
					HttpClient client = new DefaultHttpClient();
					StringBuilder stringBuilder = new StringBuilder();
					HttpResponse response = client.execute(httpget);
					InputStream stream = response.getEntity().getContent();
					int ch;
					while ((ch = stream.read()) != -1) {
						stringBuilder.append((char) ch);
					}
					String str = stringBuilder.toString();
					Log.d("test", str);

					JSONArray jArray1 = new JSONArray(str);

					for (int i = 0; i < jArray1.length(); i++) {
						JSONObject jsonObject = jArray1.getJSONObject(i);
						if (jsonObject.getString("id").equals(doctorId)){
							vId.add(jsonObject.getString("id"));
							vName.add(jsonObject.getString("Dname"));
						}
					}

				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub

						progressDialog.dismiss();

						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, vName);

						listView1.setAdapter(adapter);
					}
				});
			}
		}).start();
	}
}

