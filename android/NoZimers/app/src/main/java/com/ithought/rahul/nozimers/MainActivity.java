package com.ithought.rahul.nozimers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ithought.rahul.nozimers.AddANewFace.AddANewFaceActivity;
import com.ithought.rahul.nozimers.PeopleAroundYou.PeopleAroudYouActivity;
import com.ithought.rahul.nozimers.PeopleIKnow.PeopleIKnowActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button peopleAroundYou,peopleIKnow, addANewFace;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: OnCreate running");
        init();

        addANewFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening activity for adding new person");
                startActivity(new Intent(MainActivity.this, AddANewFaceActivity.class));
            }
        });

        peopleAroundYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "onClick: opening people around you activity");
                startActivity(new Intent(MainActivity.this, PeopleAroudYouActivity.class));

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
        startActivity(new Intent(MainActivity.this, PeopleIKnowActivity.class));
    }

    private void init() {
        addANewFace = (Button)findViewById(R.id.add_a_new_face);
        peopleIKnow = (Button)findViewById(R.id.peopleIKnow);
        peopleAroundYou = (Button)findViewById(R.id.people_around_you);
        toolbar = (Toolbar)findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mic_menu_item:
                startActivity(new Intent(MainActivity.this,AssistantActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
