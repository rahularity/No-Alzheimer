package com.ithought.rahul.nozimers.AddANewFace;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ithought.rahul.nozimers.MainActivity;
import com.ithought.rahul.nozimers.PeopleAroundYou.Utility;
import com.ithought.rahul.nozimers.R;
import com.ithought.rahul.nozimers.RetrofitClient;
import com.ithought.rahul.nozimers.models.AddingPeopleStatusObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddANewFaceActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "ChildrenEntryActivity";
    private Context context = this;
    private CircleImageView childPhoto;
    private EditText name, livesIn, contactNumber, age, placeOfMeeting, relation, notes;
    private Button saveDetails;
    private Bitmap imageBitmap;
    private String userChoosenTask;
    private String encoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anew_face);

        init();

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savingDetailsProcess();
            }
        });

        childPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void savingDetailsProcess() {

        String name_text = name.getText().toString().trim();
        String livesIn_text = livesIn.getText().toString().trim();
        String contact_text = contactNumber.getText().toString().trim();
        String age_text = age.getText().toString().trim();
        String place_of_meeting_text = placeOfMeeting.getText().toString().trim();
        String relation_text = relation.getText().toString().trim();
        String notes_text = notes.getText().toString().trim();

        if(imageBitmap == null){
            Toast.makeText(this,"please provide a picture of the missing person.",Toast.LENGTH_LONG).show();
        }

        else if (name_text.isEmpty())
            inputValidation(name,"Person's name is required");

        else if (livesIn_text.isEmpty())
            inputValidation(livesIn,"Where does the person lives?");

        else if (place_of_meeting_text.isEmpty())
            inputValidation(placeOfMeeting,"Where you met this person?");

        else if (contact_text.isEmpty())
            inputValidation(contactNumber,"Please provide a contact number of this person.");

        else if (age_text.isEmpty())
            inputValidation(age,"What is the age of this person?");

        else if (relation_text.isEmpty())
            inputValidation(relation,"What is your relation with this person?");

        else{

            Call<AddingPeopleStatusObject> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .add(encoded , name_text,  livesIn_text, contact_text, age_text, place_of_meeting_text, relation_text,
                            notes_text);

            call.enqueue(new Callback<AddingPeopleStatusObject>() {
                @Override
                public void onResponse(Call<AddingPeopleStatusObject> call, Response<AddingPeopleStatusObject> response) {
                    Log.d(TAG, "onResponse: Server Response; " + response.toString());

                    int responseCode = response.code();

                    if(responseCode == 200){

                        String status = response.body().getStatus();
                        Log.d(TAG, "onResponse : " + status);
                        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddANewFaceActivity.this,MainActivity.class));
                        finish();


                    }else{
                        Toast.makeText(context,"responseCode = " + responseCode, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AddingPeopleStatusObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                    Toast.makeText(AddANewFaceActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

                }

            });
        }


    }

    private void init() {

        saveDetails = (Button)findViewById(R.id.save_details);
        childPhoto = (CircleImageView)findViewById(R.id.missing_pic);
        name = (EditText)findViewById(R.id.name);
        livesIn = (EditText)findViewById(R.id.lives_in);
        contactNumber = (EditText)findViewById(R.id.contact_no);
        age = (EditText)findViewById(R.id.age);
        placeOfMeeting = (EditText)findViewById(R.id.place_of_meeting);
        relation = (EditText)findViewById(R.id.relation);
        notes = (EditText)findViewById(R.id.notes);
    }


    private void inputValidation(EditText et, String msg){
        et.setError(msg);
        et.requestFocus();
    }

    //getting image from gallery or camera
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddANewFaceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddANewFaceActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void onCaptureImageResult(Intent data) {
        imageBitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        childPhoto.setImageBitmap(imageBitmap);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data != null) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        childPhoto.setImageBitmap(imageBitmap);
    }
}
