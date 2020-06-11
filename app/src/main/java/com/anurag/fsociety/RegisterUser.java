package com.anurag.fsociety;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {
    private TextView email,pass ,name;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        name=findViewById(R.id.name);
        register=findViewById(R.id.register);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.spin_kit1);

        WanderingCubes chasingDots =new WanderingCubes();
        progressBar.setIndeterminateDrawable(chasingDots);
        progressBar.setVisibility(View.INVISIBLE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                progressBar.setVisibility(View.VISIBLE);
                final String name1=name.getText().toString();
                final String email1=email.getText().toString();
                final String Pass=pass.getText().toString();

                if(name1.isEmpty()){
                    Toast.makeText(RegisterUser.this, "Enter the UserName", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }else if(email1.isEmpty()){
                    Toast.makeText(RegisterUser.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else if(Pass.isEmpty()){
                    Toast.makeText(RegisterUser.this, "Enter the Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }else {
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(checkUsername(name1,dataSnapshot)){
                                name.setError("Username Exists.Try Another");
                                name.setFocusable(true);
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
                                firebaseAuth.createUserWithEmailAndPassword(email1, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> result) {
                                                    if (result.isSuccessful()) {
                                                        Toast.makeText(RegisterUser.this, "Verification Email has been Sent.Please Verify Your Email", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });

                                            user = firebaseAuth.getCurrentUser();

                                            String timestamp = String.valueOf(System.currentTimeMillis());
//                                            Calendar calendar = Calendar.getInstance(Locale.getDefault());
//                                            calendar.setTimeInMillis(Long.parseLong(timestamp));
//                                            final String pTime = DateFormat.format("hh:mm aa dd/MM/yyyy", calendar).toString();



                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("name", name1);
                                            hashMap.put("email", email1);
                                            hashMap.put("uId", user.getUid());
                                            hashMap.put("time",timestamp);

                                            databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                            databaseReference.child(user.getUid()).setValue(hashMap);
                                            startActivity(new Intent(".Login"));
                                            progressBar.setVisibility(View.INVISIBLE);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterUser.this, "Not Registerd Succesfully", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    public boolean checkUsername(String username, DataSnapshot dataSnapshot){
        for(DataSnapshot ds:dataSnapshot.getChildren()){
            String user=""+ds.child("name").getValue();
            Log.d("name", "checkUsername: "+user);
          if(user.equals(username)){
              return true;
          }
        }
     return  false;
    }
}
