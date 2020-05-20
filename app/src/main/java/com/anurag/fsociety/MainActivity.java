package com.anurag.fsociety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView=findViewById(R.id.textView3);
        mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(MainActivity.this,R.raw.chime);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.loading);
        textView.startAnimation(animation);

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mediaPlayer.stop();
                startActivity(new Intent(".Login"));
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
