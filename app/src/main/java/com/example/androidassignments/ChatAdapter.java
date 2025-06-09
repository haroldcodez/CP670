package com.example.androidassignments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<String> {
    // declare variables
    private final Context context;
    private final List<String> messages;

    // declare the constructor
    public ChatAdapter(Context context, List<String> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public String getItem(int position) {
        return messages.get(position);
    }

    // declare a getView method to regulate the display of the chat text either as incoming or outgoing
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View result;

        if (position % 2 == 0) {
            result = inflater.inflate(R.layout.chat_row_incoming, parent, false);
        } else {
            result = inflater.inflate(R.layout.chat_row_outgoing, parent, false);
        }

        TextView messageView = result.findViewById(R.id.message_text);
        messageView.setText(getItem(position));

        return result;
    }
}
