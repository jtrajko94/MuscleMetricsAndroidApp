package musclemetrics.musclemetricsandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by JerunTrajko on 1/18/16.
 */
public class options_my_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("In Library Create ----------------------");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_toolbar_option_profile);

        setTopToolbar();
        setBottomToolbar();
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(options_my_profile.this,
                profile_activity.class);
        startActivity(intentApp);
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

    //Set the top toolbar
    private void setTopToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);
    }

    //set navigation toolbar at the bottom
    private void setBottomToolbar()
    {
        final Button work = (Button) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(options_my_profile.this,
                        workout_activity.class);
                startActivity(intentApp);
            }
        });

        final Button lib = (Button) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(options_my_profile.this,
                        library_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });

        final Button cal = (Button) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(options_my_profile.this,
                        calendar_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });

        final Button prog = (Button) findViewById(R.id.progressButton);
        prog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(options_my_profile.this,
                        progress_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });

        final Button prof = (Button) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(options_my_profile.this,
                        profile_activity.class);
                startActivity(intentApp);
                // Perform action on click
            }
        });
    }
}
