package com.anurag.fsociety;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveUser extends AppCompatActivity {
    private ActiveUsersAdapter activeUsersAdapter;
    private RecyclerView recyclerView;
    private List<User> users;
    private User user;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private String myname;
    private FirebaseUser u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_user);
        recyclerView=findViewById(R.id.active_user);
        mAuth=FirebaseAuth.getInstance();
        u=mAuth.getCurrentUser();

        users=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Users");
        ref1.orderByChild("email").equalTo(u.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    myname=""+ds.child("name").getValue();
                    Log.d("myemail", "onDataChange: "+myname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         reference= FirebaseDatabase.getInstance().getReference("Users");
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if(dataSnapshot.exists()){
                     users.clear();
                     for(DataSnapshot ds:dataSnapshot.getChildren()){
                         String name=""+ds.child("name").getValue();
                         String email=""+ds.child("email").getValue();
                         String uid=""+ds.child("uId").getValue();
                         String status=""+ds.child("status").getValue();
                         String typing=""+ds.child("typing").getValue();
                         user=new User(email,name,uid,status,typing);
                         Log.d("active", "onDataChange: "+name+status);

                             if(user.getName().equals(myname)){


                             }else{
                                 users.add(user);
                                 activeUsersAdapter=new ActiveUsersAdapter(ActiveUser.this,users);
                                 recyclerView.setAdapter(activeUsersAdapter);

                             }

                     }
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
//                 Toast.makeText(ActiveUser.this, +databaseError.toString(), Toast.LENGTH_SHORT).show();

             }
         });

    }

    @Override
    protected void onResume() {
        Log.d("test", "onResume: ");
        super.onResume();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String,Object> h=new HashMap<>();
        h.put("status","online");
        ref.child(u.getUid()).updateChildren(h);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("test", "onStop: ");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String,Object> h=new HashMap<>();
        h.put("status","offline");
        ref.child(u.getUid()).updateChildren(h);
    }


}
