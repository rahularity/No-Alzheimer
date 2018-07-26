package com.ithought.rahul.nozimers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kairos.Kairos;
import com.kairos.KairosListener;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 0;
    private static final String TAG = "MainActivity";
    private Button peopleAroundYou,peopleIKnow;
    private Bitmap photo;
    private String temp;
    private FloatingActionButton assist;
    private ByteArrayOutputStream bao;
    private Kairos myKairos;
    private KairosListener listener;
    private static final String galleryId  = "MyGallery";
    private ProgressDialog pd;


//    private final String apiEndpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0";
//    private final String subscriptionKey = "2ae64a7350a44a03bfbe07e03a13ce49";

//    private final FaceServiceClient faceServiceClient =
//            new FaceServiceRestClient(apiEndpoint, subscriptionKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: OnCreate running");

        init();

        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("recognition", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has("Errors")){

                        pd.setMessage("face not recognized");
                        pd.dismiss();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        Intent i = new Intent(MainActivity.this, AddPerson.class);
                        i.putExtra("pic", byteArray);
                        startActivity(i);

                    }else if (object.has("images")){

                        pd.setMessage("recognition complete.");
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,"photo found", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);
            }
        };

        // instantiate a new kairos instance
        myKairos = new Kairos();
        // set authentication
        String app_id = "21a46ffd";
        String api_key = "aae746902a1c58b9e658e9f118fc7528";
        myKairos.setAuthentication(this, app_id, api_key);


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
        pd = new ProgressDialog(this);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            detectFace(data);

        }
    }

    private void detectFace(Intent data) {
        photo = (Bitmap) data.getExtras().get("data");
        pd.show();
        pd.setMessage("please wait...");
        pd.setCancelable(false);

        String galleryId1 = galleryId;
        String selector = "FULL";
        String threshold = "0.75";
        String minHeadScale = "0.25";
        String maxNumResults = "25";
        try {
            myKairos.recognize(photo,
                    galleryId1,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
