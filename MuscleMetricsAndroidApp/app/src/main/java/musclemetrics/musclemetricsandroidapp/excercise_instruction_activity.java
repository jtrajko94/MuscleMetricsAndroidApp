package musclemetrics.musclemetricsandroidapp;

/**
 * Created by JerunTrajko on 3/1/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class excercise_instruction_activity extends AppCompatActivity {

    SegmentedGroup segmentedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Excercise ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_lib_exc_ins);

        //Set bottom toolbar buttons
        setBottomToolbar();

        //Set Top Toolbar
        setTopToolbar();

        final RadioButton muscleR = (RadioButton) findViewById(R.id.muscles);
        final RadioButton videoR = (RadioButton) findViewById(R.id.video);

        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == videoR.getId())
                {
                    Intent intentApp = new Intent(excercise_instruction_activity.this,
                            excercise_video_activity.class);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentApp);
                    finish();
                }
                else if(checkedId == muscleR.getId())
                {
                    Intent intentApp = new Intent(excercise_instruction_activity.this,
                            excercise_muscle_activity.class);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentApp);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(excercise_instruction_activity.this,
                library_activity.class);
        intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentApp);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.workout_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Set Bottom Toolbar
    private void setBottomToolbar()
    {
        final Button work = (Button) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final Button lib = (Button) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        library_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final Button cal = (Button) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final Button pro = (Button) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final Button prof = (Button) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        profile_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

    }


    //Set Top Toolbar
    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Excercise");
        setSupportActionBar(toolbar);
    }
}

