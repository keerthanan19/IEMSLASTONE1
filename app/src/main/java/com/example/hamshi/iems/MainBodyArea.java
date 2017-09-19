package com.example.hamshi.iems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

//import com.example.hamshi.iems.WebService.ExpandableListAdapter;
//import com.example.hamshi.iems.WebService.MainBodyAreas;
//import com.example.hamshi.iems.WebService.RestAPI;
//import com.example.hamshi.iems.WebService.SubBodyAreas;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;


public class MainBodyArea extends AppCompatActivity {

	// List view
	private ListView lv;

	// Listview Adapter
	ArrayAdapter<String> adapter;

	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_body_areas);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);


		// Listview Data
		String body[] = {"A","B","C","D"};

		ListView lv = (ListView) findViewById(R.id.list);


		adapter = new ArrayAdapter<String>(this, R.layout.list_item1,R.id.Body_name, body);
		lv.setAdapter(adapter);





	}
/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_body_areas, container, false);
		getActivity().setTitle("Body Areas");
		expListView = (ExpandableListView) view.findViewById(R.id.mainBodyExpandable);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				int subId = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getID();
				String subBodyName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getName();
				communicator.respond(subId, subBodyName);
				return false;
			}
		});

		if (!isLoaded) {
			listDataHeader = new ArrayList<MainBodyAreas>();
			listDataChild = new HashMap<MainBodyAreas, List<SubBodyAreas>>();
			new AsyncLoadMainDetails().execute();

			isLoaded = true;

		}
		listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader,	listDataChild);

		expListView.setAdapter(listAdapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Communicator) {
			communicator = (Communicator) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implemenet MainBodyArea.Communicator");
		}
	}

	public interface Communicator {
		public void respond(int value, String name);
	}

	protected class AsyncLoadMainDetails extends
			AsyncTask<Void, JSONObject, ArrayList<MainBodyAreas>> {
		ArrayList<MainBodyAreas> mainBodyAreas = null;
		ProgressDialog mDialog;

		@Override
		protected ArrayList<MainBodyAreas> doInBackground(Void... params) {
			// TODO Auto-generated method stub

			RestAPI api = new RestAPI();
			try {

				JSONObject jsonObj = api.getMainBodyAreas();

				JSONParser parser = new JSONParser();

				mainBodyAreas = parser.parseMainBodyAreas(jsonObj);

				for (int i = 0; i < mainBodyAreas.size(); i++) {
					JSONObject jsonObj1 = api.getSubBodyAreas(mainBodyAreas.get(i).getID());

					JSONParser parser1 = new JSONParser();

					ArrayList<SubBodyAreas> temp = parser1.parseSubBodyAreas(jsonObj1);
					mainBodyAreas.get(i).setSubBodyItems(temp);

				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("AsyncLoadMainDetails", e.getMessage());

			}

			return mainBodyAreas;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			mDialog = new ProgressDialog(getActivity());
			mDialog.setTitle("Please Wait...");
			mDialog.setMessage("Loading lists from database");
			mDialog.setCancelable(false);
			mDialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<MainBodyAreas> result) {
			// TODO Auto-generated method stub

			if (result != null) {
				for (MainBodyAreas temp : result) {
					listDataHeader.add(temp);
					listDataChild.put(temp, temp.getSubBodyItems());
				}
				mDialog.dismiss();
				listAdapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "Loading Completed",
						Toast.LENGTH_SHORT).show();
			} else {
				mDialog.dismiss();
				Toast.makeText(getActivity(), "Could not make connection",
						Toast.LENGTH_SHORT).show();
				getActivity().finish();
			}

		}

	}*/

}
