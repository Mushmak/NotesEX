package com.example.notesex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

   public Note getNote(long id){
        // select * from databaseTable where id = 1
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.query(DATABASE_TABLE,new String[] {KEY_ID,KEY_TITLE, KEY_CONTENT, KEY_DATE, KEY_TIME}, KEY_ID+"=?",
               new String[] {String.valueOf(id)}, null, null, null);

       if (cursor  != null)
       {
           cursor.moveToFirst();
       }
       Note note = new Note(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
       return note;
   }

   public List<Note> getManyNotes()
   {
       SQLiteDatabase db = this.getReadableDatabase();
       List<Note> allNotes = new ArrayList<>();
       // get all the data from tables

       String query ="SELECT * FROM " +  DATABASE_TABLE+" ORDER BY " +KEY_ID+ " DESC";
       Cursor cursor = db.rawQuery(query,null);
       if (cursor.moveToFirst()){
           do{
                Note note = new Note();
                note.setId(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));

                allNotes.add(note);
           }while(cursor.moveToNext());

       }
       return allNotes;
   }

   public void DeleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
   }

   public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("edited","Edited Title -> " + note.getTitle() + "\n ID ->" +note.getId());
       c.put(KEY_TITLE,note.getTitle());
       c.put(KEY_CONTENT,note.getContent());
       c.put(KEY_DATE,note.getDate());
       c.put(KEY_TIME,note.getTime());
       return db.update(DATABASE_TABLE,c,KEY_ID+"=?",new String[]{String.valueOf(note.getId())});
   }

}
