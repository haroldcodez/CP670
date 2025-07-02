package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    //declare variables
    private static final String DATABASE_NAME = "Messages.db";
    private static final int VERSION_NUM = 2;
    public final static String KEY_ID = "id";
    public final static String KEY_MESSAGE = "message";
    public static final String TABLE_NAME = "chats";

    // declare variable to facilitate the database creation
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + KEY_ID
            + " integer primary key  autoincrement, " + KEY_MESSAGE
            + " text not null);";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + ", newVersion=" + newVer);

    }
}
