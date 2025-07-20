package com.example.androidassignments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

public class ChatWindow extends AppCompatActivity {
    // declare variables
    private static final String ACTIVITY_NAME = "ChatWindow";
    private ListView listView;
    private EditText editTextMessage;
    private SQLiteDatabase database;
    private ChatDatabaseHelper dbhelper;
    private Cursor cursor;
    private boolean isTablet;
    private static final int REQUEST_DELETE_MESSAGE = 1;
    ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // check if the device is a tablet
        FrameLayout frame = findViewById(R.id.frame_layout);
        isTablet = frame != null;

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

        // Initialize the database and messageAdapter
        dbhelper = new ChatDatabaseHelper(this);
        database = dbhelper.getWritableDatabase();
        cursor = getMessagesCursor();
        messageAdapter = new ChatAdapter(this, cursor);
        listView.setAdapter(messageAdapter);

        // onClick listener for the send button
        buttonSend.setOnClickListener(v -> {
            String chatMessage = editTextMessage.getText().toString().trim();
            if(!chatMessage.isEmpty()) {
                long newRowId = insertMessageIntoDatabase(chatMessage);
                if (newRowId != -1) {
                    editTextMessage.setText("");
                } else {
                    Log.e(ACTIVITY_NAME, "Failed to insert message into the database");
                }
            }
        });

        // onClick listener for the listview messages
        listView.setOnItemClickListener((p, v, pos, id) -> {
            cursor.moveToPosition(pos);        // make sure cursor points to row
            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));

            if (isTablet) {
                showMessageInFragment(id, text);
            } else {
                startMessageDetailsActivity(id, text);
            }
        });

        loadMessagesFromDatabase();

    }

    // function to get messages from the database cursor object
    private Cursor getMessagesCursor() {
        String[] columns = {ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE};
        return database.query(
                ChatDatabaseHelper.TABLE_NAME,
                columns,
                null, null, null, null, null
        );
    }

    // method to display the selected message in fragment view
    private void showMessageInFragment(long messageId, String messageText) {
        MessageDetails.MessageFragment fragment =
                MessageDetails.MessageFragment.newInstance(messageId, messageText, this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private void startMessageDetailsActivity(long messageId, String messageText) {
        Intent msgIntent = new Intent(this, MessageDetails.class);
        msgIntent.putExtra(MessageDetails.EXTRA_MESSAGE_ID, messageId);
        msgIntent.putExtra(MessageDetails.EXTRA_MESSAGE_TEXT, messageText);
        startActivityForResult(msgIntent, REQUEST_DELETE_MESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DELETE_MESSAGE && resultCode == RESULT_OK) {
            long messageId = data.getLongExtra(MessageDetails.EXTRA_MESSAGE_ID, -1);
            if (messageId != -1) {
                deleteMessage(messageId);
            }
        }
    }

    // function to load database message and log results
    private void loadMessagesFromDatabase() {
        //if (cursor != null && !cursor.isClosed()) cursor.close();

        cursor = getMessagesCursor();
        messageAdapter.changeCursor(cursor);   // CursorAdapter does the rest
    }

    // function to insert messages to the database
    private long insertMessageIntoDatabase(String message) {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, message);

        long id = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        if (id != -1) {
            loadMessagesFromDatabase();   // refresh cursor
            listView.smoothScrollToPosition(messageAdapter.getCount() - 1);
        }
        return id;
    }

    // function to delete messages from ChatWindow
    public void deleteMessage(long messageId) {
        boolean ok = dbhelper.deleteMessage(messageId);
        if (ok) {
            loadMessagesFromDatabase();
            if (isTablet) {
                Fragment f = getSupportFragmentManager()
                        .findFragmentById(R.id.frame_layout);
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().remove(f).commit();
                }
            }
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
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
        if (cursor != null) cursor.close();
        if (database != null && database.isOpen()) database.close();
    }

}