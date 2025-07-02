package com.example.androidassignments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    private static final String ACTIVITY_NAME = "ChatWindow";
    private ListView listView;
    private EditText editTextMessage;
    ArrayList<String> messages = new ArrayList<>();
    private SQLiteDatabase database;
    private ChatDatabaseHelper dbhelper;
    ChatAdapter messageAdapter;

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
        Button buttonSend = findViewById(R.id.sendButton);

        // Initialize the chatAdapter
        messageAdapter = new ChatAdapter(this, messages);
        listView.setAdapter(messageAdapter);

        // onClick listener for the send button
        buttonSend.setOnClickListener(v -> {
            String chatMessage = editTextMessage.getText().toString().trim();
            if(!chatMessage.isEmpty()) {
                long newRowId = insertMessageIntoDatabase(chatMessage);
                if (newRowId != -1) {
                    messages.add(chatMessage);
                    messageAdapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                    listView.smoothScrollToPosition(messages.size() - 1);
                } else {
                    Log.e(ACTIVITY_NAME, "Failed to insert message into the database");
                }
            }
        });
        dbhelper = new ChatDatabaseHelper(this);
        database = dbhelper.getWritableDatabase();

        loadMessagesFromDatabase();

    }

    // function to load database message and log results
    private void loadMessagesFromDatabase() {
        // Query to get  all messages from database
        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        Cursor cursor = database.query(
                ChatDatabaseHelper.TABLE_NAME,
                columns,
                null, null, null, null, null
        );

        // Log cursor information
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());

        // Print column names from the cursor
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Column " + i + ": " + cursor.getColumnName(i));
        }

        // Get column indexes from the database
        int messageColumnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        if (messageColumnIndex == -1) {
            Log.e(ACTIVITY_NAME, "Error: Column '" + ChatDatabaseHelper.KEY_MESSAGE + "' not found in cursor");
            cursor.close();
            return;
        }

        // extract and log all the saved messages in the database
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                try {
                    String message = cursor.getString(messageColumnIndex);
                    messages.add(message);
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + message);
                    cursor.moveToNext();
                } catch (Exception e) {
                    Log.e(ACTIVITY_NAME, "Error reading message from cursor", e);
                    break;
                }
            }
        } else {
            Log.i(ACTIVITY_NAME, "Cursor is empty - no messages in database");
        }

        cursor.close();
        messageAdapter.notifyDataSetChanged();
    }

    // function to insert messages to the database
    private long insertMessageIntoDatabase(String message) {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, message);

        try {
            return database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(ACTIVITY_NAME, "Error inserting message into database", e);
            return -1;
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (dbhelper != null) {
            dbhelper.close();
        }
    }

}