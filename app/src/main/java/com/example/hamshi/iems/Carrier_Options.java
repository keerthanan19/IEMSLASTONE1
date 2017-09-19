package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Carrier_Options extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier__account);

        Button btn_add_user = (Button)findViewById(R.id.btn_add_user);
        Button btn_report = (Button)findViewById(R.id.btn_report);

        Button btn_add_doctor = (Button)findViewById(R.id.btn_add_doctor);
        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startIntent = new Intent(getApplicationContext(), Add_User.class);
                startActivity(startIntent);
            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startIntent = new Intent(getApplicationContext(), Carrier_See_Report.class);
                startActivity(startIntent);
            }
        });
        btn_add_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startIntent = new Intent(getApplicationContext(), PatientListAddDetails.class);
                startActivity(startIntent);

            }
        });

    }
}
