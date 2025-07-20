package com.example.androidassignments;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cursoradapter.widget.CursorAdapter;

public class ChatAdapter extends CursorAdapter {

    // declare variable to regulate the view selection
    private int sideToggle = 0;

    public ChatAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        sideToggle = 1 - sideToggle;

        if (sideToggle == 0) {
            return inflater.inflate(R.layout.chat_row_incoming, parent, false);
        } else {
            return inflater.inflate(R.layout.chat_row_outgoing, parent, false);
        }
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView messageView = view.findViewById(R.id.message_text);

        // Get message from cursor
        int messageColumnIndex = cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE);
        if (messageColumnIndex != -1) {
            String message = cursor.getString(messageColumnIndex);
            messageView.setText(message);
        } else {
            Log.i("ChatAdapter", "Message not found");
        }
    }

    // function to get the message id from the DB
    @Override
    public long getItemId(int position) {
        Cursor c = getCursor();
        if (c.moveToPosition(position)) {
            return c.getLong(c.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID));
        }
        return -1;
    }
}
