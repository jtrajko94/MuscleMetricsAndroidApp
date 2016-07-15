package musclemetrics.musclemetricsandroidapp;

import android.content.Context;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JerunTrajko on 1/15/16.
 */

public class excercise_search_activity extends AppCompatActivity {


    String jsonText = "";
    ListView lView;
    Context context = this;
    MyCustomAdapterSearchExcercises adapter;
    ImageButton search;
    EditText editName;
    ArrayList<excercise_entry> list = new ArrayList<excercise_entry>();
    String yourJsonStringUrl = "https://mymetricsapi.herokuapp.com/records";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Creating Layout ----------------------");
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.full_toolbar_excercise_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editName = (EditText) findViewById(R.id.searchBar);
        search = (ImageButton) findViewById(R.id.searchExcercise);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AsyncTaskSearchParseJson().execute();
            }
        });

        lView = (ListView) findViewById(R.id.workoutList);
        lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new MyCustomAdapterSearchExcercises(list, context);
        lView.setAdapter(adapter);

        //Set bottom toolbar buttons
        setBottomToolbar();

        //Set Top Toolbar
        setTopToolbar();

        new AsyncTaskParseJson().execute();
    }

    @Override
    public void onBackPressed() {
        Intent intentApp = new Intent(excercise_search_activity.this,
                one_workout_activity.class);
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
                Intent intentApp = new Intent(excercise_search_activity.this,
                        workout_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton cal = (ImageButton) findViewById(R.id.calendarButton);
        cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_search_activity.this,
                        calendar_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton pro = (ImageButton) findViewById(R.id.progressButton);
        pro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentApp = new Intent(excercise_search_activity.this,
                        progress_activity.class);
                intentApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentApp);
                finish();
            }
        });

        final ImageButton prof = (ImageButton) findViewById(R.id.profileButton);
        prof.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentApp = new Intent(excercise_search_activity.this,
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
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String yourJsonStringUrl = "https://mymetricsapi.herokuapp.com/records";

        @Override
        protected void onPreExecute() {}

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

        protected void parseJSON(String response)
        {
            JSONObject mainResponseObject = null;
            try {
                mainResponseObject = new JSONObject(response);
                JSONArray array = new JSONArray(mainResponseObject.get("Records").toString());
                for(int i =0; i<array.length(); i++)
                {
                    JSONObject parse = array.getJSONObject(i);
                    excercise_entry temp = new excercise_entry();
                    temp.activity_name = parse.get("activity_name").toString();
                    temp.activity_primary_muscles = parse.get("activity_primary_muscles").toString();
                    temp.thumbnail = parse.get("image_0").toString();
                    list.add(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            adapter.notifyDataSetChanged();
            //TODO: Fix the thumbnail, plus need to add search functionality
        }
    }

    public class AsyncTaskSearchParseJson extends AsyncTask<String, String, String> {

        final String TAG = "AsyncTaskParseJson.java";

        // set your json string url here
        String yourJsonStringUrl = "http://metricsapi-dev-2sefhf4udj.elasticbeanstalk.com/records";

        @Override
        protected void onPreExecute() {}

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

        protected void parseJSON(String response)
        {
            list.clear();
            JSONObject mainResponseObject = null;
            try {
                mainResponseObject = new JSONObject(response);
                JSONArray array = new JSONArray(mainResponseObject.get("Records").toString());
                for(int i =0; i<array.length(); i++)
                {
                    JSONObject parse = array.getJSONObject(i);
                    String actName = parse.get("activity_name").toString();

                    String editUpper = editName.getText().toString().toUpperCase();
                    String activityUpper = actName.toUpperCase();
                    if(activityUpper.contains(editUpper) && editUpper != "")
                    {
                        excercise_entry temp = new excercise_entry();
                        temp.activity_name = parse.get("activity_name").toString();
                        temp.activity_primary_muscles = parse.get("activity_primary_muscles").toString();
                        temp.thumbnail = parse.get("image_0").toString();
                        list.add(temp);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {
            adapter.notifyDataSetChanged();
            //TODO: Fix the thumbnail, plus need to add search functionality
        }
    }
}
