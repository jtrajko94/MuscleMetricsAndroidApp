package musclemetrics.musclemetricsandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class all_workouts_activity extends AppCompatActivity {

    ArrayList<String> list = new ArrayList<String>();
    ListView lView;
    ImageButton add;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Layout ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_all_workouts);

        lView = (ListView) findViewById(R.id.workoutList);
        lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        MyCustomAdapterAllWorkouts adapter = new MyCustomAdapterAllWorkouts(list, this);
        list.add("Temporary Workout");
        lView.setAdapter(adapter);

        //Set bottom toolbar buttons
        setBottomToolbar();

        //Set Top Toolbar
        setTopToolbar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        final ImageButton work = (ImageButton) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(all_workouts_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(all_workouts_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(all_workouts_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(all_workouts_activity.this,
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
        toolbar.setTitle("All Workouts");
        setSupportActionBar(toolbar);

        //Dialog when adding a workout
        add = (ImageButton) findViewById(R.id.addWorkout);
        add.setFocusable(false);
        add.setFocusableInTouchMode(false);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("CREATE A WORKOUT");
                alertDialog.setMessage("Enter the Workout Name:");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.add_button);

                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String name = input.getText().toString();
                                if(name.compareTo("") != 0)
                                {
                                    list.add(name);
                                }
                                else
                                {
                                    dialog.cancel();
                                }
                            }
                        });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });
    }
}
