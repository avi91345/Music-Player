package com.example.sonicsphere;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;

public class PLAYER extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseekbar.interrupt();
    }
    @SuppressLint("DefaultLocale")
    private String formatt(int miles){
        int seconds=miles/1000;
        int mins=seconds/60;
        seconds%=60;
        return String.format("%d:%02d",mins,seconds);
    }
    Toolbar toolbar;
    ImageView navigationIcon,back,pause,next;
    TextView textView,starttime,stoptime;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String songname;
    int pos;
    SeekBar seekbar;
    Thread updateseekbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        back=findViewById(R.id.imageView);
        pause=findViewById(R.id.imageView4);
        next=findViewById(R.id.imageView5);
        textView=findViewById(R.id.textView2);
        seekbar=findViewById(R.id.seekBar2);
        starttime=findViewById(R.id.textView4);
        stoptime=findViewById(R.id.textView5);
        toolbar = findViewById(R.id.toolbar2);
        navigationIcon = findViewById(R.id.toolbarNavigationIcon);

        // Set navigation icon click listener
        navigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null) {
            songs=(ArrayList) bundle.getParcelableArrayList("SONG");
            songname = (String) bundle.getString("SongName");
            pos=bundle.getInt("position");
        }

        textView.setText(songname);
        Uri uri=Uri.parse(songs.get(pos).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekbar.setMax(mediaPlayer.getDuration());
        stoptime.setText(formatt(mediaPlayer.getDuration()));


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    starttime.setText(formatt(progress));

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Handler handler = new Handler();


        updateseekbar = new Thread() {
            @Override
            public void run() {
                try {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currpos = mediaPlayer.getCurrentPosition();

                        seekbar.setProgress(currpos);
                        starttime.setText(formatt(currpos));

                        handler.postDelayed(this, 800);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        updateseekbar.start();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.baseline_play_arrow_24);
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.pause);
                    handler.post(updateseekbar);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.release();
                if(pos!=0){
                    pos-=1;
                }
                else{
                    pos=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                songname=songs.get(pos).getName().replace(".mp3", "");
                textView.setText(songname);
                seekbar.setProgress(0);
                seekbar.setMax(mediaPlayer.getDuration());
                stoptime.setText(formatt(mediaPlayer.getDuration()));
                handler.post(updateseekbar);


            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                mediaPlayer.release();
                if(pos!= songs.size()-1){
                    pos+=1;
                }
                else{
                    pos=0;
                }
                Uri uri=Uri.parse(songs.get(pos).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                songname=songs.get(pos).getName().replace(".mp3", "");
                textView.setText(songname);
                seekbar.setProgress(0);
                seekbar.setMax(mediaPlayer.getDuration());
                stoptime.setText(formatt(mediaPlayer.getDuration()));
                handler.post(updateseekbar);


            }
        });







    }
}
