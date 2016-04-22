package musclemetrics.musclemetricsandroidapp;

/**
 * Created by JerunTrajko on 3/1/16.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by JerunTrajko on 1/15/16.
 */
public class excercise_instruction_activity extends AppCompatActivity {

    SegmentedGroup segmentedGroup;
    String jsonText = "";
    String vidURL = "";
    Toolbar toolbar;
    TextView instructionsText;
    ImageView thumbView;

    String yourJsonStringUrl = "http://metricsapi-dev-2sefhf4udj.elasticbeanstalk.com/records";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Excercise ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_lib_exc_ins);
        instructionsText = (TextView) findViewById(R.id.instText);
        instructionsText.setMovementMethod(new ScrollingMovementMethod());
        thumbView = (ImageView) findViewById(R.id.thumbnail);

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

        new AsyncTaskParseJson().execute();
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
        final ImageButton work = (ImageButton) findViewById(R.id.workoutButton);
        work.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton lib = (ImageButton) findViewById(R.id.libraryButton);
        lib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        library_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(excercise_instruction_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.argb(255, 12, 194, 247));
        setSupportActionBar(toolbar);
    }

    //Access records API and parse
    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String yourJsonStringUrl = "http://metricsapi-dev-2sefhf4udj.elasticbeanstalk.com/records";
        String tempToolName = "";
        // contacts JSONArray
        Bitmap bmp1 = null;
        Bitmap bmp2 = null;
        String tempInst = "";
        String thumbOne = "";
        String thumbTwo = "";

        @Override
        protected void onPreExecute() {}

        //Sets up the connection and calls the parse function
        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder response  = new StringBuilder();

            URL url = null;
            try {
                url = new URL(yourJsonStringUrl);
                HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null)
                    {
                        response.append(strLine);
                    }
                    input.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonText = response.toString();
            parseJSON(jsonText);
            return response.toString();
        }

        //Get all necessary fields
        protected void parseJSON(String response)
        {
            JSONArray mainResponseObject = null;
            try {
                mainResponseObject = new JSONArray(response);
                JSONObject parse = mainResponseObject.getJSONObject(275);
                tempInst = parse.get("activity_instructions").toString();
                thumbOne = parse.get("image_0").toString();
                thumbTwo = parse.get("image_1").toString();
                tempToolName = parse.get("activity_name").toString();
                InputStream in1 = new URL(thumbOne).openStream();
                bmp1 = BitmapFactory.decodeStream(in1);
                InputStream in2 = new URL(thumbTwo).openStream();
                bmp2 = BitmapFactory.decodeStream(in2);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Update the instructions on the page and set up a new thread alternating the thumbnails
        @Override
        protected void onPostExecute(String strFromDoInBg) {
            instructionsText.setText(tempInst);
            toolbar.setTitle(tempToolName);
            Thread timer = new Thread()
            {
                public void run()
                {
                    while(1==1) {
                        try {
                            sleep(2000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (bmp1 != null) {
                                        thumbView.setImageBitmap(bmp1);
                                        thumbView.invalidate();
                                    }
                                }
                            });
                            sleep(2000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (bmp2 != null) {
                                        thumbView.setImageBitmap(bmp2);
                                        thumbView.invalidate();
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            timer.start();
        }
    }
}

