package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SymptomCheckerOption extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symptom_checker_option);
		
		ImageButton btnSelf = (ImageButton) findViewById(R.id.btn_selfCheck);
		btnSelf.setOnClickListener(this);
		
		//ImageButton btnOther = (ImageButton) findViewById(R.id.btn_otherCheck);
		//btnOther.setOnClickListener(this);

		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_selfCheck) {
//			Vaidhya symList = (Vaidhya) this.getApplicationContext();
//			symList.clearList();
//			SessionManager1 sm = new SessionManager1(getApplicationContext());
//			if(!sm.isLoggedIn()){
				Intent intent = new Intent(this, BodyAreas.class);// it should be sign.class
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_NO_ANIMATION);
				this.startActivity(intent);
		/*	}else{
				Vaidhya ht = (Vaidhya) getApplicationContext();
				ht.setAgeGroup(sm.getAge());
				ht.setGender(sm.getGender());
				ht.setConsultantPreference(sm.getConsultantPreference());
				*//*Intent intent = new Intent(this, SymptomNavigator.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
				this.startActivity(intent);*//*
			}*/
		}/* else if (v.getId() == R.id.btn_otherCheck) {
			Intent intent = new Intent(this, SignIn.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_ANIMATION);
			this.startActivity(intent);
		}*/
		
	}
	
}
