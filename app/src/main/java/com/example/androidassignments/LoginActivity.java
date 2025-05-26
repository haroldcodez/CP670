package com.example.androidassignments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.i(TAG, "inside onCreate");

        loadSavedData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "inside onResume");

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "inside onStart");

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "inside onPause");


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "inside onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "inside onDestroy");

    }

    private void loadSavedData() {

        String data_file = getString(R.string.filename);
        SharedPreferences shared_pref = getSharedPreferences(data_file, MODE_PRIVATE);

        String user_email_key = getString(R.string.email_address);
        String user_email_value = shared_pref.getString(user_email_key, "example@domain.com");
        ((EditText) findViewById(R.id.login_text)).setText(user_email_value);


    }

    private void saveEmail() {

        String data_file_name = getString(R.string.filename);
        SharedPreferences emailPrefs = getSharedPreferences(data_file_name, MODE_PRIVATE);

        SharedPreferences.Editor emailEditor = emailPrefs.edit();
        emailEditor.clear();

        EditText emailText = findViewById(R.id.login_text);
        String user_email_key  = getString(R.string.email_address);
        String new_email = emailText.getText().toString();


        emailEditor.putString(user_email_key, new_email);

        emailEditor.apply();

    }

    public void onLoginClicked(View view) {

        EditText emailText = findViewById(R.id.login_text);
        EditText passwordText = findViewById(R.id.password_text);

        String new_email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (new_email.isEmpty()) {
            emailText.setError("Email cannot be empty");
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_LONG).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(new_email).matches()) {
            emailText.setError("Invalid email format");
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            passwordText.setError("Password cannot be empty");
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        saveEmail();

        Intent saveIntent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(saveIntent);
    }
}