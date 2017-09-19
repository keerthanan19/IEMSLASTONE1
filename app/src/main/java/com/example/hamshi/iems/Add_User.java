package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Add_User extends Activity {

    SessionManager session;
    private String name,user_name,user_pass,email,doctor,carer_id;
    EditText ET_NAME,ET_USER_NAME,ET_USER_PASS,ET_EMAIL,ET_DOCTOR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__user);
        Button btn_doctor = (Button)findViewById(R.id.Doctor_list);

        ET_NAME = (EditText)findViewById(R.id.name);
        ET_USER_NAME= (EditText)findViewById(R.id.new_user_name);
        ET_USER_PASS = (EditText)findViewById(R.id.new_user_pass);
        ET_EMAIL = (EditText)findViewById(R.id.email);
         ET_DOCTOR = (EditText)findViewById(R.id.Doctor);

        name=ET_NAME.getText().toString();
        user_name = ET_USER_NAME.getText().toString();
        user_pass =ET_USER_PASS.getText().toString();
        email =ET_EMAIL.getText().toString();
         doctor =ET_DOCTOR.getText().toString();

        if(List_Doctor.check==true) {
        // String pid = getIntent().getExtras().getString("id");
        //    ET_DOCTOR.setText(pid);
        //    Toast.makeText(Add_User.this, pid, Toast.LENGTH_SHORT).show();


        }
        btn_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent startIntent = new Intent(getApplicationContext(), List_Doctor.class);
                startActivity(startIntent);

            }
        });



    }
    public void userReg(View view)
    {


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        carer_id = user.get(SessionManager.KEY_ID);
        name = ET_NAME.getText().toString();
        user_name = ET_USER_NAME.getText().toString();
        user_pass =ET_USER_PASS.getText().toString();
        email =ET_EMAIL.getText().toString();
        boolean valid = isValidEmail(email);
        doctor=ET_DOCTOR.getText().toString();
        if(name.length()==0)
        {
            ET_NAME.requestFocus();
            ET_NAME.setError("FIELD CANNOT BE EMPTY");
        }
        else if(user_name.length()==0)
        {
            ET_USER_NAME.requestFocus();
            ET_USER_NAME.setError("FIELD CANNOT BE EMPTY");
        }
        else if(user_pass.length()==0)
        {
            ET_USER_PASS.requestFocus();
            ET_USER_PASS.setError("FIELD CANNOT BE EMPTY");
        }
        else if(valid == false ) {
            ET_EMAIL.requestFocus();
            ET_EMAIL.setError("TYPE VALID EMAIL ");

        }

       else if(doctor.length()==0)
        {
            ET_DOCTOR.requestFocus();
            ET_DOCTOR.setError("FIELD CANNOT BE EMPTY");
        }
        else {

            String method = "register";
            BackgroundTask backgroundTask = new BackgroundTask(this);
            backgroundTask.execute(method, name, user_name, email, user_pass, doctor, carer_id);
            StartActivity();
            finish();

        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

public void StartActivity(){

    Intent startIntent = new Intent(getApplicationContext(), Carrier_Options.class);
    startActivity(startIntent);
    Toast.makeText(Add_User.this,"User successfully added ", Toast.LENGTH_SHORT).show();
   // ET_DOCTOR.setText("");

}
    ;

}
