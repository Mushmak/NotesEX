package com.example.notesex;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends AppCompatActivity {
    TextView noteDets;
    Note note;
    NoteExDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteDets = findViewById(R.id.noteDets);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID",0);

        db = new NoteExDatabase(this);
       // NoteExDatabase db = new NoteExDatabase(this);
       // Note note = db.getNote(id);
        note =db.getNote(id);
        getSupportActionBar().setTitle(note.getTitle());
        noteDets.setText(note.getContent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noteDets.setMovementMethod(new ScrollingMovementMethod());


      /*  FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editNote){
            Intent intent = new Intent(this,Edit.class);
            intent.putExtra("ID",note.getId());
            startActivity(intent);

        }
        if(item.getItemId() == R.id.delete){
            db.DeleteNote(note.getId());
            Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
          //  onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }




}