package com.example.androidassignments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MessageDetails extends AppCompatActivity {

    // declare variables
    public static final String EXTRA_MESSAGE_ID = "message_id";
    public static final String EXTRA_MESSAGE_TEXT = "message_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        long messageId = getIntent().getLongExtra(EXTRA_MESSAGE_ID, -1);
        String messageText = getIntent().getStringExtra(EXTRA_MESSAGE_TEXT);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        MessageFragment.newInstance(messageId, messageText, null))
                .commit();
    }

    // MessageFragment class inside the MessageDetails Fragment
    public static class MessageFragment extends Fragment {
        private ChatWindow chatActivity;
        private static final String ARG_MESSAGE_ID = "message_id";
        private static final String ARG_MESSAGE_TEXT = "message_text";

        public static MessageFragment newInstance(long id, String text, ChatWindow activity) {
            MessageFragment fragment = new MessageFragment();
            Bundle args = new Bundle();
            args.putLong(ARG_MESSAGE_ID, id);
            args.putString(ARG_MESSAGE_TEXT, text);
            fragment.setArguments(args);
            fragment.chatActivity = activity;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_message_detail, container, false);

            Bundle args = getArguments();
            if (args != null) {
                TextView idText = view.findViewById(R.id.message_id_text);
                TextView msgText = view.findViewById(R.id.message_text);
                Button deleteBtn = view.findViewById(R.id.delete_button);

                idText.setText("ID: " + args.getLong(ARG_MESSAGE_ID));
                msgText.setText(args.getString(ARG_MESSAGE_TEXT));

                // onClick listener for the delete button
                deleteBtn.setOnClickListener(v -> {
                    long messageId = args.getLong(ARG_MESSAGE_ID);
                    if (messageId == -1) {
                        Toast.makeText(getContext(), "Invalid message ID", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // check if the view is a tablet or mobile and invoke the corresponding delete function
                    if (chatActivity != null) {
                        chatActivity.deleteMessage(messageId);
                    } else if (getActivity() != null) {
                            Intent result = new Intent();
                            result.putExtra(EXTRA_MESSAGE_ID, messageId);
                            getActivity().setResult(RESULT_OK, result);
                            getActivity().finish();

                            Toast.makeText(getContext(), "Message deleted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
            return view;
        }
    }

}