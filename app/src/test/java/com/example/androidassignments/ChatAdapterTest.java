package com.example.androidassignments;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ChatAdapterTest {

    private Context context;
    private ChatAdapter adapter;
    private Cursor cursor;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();

        // Build an in-memory cursor with the same columns as the real table
        MatrixCursor matrixCursor = new MatrixCursor(
                new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE});
        matrixCursor.addRow(new Object[]{1L, "Hello"});
        matrixCursor.addRow(new Object[]{2L, "World"});
        matrixCursor.addRow(new Object[]{3L, "Test"});
        cursor = matrixCursor;

        adapter = new ChatAdapter(context, cursor);
    }

    @Test
    public void testGetCount() {
        assertEquals(3, adapter.getCount());
    }

    @Test
    public void testGetItem() {
        cursor.moveToPosition(1);
        assertEquals("World", cursor.getString(
                cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
    }

    @After
    public void tearDown() {
        cursor.close();
    }
}