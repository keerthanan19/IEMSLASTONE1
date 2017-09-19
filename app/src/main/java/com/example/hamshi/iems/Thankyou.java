package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Thankyou extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        Button btnback = (Button) findViewById(R.id.btn_backhome);
        btnback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_backhome) {
            Intent intent = new Intent(this, SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.startActivity(intent);
        }
    }
}
