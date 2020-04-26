package com.example.notesex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/*
    Edit previously made notes, accessed from Details Activity
 */

public class Edit extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final String APP_TAG = "NoteEx";

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar calendar;
    String todaysDate;
    String currentTime;
    String timeStamp;
    String time;
    String date;
    ImageView imageView;
    int imageCount;
    int imageIndex;
    NoteExDatabase db;
    Note note;
    long noteId;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID",0);
        db = new NoteExDatabase(this);
        note = db.getNote(id);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);
        imageView = findViewById(R.id.editImage);

        noteTitle.setText(note.getTitle());
        noteDetails.setText(note.getContent());



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

        calendar = Calendar.getInstance();
        todaysDate = (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DAY_OF_MONTH)+ "/" + calendar.get(Calendar.YEAR)  ;
        currentTime = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));
        Log.d("calendar", "Date and Time: " + todaysDate +" and " + currentTime);
        imageCount = note.getImageCount();
        imageIndex = note.getImageIndex();
        time = note.getTime();
        date = note.getDate();
        timeStamp = date+time;

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
                    note.setTitle(noteTitle.getText().toString());
                    note.setContent(noteDetails.getText().toString());
                    int id = db.editNote(note);
                    if(id==note.getId()){
                        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                    }
                    else {
                       // Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(getApplicationContext(),Details.class);
                    intent.putExtra("ID",note.getId());
                    startActivity(intent);
        }
        if(item.getItemId() == R.id.Capture){
            dispatchTakePictureIntent();
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
                note.setImageCount(imageCount);

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
        note.setImageIndex(imageIndex);
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
        note.setImageIndex(imageIndex);
        Log.d("Current Index:", String.valueOf(imageIndex));
        displayImage();
    }


}







