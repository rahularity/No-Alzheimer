package com.ithought.rahul.nozimers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    private static final String TAG = "MainActivity";
    private Button peopleAroundYou,peopleIKnow;
    private Bitmap photo;
    private String temp;
    private FloatingActionButton assist;
    private ByteArrayOutputStream bao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: OnCreate running");

        init();


        assist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AssistantActivity.class));
            }
        });




        peopleAroundYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: opening people around you function");

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//                Intent intent = new Intent(MainActivity.this,Profile.class);
//                startActivity(intent);
            }
        });


        peopleIKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPeopleList();
            }
        });

    }


    private void openPeopleList() {
        //make people's list from the database
    }

    private void init() {
        assist = (FloatingActionButton)findViewById(R.id.assistant);
        peopleIKnow = (Button)findViewById(R.id.peopleIKnow);
        peopleAroundYou = (Button)findViewById(R.id.people_around_you);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            detectFace(data);

        }
    }

    private void detectFace(Intent data) {
        photo = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        temp=Base64.encodeToString(b, Base64.DEFAULT);


        String url = "http://192.168.4.152:5000/connect/";

        //hashmap for storing image
        HashMap postData = new HashMap();
        postData.put("image", temp);



        //network requests to send picture and detect pictures
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(MainActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

                //if picture doesn't match any face in the database then it will give error message
                if (s.equals("error")){

                    Intent i = new Intent(MainActivity.this,AddPerson.class);
                    i.putExtra("pic",temp);
                    startActivity(i);

                }
                //if picture matches the any face then receiveing the data as JSON
                else{
                    Intent intent = new Intent(MainActivity.this,Profile.class);
                    intent.putExtra("profileDetails",s);
                    startActivity(intent);
                }

            }
        });
        readTask.execute(url);
    }

}
