package com.example.notesex;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NoteExDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "NoteDb";
    private static final String DATABASE_TABLE = "NoteTable";

    // column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "data";
    private static final String KEY_TIME = "time";

    NoteExDatabase(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DATABASE_TABLE +" ("+
               KEY_ID+" INTEGER PRIMARY KEY,"+
               KEY_TITLE+" TEXT,"+
               KEY_CONTENT+" TEXT,"+
               KEY_DATE+" TEXT,"+
               KEY_TIME+" TEXT"
               +" )";
       db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_TITLE, note.getTitle());
        c.put(KEY_CONTENT, note.getContent());
        c.put(KEY_DATE, note.getDate());
        c.put(KEY_TIME, note.getTime());

        long id = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID -> " + id);
        return id;

    }
}
