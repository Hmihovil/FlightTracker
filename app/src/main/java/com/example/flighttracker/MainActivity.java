package com.example.flighttracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private ProgressBar cpb1;
    private TextView website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //hides the title bar / actionbar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        logo = (ImageView)findViewById(R.id.dee_logo);
        cpb1 = (ProgressBar)findViewById(R.id.cpb1);
        website = (TextView)findViewById(R.id.text_website);

        //logo, websiteURL and progressbar animation with fade in and out-------------------------------
        cpb1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_progress1));
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_logo));
        website.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_website));

        //Handler for Transition out and Timer to go to the next activity--------------------------
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                cpb1.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_transition_out));
                logo.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_transition_out));
                website.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_transition_out));

                cpb1.setVisibility(View.INVISIBLE);
                logo.setVisibility(View.INVISIBLE);
                website.setVisibility(View.INVISIBLE);
            }
        }, 8000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, FlightMain.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }, 10000);
    }
}