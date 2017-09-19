package com.example.hamshi.iems;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hamshi on 23-07-2016.
 */
public class Patient  extends Activity {
    EditText chat;
    TextView name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient);
        chat = (EditText)findViewById(R.id.editText);
        name = (TextView) findViewById(R.id.name);

        String pid = getIntent().getExtras().getString("id");
            name.setText(pid);
        chat.setText("hai");


           Toast.makeText(Patient.this, pid, Toast.LENGTH_SHORT).show();


    }
}
