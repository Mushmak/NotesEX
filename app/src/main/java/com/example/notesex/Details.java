package com.example.notesex;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/*
    View previously made notes.  Accessed from Main activity after selecting a note listed.
 */
public class Details extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public final String APP_TAG = "NoteEx";
    TextView noteDets;
    ImageView imageView;
    Note note;
    byte[] byteArray;
    Bitmap image;
    NoteExDatabase db;
    String timeStamp;
    String date;
    String time;
    int imageCount;
    int imageIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        noteDets = findViewById(R.id.noteDets);
        imageView = findViewById(R.id.detailImage);

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

        date = note.getDate();
        time = note.getTime();
        timeStamp = date+time;
        imageCount = note.getImageCount();
        imageIndex = note.getImageIndex();
        displayImage();

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

    //Extra menu in top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }
    //Action called based on item pressed.
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editNote){
            Intent intent = new Intent(this,Edit.class);
            intent.putExtra("ID",note.getId());
            startActivity(intent);

        }
        if(item.getItemId() == R.id.delete){
            AlertDialog.Builder dialong = new AlertDialog.Builder(this);
            dialong.setTitle("Delete");
            dialong.setMessage("Do you want to delete this note? ");
            dialong.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.DeleteNote(note.getId());
                    Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
            dialong.setNegativeButton("NO",null);
            AlertDialog di = dialong.create();
            di.show();


          //  onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
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
