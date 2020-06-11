package com.anurag.fsociety;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private EditText editText;
    private ImageButton imageButton;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private Boolean backgroundColor;
    private TextView textView;
    private AdapeterHelp adapeterHelp ;
    private String name;
    private int color,Color1;
    private long RegisterTime;
    private ConstraintLayout constraintLayout;
    private RecyclerView mMessageRecycler;
    private AdapterMessage adapterMessage;
    private List<Messages> messages;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1D78svI:APA91bE1p_x0VjsKnPfPSbcL27iHyTvMDmGaqhDpcTtZ9hruWzdJUHgozXlz9Wu3HvPuU9PfmCsAFSz_vrXCuKrSrxDXbZ3d_MulKJ7DsKEfDTchsLt_dfAZPNU2ikN0Ovxjnd2Oc7DN";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    final double VERSION=1.3;
    private double version;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        editText = findViewById(R.id.edittext_chatbox);
        imageButton = findViewById(R.id.button_chatbox_send);
        constraintLayout=findViewById(R.id.constraint);
//        progressBar=findViewById(R.id.spin_kit3);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        checkUser();




        FirebaseMessaging.getInstance().subscribeToTopic("/topics/notification");


//        Pulse wanderingCubes=new Pulse();
//        progressBar.setIndeterminateDrawable(wanderingCubes);
//        progressBar.setVisibility(View.VISIBLE);
//        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Version");
//        ref1.child("version").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds:dataSnapshot.getChildren()) {
//                     version=Double.parseDouble(ds.child("version").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        Log.d("time", "vers: "+version);



//        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
//        ref.orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               for(DataSnapshot ds:dataSnapshot.getChildren()){
//                   if(ds.child("bgColor").exists()){
//                      int Color1= Integer.valueOf(ds.child("bgColor").getValue().toString());
//                      if(Color1==-1){
//                          editText.setTextColor(getResources().getColor(R.color.black));
//                          editText.setHintTextColor(getResources().getColor(R.color.black));
//                      }
//                       constraintLayout.setBackgroundColor(Color1);
//                   }
//               }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });





            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().trim().length()==0){
                            checkTypingStatus("none");
                        }else{
                            checkTypingStatus("typing....");
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });




            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        name= "" + ds.child("name").getValue();
                        RegisterTime= Long.parseLong(ds.child("time").getValue().toString());
                        Log.d("time", "register: "+RegisterTime+name);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

           Log.d("time", "register: "+RegisterTime+name);

            mMessageRecycler = findViewById(R.id.reyclerview_message_list);
            LinearLayoutManager layoutManager =new LinearLayoutManager(this);
            mMessageRecycler.setLayoutManager(layoutManager);







            messages = new ArrayList<>();

            loadMessages();
         DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
         HashMap<String,Object> h=new HashMap<>();
        h.put("status","offline");
        h.put("typing","none");
        ref.child(user.getUid()).onDisconnect().updateChildren(h);
            mMessageRecycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ( bottom < oldBottom) {
                    mMessageRecycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMessageRecycler.scrollToPosition(messages.size()-1);
                        }
                    }, 100);
                }
            }
        });






            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage(v);

                }
            });
            imageButton.setVisibility(View.VISIBLE);
            mMessageRecycler.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return false;
                }
            });

    }


    public void hideKeyBoard(View view) {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    private void loadMessages() {
//        progressBar.setVisibility(View.VISIBLE);
         databaseReference = FirebaseDatabase.getInstance().getReference("message");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                       long messagetime=Long.parseLong(ds.child("timestamp").getValue().toString());
                        Log.d("time", "onDataChange: "+messagetime);
                        Log.d("time", "registerr: "+RegisterTime);
                        if(RegisterTime<=messagetime){
                            Messages messages1 = ds.getValue(Messages.class);
                            messages.add(messages1);
                            Log.d("bool", "onDataChange: true");
                            adapterMessage = new AdapterMessage(getBaseContext(), messages);
                            mMessageRecycler.setAdapter(adapterMessage);
                            mMessageRecycler.scrollToPosition(messages.size() - 1);
                        }

                    }
                }else{
                    Toast.makeText(ChatActivity.this, "No Messgaes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(View v) {
        final String messsage = editText.getText().toString();
        if (messsage.isEmpty()) {
            Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show();
        } else if (messsage.equals("sudo exit")) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/notification");
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
            HashMap<String,Object> h=new HashMap<>();
            h.put("status","offline");
            ref.child(user.getUid()).updateChildren(h);
            checkTypingStatus("none");
            firebaseAuth.signOut();
            checkUser();
        }else if(messsage.equals("sudo active-users")) {
            hideKeyBoard(v);
            startActivity(new Intent(ChatActivity.this, ActiveUser.class));
            editText.setText("");
        }


//        }else if(messsage.equals("sudo backgroundColor:red")||messsage.equals("sudo backgroundColor:white")){
//            if(messsage.equals("sudo backgroundColor:red")){
//            constraintLayout.setBackgroundColor(getResources().getColor(R.color.Red));
//            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
//            HashMap<String,Object> h=new HashMap<>();
//            color=getResources().getColor(R.color.Red);
//            h.put("bgColor",color);
//            ref.child(user.getUid()).updateChildren(h);
//            }
//            else if(messsage.equals("sudo backgroundColor:white")){
//                constraintLayout.setBackgroundColor(getResources().getColor(R.color.White));
//                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
//                HashMap<String,Object> h=new HashMap<>();
//                color=getResources().getColor(R.color.White);
//                h.put("bgColor",color);
//                ref.child(user.getUid()).updateChildren(h);
//            }
//            }

        else {
            imageButton.setEnabled(true);
            String timestamp = String.valueOf(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(timestamp));
            final String pTime = DateFormat.format("hh:mm aa dd/MM/yyyy", calendar).toString();




            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("message", messsage);
            hashMap.put("sUid", user.getUid());
            hashMap.put("name", name);
            hashMap.put("time", pTime);
            hashMap.put("timestamp",timestamp);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("message");
            ref.push().setValue(hashMap);
            editText.setText("");


            String topic="/topics/notification";

//
//
            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", name);
                notifcationBody.put("message", messsage);
                notification.put("to", topic);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
            sendNotification(notification);
//
//
        }

    }

    private void sendNotification(JSONObject notification) {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onErrorResponse: Didn't work"+error);

            }
        }){
        @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

        private void checkUser() {
            user=firebaseAuth.getCurrentUser();
            if(user!=null){
                if(user.isEmailVerified()){

                }else{
                    firebaseAuth.signOut();
                    startActivity(new Intent(ChatActivity.this,Login.class));
                    finish();
                }

            }else{
                startActivity(new Intent(ChatActivity.this,Login.class));
                finish();
            }
        }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/notification");
        Log.d("test", "onResumec: ");
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String,Object> h=new HashMap<>();
        h.put("status","online");
        ref.child(user.getUid()).updateChildren(h);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/notification");
        Log.d("test", "onDestroy: ");
    }


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/notification");
        Log.d("test", "onStopc: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/notification");
        Log.d("test", "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(user!=null){
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
            HashMap<String,Object> h=new HashMap<>();
            h.put("status","offline");
            h.put("typing","none");
            ref.child(user.getUid()).updateChildren(h);
        }

        Log.d("test", "onPause: ");
    }

    public void scroll(View view) {
        mMessageRecycler.scrollToPosition(messages.size() - 1);
    }
    public void checkTypingStatus(String typing){
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String,Object> h=new HashMap<>();
        h.put("typing",typing);
        ref.child(user.getUid()).updateChildren(h);
    }
}



