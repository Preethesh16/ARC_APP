package com.example.arcmember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference databaseReference;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        Username = intent.getStringExtra("Username");
        TextView greet = findViewById(R.id.textView6);
        greet.setText("Hi, "+Username);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("access/"+Username+"/status");
        TextView feedback = findViewById(R.id.textView3);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String data = dataSnapshot.getValue(String.class);
                    Status=data;
                    if(data.equals("granted")){
                        feedback.setText("Granted");
                        feedback.setTextColor(Color.parseColor("#7ed957"));
                    }
                    else{
                        feedback.setText("Denied");
                        feedback.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SecondActivity.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void open(View view){
        TextView feedback = findViewById(R.id.textView3);
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
    public void click(View view){
        Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
        intent.putExtra("Username",Username);
        // Start the second activity
        startActivity(intent);
    }
}