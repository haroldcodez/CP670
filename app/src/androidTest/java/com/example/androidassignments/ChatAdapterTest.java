package com.example.androidassignments;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ChatAdapterTest {

    private ChatAdapter adapter;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        List<String> messages = Arrays.asList("Hello", "World", "Test");
        adapter = new ChatAdapter(context, messages);
    }

    // test that the adapter returns the current count of items
    @Test
    public void testGetCount() {
        assertEquals(3, adapter.getCount());
    }

    // test that the adapter returns the correct item from the specified position
    @Test
    public void testGetItem() {
        assertEquals("World", adapter.getItem(1));
    }

    // test that the adapter is sending data to the listview and that the data is displayed in the TextView position
    @Test
    public void testGetViewInflatesCorrectLayout() {
        View view = adapter.getView(0, null, new android.widget.FrameLayout(context));
        assertNotNull(view);
        TextView messageView = view.findViewById(R.id.message_text);
        assertNotNull(messageView);
        assertEquals("Hello", messageView.getText().toString());
    }
}
