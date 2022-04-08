package com.cosmas.dev.ug.apps.schoolfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class NewAccount extends AppCompatActivity {
    private EditText email,password,confirm;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confrmPassword);
        auth = FirebaseAuth.getInstance();
    }

    public void back_to_login(View view) {
      onBackPressed();
      finish();
    }

    public void add_user(View view) {
        String mail=email.getText().toString().trim();
        String pass=password.getText().toString().trim();
        String passConfirm=confirm.getText().toString().trim();

        if(TextUtils.isEmpty(mail)){
            email.setError("Email is required");
        }
        else if(TextUtils.isEmpty(mail) || pass.length() < 6){
            password.setError("Password at least 6 characters");
        }
        else if(!pass.equals(passConfirm)){
            confirm.setError("Password confirmation failed");
        }
        else{
            auth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(NewAccount.this,"You have created the account",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(NewAccount.this,LoginActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(NewAccount.this,"Something wrong happened",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(NewAccount.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

            });
        }
    }
}