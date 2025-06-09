package com.example.androidassignments;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {
// declare variables
    private ListView listView;
    private EditText editTextMessage;
    private Button buttonSend;
    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // Set Toolbar as ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back arrow button on toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        listView = findViewById(R.id.listView);
        editTextMessage = findViewById(R.id.chatTextEditor);
        buttonSend = findViewById(R.id.sendButton);

        // Initialize the chatAdapter
        ChatAdapter messageAdapter = new ChatAdapter(this, messages);
        listView.setAdapter(messageAdapter);

        // onClick listener for the send button
        buttonSend.setOnClickListener(v -> {
            String chatMessage = editTextMessage.getText().toString().trim();
            if(!chatMessage.isEmpty()) {
                messages.add(chatMessage);
                messageAdapter.notifyDataSetChanged();
                editTextMessage.setText(" ");
                listView.smoothScrollToPosition(messages.size() - 1);
            }
        });
    }

    // handler for the back button on the toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}