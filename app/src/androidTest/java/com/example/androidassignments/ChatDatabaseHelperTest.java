package com.example.androidassignments;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatDatabaseHelperTest {

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        dbHelper = new ChatDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + ChatDatabaseHelper.TABLE_NAME); // Clean slate
    }

    @After
    public void tearDown() {
        db.close();
        dbHelper.close();
    }

    @Test
    public void testDatabaseCreation() {
        assertTrue(db.isOpen());
    }

    @Test
    public void testInsertMessage() {
        String message = "Hello, test!";
        db.execSQL("INSERT INTO " + ChatDatabaseHelper.TABLE_NAME + " (" + ChatDatabaseHelper.KEY_MESSAGE + ") VALUES (?)", new Object[]{message});

        Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        assertTrue(cursor.moveToFirst());
        String retrieved = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE));
        assertEquals(message, retrieved);
        cursor.close();
    }

    @Test
    public void testOnUpgradeDropsTable() {
        db.execSQL("INSERT INTO " + ChatDatabaseHelper.TABLE_NAME + " (" + ChatDatabaseHelper.KEY_MESSAGE + ") VALUES ('Old message')");
        dbHelper.onUpgrade(db, 1, 2);

        Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        assertEquals(0, cursor.getCount()); // Table should be recreated
        cursor.close();
    }
}
