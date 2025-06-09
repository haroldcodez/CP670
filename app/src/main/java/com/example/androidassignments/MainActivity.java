package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back arrow button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Log.i(TAG, "inside onCreate");

        // create login button object
        Button main_button = findViewById(R.id.button);

        // onclick listener for login button
        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ListItemsActivity.class), 10);
            }
        });

        Button chat_button = findViewById(R.id.chat_btn);

        //create onclick listener for the chat_button
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User clicked Start Chat");
                Intent chatIntent = new Intent(MainActivity.this, ChatWindow.class);
                startActivity(chatIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            Log.i(TAG, "Returned to MainActivity.onActivityResult");

            if (resultCode == MainActivity.RESULT_OK) {
                String messagePassed = data.getStringExtra("Response");
                String toastText = "ListItemsActivity passed: "+ messagePassed;
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            }
        }

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

    // handler for the back button on the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Or NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}