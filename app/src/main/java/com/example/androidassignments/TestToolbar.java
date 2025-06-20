package com.example.androidassignments;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.androidassignments.databinding.ActivityTestToolbarBinding;

import java.util.Objects;

public class TestToolbar extends AppCompatActivity {

    private ActivityTestToolbarBinding binding;

    // variable to hold the value of the message entered in the alert dialog when option 3 is clicked
    String savedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestToolbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // Enable back arrow button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // onclick listener for the fab button
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Welcome to my Toolbar page", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.fab)
                        .setAction("Action", null).show();
            }
        });
    }

    // create menu options to be attached to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    // setup actions for the menu items when clicked using Log messages, snackBar and dialogs
    @Override
    public boolean onOptionsItemSelected(MenuItem mi){

        int id = mi.getItemId();

        AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);

        // action for when back button on the toolbar is clicked.
        if (mi.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // action for when option 1 menu is clicked
        if (id == R.id.action_fb) {
            Log.d("Toolbar", "Facebook Selected");

            Snackbar.make(binding.getRoot(), Objects.requireNonNullElse(savedMessage, "You selected item 1"), Snackbar.LENGTH_LONG).show();

            return true;

        // action for when option 2 menu is clicked
        } else if (id == R.id.action_twt) {
            Log.d("Toolbar", "Twitter-X Selected");

            builder.setMessage(R.string.toolbar_dialog_message);
            builder.setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            return true;

        // action for when option 1 menu is clicked
        } else if (id == R.id.action_tik) {
            Log.d("Toolbar", "Tik-Tok Selected");
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);

            builder.setView(dialogView)
                    .setTitle(R.string.new_msg)
                    .setPositiveButton(R.string.ok, (dialog, m_id) -> {
                        EditText new_message = dialogView.findViewById(R.id.dialog_message_box);
                        savedMessage = new_message.getText().toString();
                    })
                    .setNegativeButton(R.string.cancel, (dialog, m_id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            return true;

        // action for when the hidden about menu is clicked
        } else if (id == R.id.action_about) {
            Log.d("Toolbar", "Version 1.0 by Nonso Harold Ukwuma");
            return true;

        // default action
        } else {
            Log.d("Toolbar", "Nothing has been Selected");
        }

        return super.onOptionsItemSelected(mi);

    }

}