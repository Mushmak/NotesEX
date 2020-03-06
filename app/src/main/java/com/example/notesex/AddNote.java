package com.example.notesex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        calendar = Calendar.getInstance();
        todaysDate = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH)+ "/" + calendar.get(Calendar.YEAR)  ;
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
        Log.d("calendar", "Date and Time: " + todaysDate +" and " + currentTime);
    }

    private String pad(int i) {
        if(i < 10) {
            return "0" + i;
        }
        else {
            return String.valueOf(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save) {
            Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime);
            NoteExDatabase db = new NoteExDatabase(this);
            Log.d("Inserted", "Title -> " + noteTitle.getText().toString());
            Log.d("Inserted", "Details -> " + noteDetails.getText().toString());
            Log.d("Inserted", "Date -> " + todaysDate);
            Log.d("Inserted", "Time -> " + currentTime);
            db.addNote(note);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
