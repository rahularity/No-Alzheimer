package com.ithought.rahul.nozimers.PeopleAroundYou;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ithought.rahul.nozimers.R;
import com.ithought.rahul.nozimers.RetrofitClient;
import com.ithought.rahul.nozimers.models.Person;
import com.ithought.rahul.nozimers.models.UserObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleAroudYouActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final String TAG = "HomeActivity2";
    private ImageView capturedImage;
    private Bitmap imageBitmap;
    private Button recognize;
    public static final int ACTIVITY_NUM = 0;
    private Context context = this;
    private String userChoosenTask;
    private String encoded = "";
    private RecyclerView recyclerView;
    private ProgressBar progress;
    private TextView instructionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_aroud_you);

        init();

        capturedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(encoded.isEmpty()){
                    Toast.makeText(context,"Please provide a picture of the child to recognize",Toast.LENGTH_LONG).show();
                }else{
                    faceRecognition();
                }

            }
        });

    }

    private void faceRecognition() {

        recyclerView.setVisibility(View.GONE);
        instructionText.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        Call<UserObject> call  = RetrofitClient.getInstance().getApi().compare(encoded);

        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());

                int responseCode = response.code();

                if(responseCode == 200){

                    String status = response.body().getStatus();
                    if(status.equals("matched")){

                        List<Person> person = response.body().getPerson();
                        int numberOfChildrenMatched = person.size();

                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new PersonAdapter(person, R.layout.list_item_person, getApplicationContext()));
                        progress.setVisibility(View.GONE);
                        Toast.makeText(context, "Successfully fetched. There are " + numberOfChildrenMatched + " matched faces from our database.", Toast.LENGTH_LONG).show();



                    }else{
                        //TODO: when there is no match for the picture clicked
                        progress.setVisibility(View.GONE);
                        instructionText.setText("Alas! No face in our database is matched with this child.");
                        instructionText.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Alas! No face in our database is matched with this child.", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {

                Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                progress.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                Toast.makeText(PeopleAroudYouActivity.this,"Something went wrong...please try again.",Toast.LENGTH_LONG).show();

            }
        });

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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PeopleAroudYouActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(PeopleAroudYouActivity.this);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        imageBitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] byteArray = bytes.toByteArray();

        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        capturedImage.setImageBitmap(imageBitmap);


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

        capturedImage.setImageBitmap(imageBitmap);
    }

    private void init() {
        instructionText = (TextView)findViewById(R.id.instruction_text);
        progress = (ProgressBar)findViewById(R.id.progress);
        capturedImage = (ImageView)findViewById(R.id.captured_image);
        recognize = (Button)findViewById(R.id.recognize_button);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
