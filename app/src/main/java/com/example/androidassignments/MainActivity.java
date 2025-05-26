package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.i(TAG, "inside onCreate");

        Button main_button = findViewById(R.id.button);

        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ListItemsActivity.class), 10);
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
}