package com.anurag.fsociety;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextView email,pass;
    private Button login,signup;
    private FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        email=findViewById(R.id.email1);
        pass=findViewById(R.id.password1);
        progressBar=findViewById(R.id.spin_kit);

        WanderingCubes wanderingCubes=new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);

        login=findViewById(R.id.login);
        signup=findViewById(R.id.button2);
        firebaseAuth=FirebaseAuth.getInstance();
        mediaPlayer=new MediaPlayer();
        mediaPlayer=MediaPlayer.create(Login.this,R.raw.button2);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playmusic();
                startActivity(new Intent(Login.this,RegisterUser.class));
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                progressBar.setVisibility(View.VISIBLE);
                playmusic();
                final String email1=email.getText().toString();
                String Pass=pass.getText().toString();

                if(email1.isEmpty()){
                    Toast.makeText(Login.this, "Plase Enter Your Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }else if(Pass.isEmpty()){
                    Toast.makeText(Login.this, "Password Please", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }else {

                    firebaseAuth.signInWithEmailAndPassword(email1, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    email.setText("");
                                    pass.setText("");
                                    startActivity(new Intent(Login.this,ChatActivity.class));
                                    finish();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }else{
                                    Toast.makeText(Login.this, "Please Verify your email.", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }else{
                                Toast.makeText(Login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });


    }

    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    public void playmusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
        }
    }
}
