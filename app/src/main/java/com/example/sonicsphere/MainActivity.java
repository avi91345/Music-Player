package com.example.sonicsphere;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> mysongs = arlst(Environment.getExternalStorageDirectory());
                        String[] name = new String[mysongs.size()];
                        int[] imageResIds = new int[mysongs.size()];
                        for (int i = 0; i < mysongs.size(); i++) {
                            name[i] = mysongs.get(i).getName().replace(".mp3", "");
                            imageResIds[i] = R.drawable.ad3;
                        }



                        CustomAdapter c = new CustomAdapter(name, imageResIds);
                        listView.setAdapter(c);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String currentsong=listView.getItemAtPosition(position).toString();
                                Intent intent=new Intent(MainActivity.this, PLAYER.class);
                                intent.putExtra("SONG",mysongs);
                                intent.putExtra("SongName",currentsong);
                                intent.putExtra("position",position);
                                startActivity(intent);

                            }
                        });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> arlst(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myfiles : songs) {
                if (!myfiles.isHidden() && myfiles.isDirectory()) {
                    arrayList.addAll(arlst(myfiles));
                } else if (myfiles.getName().endsWith(".mp3") && !myfiles.getName().startsWith(".")) {
                    arrayList.add(myfiles);
                }
            }
        }
        return arrayList;
    }
}
