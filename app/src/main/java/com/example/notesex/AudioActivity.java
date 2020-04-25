package com.example.notesex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AudioActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    MediaRecorder mediaRecorder;
    Button play, record, stop;
    private static String audioFilePath;
    boolean isRecording  = false;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_activity);
        play = findViewById(R.id.playAudio);
        record = findViewById(R.id.record);
        stop = findViewById(R.id.stop);
        Note note = new Note();
        mediaRecorder = new MediaRecorder();
        String timestamp = getIntent().getStringExtra("data");
        if (!hasMic())
        {
            stop.setEnabled(false);
            play.setEnabled(false);
            record.setEnabled(false);
        } else {
            play.setEnabled(false);
            stop.setEnabled(false);
        }

        audioFilePath = getExternalCacheDir().getAbsolutePath()
                        + "/notex" + timestamp ;


        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    }
    protected boolean hasMic(){
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

   public void recordAudio(View view) throws IOException{
        isRecording = true;
       stop.setEnabled(true);
       play.setEnabled(true);
       record.setEnabled(true);
       if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ) {
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},20);
       }
       try {

           mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
           mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           mediaRecorder.setOutputFile(audioFilePath);
           mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
           mediaRecorder.prepare();
           mediaRecorder.start();
           Log.i("AudioRecorderTask", "Started recording");
       } catch (Exception e) {
           e.printStackTrace();
       }


   }

   public void stopAudio(View view){
        stop.setEnabled(false);
        play.setEnabled(true);

       if (isRecording)
       {
           record.setEnabled(false);
           mediaRecorder.stop();
           mediaRecorder.release();
           mediaRecorder = null;
           isRecording = false;
           Log.i("AudioRecorderTask", "Stopped recording");
       } else {
           mediaPlayer.release();
           mediaPlayer = null;
           record.setEnabled(true);
       }

   }

   public void playAudio(View view ) throws IOException{
        play.setEnabled(false);
        record.setEnabled(false);
        stop.setEnabled(true);

       mediaPlayer = new MediaPlayer();
       mediaPlayer.setDataSource(audioFilePath);
       mediaPlayer.prepare();
       mediaPlayer.start();

   }


}
