package com.cosmas.dev.ug.apps.schoolfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        Handler handler = new Handler();
        Runnable runnable = () -> {

            auth.addAuthStateListener(firebaseAuth -> {
                FirebaseUser current_user=firebaseAuth.getCurrentUser();
                if (current_user == null){
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(MainActivity.this,DashboardApp.class));
                    finish();
                }

            });

        };
        handler.postDelayed(runnable,3000);
    }
}