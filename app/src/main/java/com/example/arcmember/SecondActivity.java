package com.example.arcmember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondActivity extends AppCompatActivity {
    String Username;
    String Status;
    private DatabaseReference switchesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference , nameReference ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        Username = intent.getStringExtra("Username");
        TextView greet = findViewById(R.id.textView17);

        Switch switch0 = findViewById(R.id.switch1);
        Switch switch1 = findViewById(R.id.switch2);
        Switch switch2 = findViewById(R.id.switch3);
        Switch switch3 = findViewById(R.id.switch4);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("access/"+Username+"/status");
        TextView feedback = findViewById(R.id.textView18);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String data = dataSnapshot.getValue(String.class);
                    Status=data;
                    if(data.equals("granted")){
                        feedback.setText("Granted");
                        feedback.setTextColor(Color.parseColor("#7ed957"));
                        switch0.setEnabled(true);
                        switch1.setEnabled(true);
                        switch2.setEnabled(true);
                        switch3.setEnabled(true);
                    }
                    else{
                        feedback.setText("Denied");
                        feedback.setTextColor(Color.RED);
                        switch0.setEnabled(false);
                        switch1.setEnabled(false);
                        switch2.setEnabled(false);
                        switch3.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });

        nameReference = FirebaseDatabase.getInstance().getReference().child("access/"+Username+"/Name");
        nameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String data = dataSnapshot.getValue(String.class);
                    greet.setText("Hi, "+data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });
        switchesRef = FirebaseDatabase.getInstance().getReference().child("switches");


        setup(0, switch0);
        setup(1, switch1);
        setup(2, switch2);
        setup(3, switch3);

        DatabaseReference databaseReference2;

        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("switches");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setup(0, switch0);
                setup(1, switch1);
                setup(2, switch2);
                setup(3, switch3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });

        switch0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton(0, switch0);
            }
        });
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton(1, switch1);
            }
        });
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton(2, switch2);
            }
        });
        switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButton(3, switch3);
            }
        });

    }
    public void open(View view){
        TextView feedback = findViewById(R.id.textView18);
        if(Status.equals("granted")){
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference1 = database1.getReference("lock");
            String status = "open";
            databaseReference1.setValue(status);
            feedback.setText("Granted");
            feedback.setTextColor(Color.GREEN);
            Button btn = findViewById(R.id.button2);
            btn.setBackground(getResources().getDrawable(R.drawable.round_btn_white, null));
            btn.setTextColor(Color.parseColor("#5e17eb"));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btn.setBackground(getResources().getDrawable(R.drawable.round_btn, null));
                    btn.setTextColor(Color.parseColor("#FFFFFF"));
                }
            },500);
        }
        else{
            feedback.setText("Denied");
            feedback.setTextColor(Color.RED);
        }

    }
    private void setup(int buttonNumber, Switch switches){
        switchesRef.child(String.valueOf(buttonNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String value = dataSnapshot.getValue(String.class);


                    // Update the value in Firebase
                    switchesRef.child(String.valueOf(buttonNumber)).setValue(value);

                    // Update the button color based on the new value
                    if (value.equals("ON")) {
                        switches.setChecked(true);
                    } else {
                        switches.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
    private void toggleButton(int buttonNumber, Switch switches) {
        if(Status.equals("granted")) {
            switchesRef.child(String.valueOf(buttonNumber)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String value = dataSnapshot.getValue(String.class);
                        String newValue;
                        if(value.equals("ON"))
                            newValue = "OFF";
                        else
                            newValue = "ON";


                        // Update the value in Firebase
                        switchesRef.child(String.valueOf(buttonNumber)).setValue(newValue);

                        // Update the button color based on the new value
                        if (newValue == "ON") {
                            switches.setChecked(true);
                        } else if (newValue == "OFF"){
                            switches.setChecked(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                }
            });
        }
    }
}