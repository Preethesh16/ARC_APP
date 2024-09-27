package com.example.arcmember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences saved = getSharedPreferences("login", MODE_PRIVATE);
        String Username = saved.getString("Username","");
        String Password = saved.getString("Password","");
        if (!Username.isEmpty() && !Password.isEmpty()) {
            login(Username, Password);
        }

    }
    public void submit(View view) {
        EditText username = findViewById(R.id.editTextText);
        EditText password = findViewById(R.id.editTextTextPassword);
        TextView feedback = findViewById(R.id.textView);

        if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            login(username.getText().toString(), password.getText().toString());
        }
        else
            feedback.setText("Enter username and password");
    }
    public void login(String Username, String Password){
        TextView feedback = findViewById(R.id.textView);

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
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra("Username",Username);
                        // Start the second activity
                        startActivity(intent);
                        finish();
                    }
                    else
                        feedback.setText("Invalid Password");
                }
                else
                    feedback.setText("Invalid username");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Connectivity issue", Toast.LENGTH_SHORT).show();
            }
        });

    }
}