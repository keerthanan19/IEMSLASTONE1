package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import com.theopentutorials.expandablelist.adapters.ExpandableListAdapterNew;

public class BodyAreas extends Activity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_main);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) findViewById(R.id.laptop_list);
        final ExpandableListAdapterNew expListAdapter = new ExpandableListAdapterNew(
                this, groupList, laptopCollection);
        expListView.setAdapter(expListAdapter);

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);

                activity(selected);

                return true;
            }

        });

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.iemstoast,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Please select the Body area..");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }
    private void  activity(String selected){
        Intent intent = new Intent(this, SymptomlistActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("sub_id",selected);
       // Toast.makeText(BodyAreas.this, selected, Toast.LENGTH_SHORT).show();
        this.startActivity(intent);

    }
    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Arm");
        groupList.add("Chest");
        groupList.add("Head");
        groupList.add("Abdomen");
        groupList.add("Neck");

    }

    private void createCollection() {
        // preparing laptops collection(child)
        String[] ArmModels = { "Shoulder", "Upper Arm", "Armpit", "Elbow","Forearm(Flexor)", "Wrist", "Palm", "Finger"};
        String[] ChestModels = { "Chest", "Lateral Chest" };
        String[] HeadModels = { "Scalp", "Eye", "Nose","Ear","Face","Mouth","Jaw" };
        String[] Abdomen = { "Upper", "Lower Abdomen" };
        String[] NeckModels = { "Back Neck", "Neck front" };



        laptopCollection = new LinkedHashMap<String, List<String>>();

        for (String laptop : groupList) {
            if (laptop.equals("Arm")) {
                loadChild(ArmModels);
            } else if (laptop.equals("Chest"))
                loadChild(ChestModels);
            else if (laptop.equals("Head"))
                loadChild(HeadModels);
            else if (laptop.equals("Abdomen"))
                loadChild(Abdomen);
            else if (laptop.equals("Neck"))
                loadChild(NeckModels);


            laptopCollection.put(laptop, childList);
        }
    }


    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }

    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


}
