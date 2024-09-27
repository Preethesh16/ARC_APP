package com.example.arcmember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash extends AppCompatActivity {
    int Cversion = 3;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences saved = getSharedPreferences("login", MODE_PRIVATE);
                String Username = saved.getString("Username","");
                String Password = saved.getString("Password","");
                databaseReference2 = FirebaseDatabase.getInstance().getReference().child("version");
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            int version = dataSnapshot.getValue(Integer.class);
                            if(Cversion!=version){
                                Intent intent = new Intent(splash.this, versionfail.class);
                                intent.putExtra("version",Cversion);
                                // Start the second activity
                                startActivity(intent);
                                finish();
                            }
                            else if (!Username.isEmpty() && !Password.isEmpty()) {
                                login(Username, Password);
                            }
                            else{
                                Intent intent = new Intent(splash.this, MainActivity.class);
                                // Start the second activity
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(splash.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        },1000);
    }

    public void login(String Username, String Password){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("access/"+Username+"/password");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String data = dataSnapshot.getValue(String.class);
                    if(data.equals(Password)){
                        SharedPreferences saver = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = saver.edit();
                        editor.putString("Username",Username);
                        editor.putString("Password",Password);
                        editor.apply();
                        Intent intent = new Intent(splash.this, SecondActivity.class);
                        intent.putExtra("Username",Username);
                        // Start the second activity
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(splash.this, MainActivity.class);
                        // Start the second activity
                        startActivity(intent);
                        finish();
                    }
                }
                else{
                    Intent intent = new Intent(splash.this, MainActivity.class);
                    // Start the second activity
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(splash.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });
    }
}