package com.jongzazaal.alertme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jongzazaal on 16/12/2559.
 */

public class AlarmMeActivity extends Activity {
    Vibrator v;
    private Button stop;
    private TextView placeName;
    private boolean isRunAnime = true;
    private MediaPlayer mp;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);

        Animation animeButton = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        animeButton.setInterpolator(interpolator);
        animeButton.setRepeatMode(Animation.INFINITE);
        animeButton.setRepeatCount(Animation.INFINITE);

        i = getIntent();

        placeName = (TextView) findViewById(R.id.placeName);
        placeName.setText("You are near "+i.getStringExtra("placeName"));

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

        // The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
        v.vibrate(pattern, 0);

        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.setLooping(true);
        mp.start();
        stop = (Button) findViewById(R.id.stop);
        stop.startAnimation(animeButton);

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.cancel();
                mp.stop();
                SwitchService.getInstace().removeServiceName(i.getStringExtra("placeName"));
                isRunAnime=false;
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        v.cancel();
        mp.stop();
        SwitchService.getInstace().removeServiceName(i.getStringExtra("placeName"));
        isRunAnime=false;
        finish();
    }
}
