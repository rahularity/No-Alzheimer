package com.ithought.rahul.nozimers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.kairos.Kairos;
import com.kairos.KairosListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddPerson extends AppCompatActivity {
    private HashMap<String , String> Data;
    private EditText mContactName,mContactLivesIn,mContactAge,mContactPlace,mContactRelation,mContactNotes;
    private Button mContactSubmit;
    private String mName,mLivesIn,mAge,mPlace,mRelation,mNotes;
    private Bitmap photo;
    private Kairos myKairos;
    private KairosListener listener;
    private static final String galleryId  = "MyGallery";
    private ProgressDialog progress;
    private String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        init();

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        // textView is the TextView view that should display it

        byte[] byteArray = getIntent().getByteArrayExtra("pic");
        photo = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                // your code here!
                Log.d("KAIROS DEMO", response);

                Toast.makeText(AddPerson.this,response,Toast.LENGTH_LONG).show();
                
                progress.dismiss();
                Intent i = new Intent(AddPerson.this, MainActivity.class);
                startActivity(i);
                finish();
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
        String app_id = "56b9c638";
        String api_key = "5a99824f93696cad8c89cce98709bca4";
        myKairos.setAuthentication(this, app_id, api_key);


        mContactSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mName = mContactName.getText().toString();
                mLivesIn = mContactLivesIn.getText().toString();
                mAge = mContactAge.getText().toString();
                mPlace = mContactPlace.getText().toString();
                mRelation = mContactRelation.getText().toString();
                mNotes = mContactNotes.getText().toString();

//                HashMap postData = new HashMap();
//                /*postData.put("image", temp);*/
//                postData.put("name",mName);
//                postData.put("livesIn",mLivesIn);
//                postData.put("age",mAge);
//                postData.put("placeOfMeeting",mPlace);
//                postData.put("relation",mRelation);
//                postData.put("notes",mNotes);
//
//                String url = "http://192.168.4.152:5000/saveprofile/";
//
//
//                PostResponseAsyncTask readTask = new PostResponseAsyncTask(AddPerson.this, postData, new AsyncResponse() {
//                    @Override
//                    public void processFinish(String s) {
//
//                        Intent i = new Intent(AddPerson.this,MainActivity.class);
//                        startActivity(i);
//                        finish();
//
//                    }
//                });
//                readTask.execute(url);
                submitPerson();
            }
        });

    }

    private void init() {
        mContactName = (EditText)findViewById(R.id.contactName);
        mContactLivesIn = (EditText)findViewById(R.id.contactLivesIn);
        mContactAge = (EditText)findViewById(R.id.contactAge);
        mContactPlace = (EditText)findViewById(R.id.contactPlace);
        mContactRelation = (EditText)findViewById(R.id.contactRelation);
        mContactNotes = (EditText)findViewById(R.id.contactNotes);
        mContactSubmit = (Button)findViewById(R.id.contactSubmit);
        progress = new ProgressDialog(this);

    }

    private void submitPerson() {

        progress.show();
        progress.setMessage("Saving person to the database. Please wait.");

        String subjectId = mName;
        String galleryId = "MyGallery";
        String selector = "FULL";
        String multipleFaces = "false";
        String minHeadScale = "0.25";
        try {
            myKairos.listGalleries(listener);
            myKairos.enroll(photo,
                    subjectId,
                    galleryId,
                    selector,
                    multipleFaces,
                    minHeadScale,
                    listener);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}