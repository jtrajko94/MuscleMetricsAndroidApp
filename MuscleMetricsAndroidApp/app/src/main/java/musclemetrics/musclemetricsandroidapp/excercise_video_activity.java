package musclemetrics.musclemetricsandroidapp;

/**
 * Created by JerunTrajko on 3/1/16.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class excercise_video_activity extends AppCompatActivity {

    SegmentedGroup segmentedGroup;

    private VideoView videoView;
    private MediaController mController;
    private Uri uriYouTube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Excercise ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_lib_exc_vid);

        //Set bottom toolbar buttons
        setBottomToolbar();

        //Set Top Toolbar
        setTopToolbar();

        final RadioButton muscleR = (RadioButton) findViewById(R.id.muscles);
        final RadioButton instructionR = (RadioButton) findViewById(R.id.instructions);

        segmentedGroup = (SegmentedGroup) findViewById(R.id.segmented2);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == muscleR.getId())
                {
                    Intent intentApp = new Intent(excercise_video_activity.this,
                            excercise_muscle_activity.class);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentApp);
                    finish();
                }
                else if(checkedId == instructionR.getId())
                {
                    Intent intentApp = new Intent(excercise_video_activity.this,
                            excercise_instruction_activity.class);
                    intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentApp);
                    finish();
                }
            }
        });

        new AsyncTaskParseJson().execute();

        String path1="http://videocdn.bodybuilding.com/video/mp4/52000/53781m.mp4";

        Uri uri=Uri.parse(path1);

        VideoView video=(VideoView)findViewById(R.id.videoView);
        video.setMediaController(new MediaController(this));
        video.setVideoURI(uri);
        video.start();
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(excercise_video_activity.this,
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
        final ImageButton work = (ImageButton) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_video_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton lib = (ImageButton) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_video_activity.this,
                        library_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_video_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(excercise_video_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_video_activity.this,
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

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String yourJsonStringUrl = "http://metricsapi-dev-2sefhf4udj.elasticbeanstalk.com/records";

        // contacts JSONArray
        JSONArray dataJsonArr = null;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... arg0) {

            try {

                // instantiate our json parser
                jsonParser jParser = new jsonParser();

                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);

                // get the array of users
                dataJsonArr = json.getJSONArray("Records");

                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {

                    JSONObject c = dataJsonArr.getJSONObject(i);

                    // Storing each json item in variable
                    String firstname = c.getString("activity_name");
                    //String lastname = c.getString("lastname");
                    //String username = c.getString("username");

                    // show the values in our logcat
                    Log.e(TAG, "firstname: " + firstname
                            + ", lastname: " + firstname
                            + ", username: " + firstname);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {}
    }
}

