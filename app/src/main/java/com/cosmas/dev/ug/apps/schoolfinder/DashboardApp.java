package com.cosmas.dev.ug.apps.schoolfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.window.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

public class DashboardApp extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setContentView(R.layout.activity_dasboard_app);
auth=FirebaseAuth.getInstance();
user=auth.getCurrentUser();
    }

    public void add_school(View view) {
        Intent intent = new Intent(DashboardApp.this,AddSchool.class);
        startActivity(new Intent(intent));

    }

    public void view_university(View view) {
        Intent intent = new Intent(DashboardApp.this,ViewSchool.class);
        intent.putExtra("key","University/Institution");
        startActivity(new Intent(intent));
    }

    public void view_secondary(View view) {
        Intent intent = new Intent(DashboardApp.this,ViewSchool.class);
        intent.putExtra("key","Secondary School");
        startActivity(new Intent(intent));
    }

    public void view_primary(View view) {
        Intent intent = new Intent(DashboardApp.this,ViewSchool.class);
        intent.putExtra("key","Primary School");
        startActivity(new Intent(intent));
    }

    public void view_nursery(View view) {
        Intent intent = new Intent(DashboardApp.this,ViewSchool.class);
        intent.putExtra("key","Nursery School");
        startActivity(new Intent(intent));
    }

    public void view_both(View view) {
        Intent intent = new Intent(DashboardApp.this,ViewSchool.class);
        intent.putExtra("key","Primary and Nursery");
        startActivity(new Intent(intent));
    }

    public void logout(View view) {
        AlertDialog.Builder builder= new AlertDialog.Builder(DashboardApp.this);
        builder.setMessage(" Are sure you want to logout "+user.getEmail()+"?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        Intent intent = new Intent(DashboardApp.this,MainActivity.class);
                        startActivity(new Intent(intent));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}