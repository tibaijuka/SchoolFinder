package com.cosmas.dev.ug.apps.schoolfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
    }
    public void open_sign_up(View view) {
        startActivity(new Intent(LoginActivity.this, NewAccount.class));
    }

    public void login_user(View view) {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (TextUtils.isEmpty(mail)) {
            Snackbar.make(view, "Email is required", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(pass)) {
            Snackbar.make(view, "Password is required", Snackbar.LENGTH_LONG).show();
        } else {

            auth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    Snackbar.make(view, "Account Found", Snackbar.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Snackbar.make(view, "Login Failed", Snackbar.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e ->
                    Snackbar.make(view, "Error "+e.getMessage(), Snackbar.LENGTH_LONG).show());
        }

    }

}