package com.anurag.fsociety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Email extends AppCompatActivity {
    private Button button;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


//        button=findViewById(R.id.button);
        mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(Email.this,R.raw.button2);

        Thread thread =new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1500);
                    startActivity(new Intent(Email.this, ChatActivity.class));
                    finish();
                }catch (Exception e){
                }
            }
        };
        thread.start();

    }
}
