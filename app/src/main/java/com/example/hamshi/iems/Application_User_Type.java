package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Application_User_Type extends Activity {

    public  static String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application__user__type);

        Button btn_doctor = (Button)findViewById(R.id.btn_doctor);
        Button btn_patient = (Button)findViewById(R.id.btn_patient);
        Button btn_carrier = (Button)findViewById(R.id.btn_carrier);

        btn_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "doctor";
                Intent startIntent = new Intent(getApplicationContext(), Doctor_login.class);
                startActivity(startIntent);
            }
        });

        btn_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "doctor";
                Intent startIntent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(startIntent);
            }
        });


        btn_carrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userType = "carrier";
                Intent startIntent = new Intent(getApplicationContext(), Carrier_login.class);
                startActivity(startIntent);
            }
        });

    }
}
