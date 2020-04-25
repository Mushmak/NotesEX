package com.example.notesex;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.content.FileProvider;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
    Create a new note, allow for editing and then add it to the main menu for viewing
*/

public class AddNote extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final String APP_TAG = "NoteEx";

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    ImageView imageView;
    Calendar calendar;
    String todaysDate;
    String currentTime;
    String currentImagePath = null;
    String timeStamp;
    Bitmap image;
    byte[] byteArray;

    int imageCount=0;
    int imageIndex=0;


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
        todaysDate = (calendar.get(Calendar.MONTH)+1) + "_" + calendar.get(Calendar.DAY_OF_MONTH)+ "_" + calendar.get(Calendar.YEAR)  ;
        currentTime = pad(calendar.get(Calendar.HOUR)) + "_" + pad(calendar.get(Calendar.MINUTE));
        Log.d("calendar", "Date and Time: " + todaysDate +" and " + currentTime);
        timeStamp = todaysDate+currentTime;

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
        if(item.getItemId() == R.id.recordAudio){

            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},20);
            }

                Intent aduioA = new Intent(this, AudioActivity.class);
                aduioA.putExtra("data",timeStamp);
                startActivity(aduioA);



        }
        return super.onOptionsItemSelected(item);
    }

    //Return to main menu
    private void goToMain(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    //Go back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Intent to open camera and take picture
    private void dispatchTakePictureIntent(){
        String fileName = "jpg_"+timeStamp+"_"+imageCount+"_.jpg";
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!= null){
            File imageFile = null;

            try {
                imageFile = getImageFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(imageFile != null){
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.android.fileprovider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

                startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
                imageCount++;

            }
        }
    }
    //sets image view to indexed image
    public void displayImage(){
        String imageName = "jpg_"+timeStamp+"_"+imageIndex+"_.jpg";
        File imageFile = null;
        try {
            imageFile = getImageFile(imageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imageFile.exists()){
            Log.d("Image","Exists");
            image= BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            imageView.setImageBitmap(image);
        }
        else{
            Log.d("Image","Does Not Exist");
        }
    }

    //gets file path
    private File getImageFile(String fileName) throws IOException {
        File imageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),APP_TAG);

        if(!imageDir.exists() && !imageDir.mkdirs()){
            Log.d(APP_TAG, "FAILED TO CREATE IMAGE DIRECTORY");
        }

        File image = new File(imageDir.getPath()+File.separator+fileName);
        return image;
    }

    //scrolls Image gallery backward
    public void previousImage(View view) {
        if(imageIndex == 0){
            imageIndex=imageCount;
        }
        else{
            imageIndex--;
        }
        displayImage();
    }
    //scrolls Image gallery forward
    public void nextImage(View view) {
        if(imageIndex==imageCount-1){
            imageIndex=0;
        }
        else{
            imageIndex++;
        }
        Log.d("Current Index:", String.valueOf(imageIndex));
        displayImage();
    }
}
