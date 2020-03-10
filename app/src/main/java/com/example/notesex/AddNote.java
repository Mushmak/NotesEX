package com.example.notesex;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    ImageView imageView;
    Calendar calendar;
    String todaysDate;
    String currentTime;
    Bitmap image;
    byte[] byteArray;

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
        imageView = findViewById(R.id.noteImage);

        //Text Listener watch for changes in title and update note title in support bar
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
                if(s.length() != 0) {
                    getSupportActionBar().setTitle(s);
                }
            }
        });

        //Get current date and time
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

    //Use Save Menu toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    //Provide Save menu toolbar functionality
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save) {
            Note note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime, byteArray);
            NoteExDatabase db = new NoteExDatabase(this);
            Log.d("Inserted", "Title -> " + noteTitle.getText().toString());
            Log.d("Inserted", "Details -> " + noteDetails.getText().toString());
            Log.d("Inserted", "Date -> " + todaysDate);
            Log.d("Inserted", "Time -> " + currentTime);
            Log.d("Inserted", "Image");
            db.addNote(note);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            goToMain();
        }
        if(item.getItemId() == R.id.Capture){
            dispatchTakePictureIntent();

        }
        if(item.getItemId() == R.id.showImage_add){
            if(imageView.getImageAlpha() == 0){
                imageView.setImageAlpha(255);
            }
            else{
                imageView.setImageAlpha(0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            imageView.setImageBitmap(image);
            byteArray = getBitmapAsByteArray(image);
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
