package com.ithought.rahul.nozimers.PeopleIKnow;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ithought.rahul.nozimers.PeopleAroundYou.PersonAdapter;
import com.ithought.rahul.nozimers.R;
import com.ithought.rahul.nozimers.RetrofitClient;
import com.ithought.rahul.nozimers.models.Person;
import com.ithought.rahul.nozimers.models.UserObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleIKnowActivity extends AppCompatActivity {

    private static final String TAG = "PeopleIKnowActivity";
    private Context context = this;
    private ProgressBar progress;
    private TextView instructionText;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_iknow);

        init();
        faceRecognition();


    }

    private void refresh() {
        faceRecognition();
    }

    private void faceRecognition() {

        recyclerView.setVisibility(View.GONE);
        instructionText.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        Call<UserObject> call = RetrofitClient
                .getInstance()
                .getApi()
                .getall();

        call.enqueue(new Callback<UserObject>() {
            @Override
            public void onResponse(Call<UserObject> call, Response<UserObject> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());

                int responseCode = response.code();

                if(responseCode == 200){

                    String status = response.body().getStatus();
                    if(status.equals("Found")){

                        List<Person> children = response.body().getPerson();
                        int numberOfChildrenMatched = children.size();

                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(new PersonAdapter(children,R.layout.list_item_person, getApplicationContext()));
                        progress.setVisibility(View.GONE);
                        Toast.makeText(context, "Successfully fetched. There are " + numberOfChildrenMatched + " people you know.", Toast.LENGTH_LONG).show();



                    }else{
                        //TODO: when there is no match for the picture clicked
                        progress.setVisibility(View.GONE);
                        instructionText.setText("There are no people to show in your database");
                        instructionText.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "There are no people in our database", Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<UserObject> call, Throwable t) {

                Log.e(TAG, "onFailure: something went wrong." + t.getMessage());
                progress.setVisibility(View.GONE);
                instructionText.setVisibility(View.VISIBLE);
                Toast.makeText(PeopleIKnowActivity.this,"Something went wrong...please try again.",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void init() {
        instructionText = (TextView)findViewById(R.id.instruction_text);
        progress = (ProgressBar)findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.listRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar)findViewById(R.id.list_activity_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu_item:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
