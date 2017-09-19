package com.example.hamshi.iems;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashScreen extends Activity {
	
	
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

	private static int SPLASH_TIME_OUT = 4500;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		startAnimation();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(SplashScreen.this, Application_User_Type.class);
				startActivity(i);
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

	public void startAnimation() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		anim.reset();
		RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.translate);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.clearAnimation();
		iv.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
		anim.reset();
		ImageView text = (ImageView) findViewById(R.id.logoText);
		text.clearAnimation();
		text.startAnimation(anim);

	}
}
